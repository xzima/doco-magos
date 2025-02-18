/**
 * Copyright 2025 Alex Zima(xzima@ro.ru)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.xzima.docomagos.server.services.impl

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.docker.apis.ContainerApi
import io.github.xzima.docomagos.docker.apis.SystemApi
import io.github.xzima.docomagos.docker.infrastructure.ApiClient
import io.github.xzima.docomagos.docker.ktor.engine.socket.SocketCIO
import io.github.xzima.docomagos.docker.models.ContainerCreateRequest
import io.github.xzima.docomagos.docker.models.ErrorResponse
import io.github.xzima.docomagos.docker.models.HostConfig
import io.github.xzima.docomagos.docker.models.RestartPolicy
import io.github.xzima.docomagos.logging.HttpClientLogger
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.exceptions.DockerApiException
import io.github.xzima.docomagos.server.props.DockerProps
import io.github.xzima.docomagos.server.services.DockerClient
import io.github.xzima.docomagos.server.services.models.DockerContainerInfo
import io.github.xzima.docomagos.server.services.models.DockerSystemInfo
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

private val logger = KotlinLogging.from(DockerClientImpl::class)

class DockerClientImpl(
    private val props: DockerProps,
) : DockerClient {
    private val client = HttpClient(SocketCIO) {
        engine {
            unixSocketPath = props.unixSocketFile
        }
        install(Logging) {
            level = props.loggingLevel
            logger = HttpClientLogger()
        }
        install(ContentNegotiation) {
            json(ApiClient.JSON_DEFAULT)
        }
    }
    private val containerApi = ContainerApi(ApiClient.BASE_URL, client)
    private val systemApi = SystemApi(ApiClient.BASE_URL, client)

    override suspend fun systemInfo(): DockerSystemInfo {
        val response = systemApi.systemInfo()
        val body = when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> throw DockerApiException.from(response)
        }
        return DockerSystemInfo(
            daemonId = body.id,
            swarmNodeState = body.swarm?.localNodeState,
            serverVersion = body.serverVersion,
        )
    }

    override suspend fun containerInfoOrNull(idOrName: String): DockerContainerInfo? {
        val response = containerApi.containerInspect(idOrName)
        val body = when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> return null
            else -> throw DockerApiException.from(response)
        }
        return DockerContainerInfo(
            id = body.id,
            name = body.name,
            imageName = body.config?.image,
            hostname = body.config?.hostname,
            createdAt = body.created,
            startedAt = body.state?.startedAt,
            finishedAt = body.state?.finishedAt,
            status = body.state?.status,
            restartCount = body.restartCount ?: 0,
            envs = body.config?.env ?: emptyList(),
            labels = body.config?.labels ?: emptyMap(),
            binds = body.hostConfig?.binds ?: emptyList(),
            mounts = body.hostConfig?.mounts ?: emptyList(),
            autoRemove = true == body.hostConfig?.autoRemove,
            restartPolicy = body.hostConfig?.restartPolicy?.name ?: RestartPolicy.Name.UNKNOWN,
        )
    }

    override suspend fun copyContainer(
        name: String,
        cmd: String,
        source: DockerContainerInfo,
        autoRemove: Boolean,
    ): String? {
        val response = containerApi.containerCreate(
            name = name,
            body = ContainerCreateRequest(
                image = source.imageName,
                env = source.envs,
                cmd = cmd.split(" "),
                hostConfig = HostConfig(
                    binds = source.binds,
                    autoRemove = autoRemove,
                    mounts = source.mounts,
                ),
            ),
        )
        val body = when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.Conflict -> {
                val error = response.typedBody<ErrorResponse>()
                logger.warn { "Conflict on creation container($name): ${error.message}" }
                return null
            }

            else -> throw DockerApiException.from(response)
        }
        body.warnings.forEach { message ->
            logger.warn { "On creation container($name): $message" }
        }

        return body.id
    }

    override suspend fun deleteContainer(idOrName: String): Boolean {
        val response = containerApi.containerDelete(idOrName)
        return when (response.status) {
            HttpStatusCode.NoContent -> true
            HttpStatusCode.NotFound -> false
            else -> throw DockerApiException.from(response)
        }
    }

    override suspend fun startContainer(idOrName: String): Boolean {
        val response = containerApi.containerStart(idOrName)
        return when (response.status) {
            HttpStatusCode.NoContent, HttpStatusCode.NotModified -> true
            HttpStatusCode.NotFound -> false
            else -> throw DockerApiException.from(response)
        }
    }
}
