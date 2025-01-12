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
import kotlinx.coroutines.*

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

    override suspend fun systemInfo(): DockerSystemInfo = withContext(Dispatchers.IO) {
        val response = systemApi.systemInfo()
        val body = when (response.status) {
            HttpStatusCode.OK -> response.body()
            else -> throw DockerApiException.from(response)
        }
        return@withContext DockerSystemInfo(
            daemonId = body.id,
            swarmNodeState = body.swarm?.localNodeState,
            serverVersion = body.serverVersion,
        )
    }

    override suspend fun containerInfoOrNull(idOrName: String): DockerContainerInfo? = withContext(Dispatchers.IO) {
        val response = containerApi.containerInspect(idOrName)
        val body = when (response.status) {
            HttpStatusCode.OK -> response.body()
            HttpStatusCode.NotFound -> return@withContext null
            else -> throw DockerApiException.from(response)
        }
        return@withContext DockerContainerInfo(
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
            // mounts = body.hostConfig?.mounts,
            autoRemove = true == body.hostConfig?.autoRemove,
            restartPolicy = body.hostConfig?.restartPolicy?.name ?: RestartPolicy.Name.UNKNOWN,
        )
    }

    override suspend fun copyContainer(
        name: String,
        cmd: String,
        source: DockerContainerInfo,
        autoRemove: Boolean,
    ): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        val response = containerApi.containerCreate(
            name = name,
            body = ContainerCreateRequest(
                image = source.imageName,
                env = source.envs,
                cmd = cmd.split(" "),
                hostConfig = HostConfig(
                    binds = source.binds,
                    autoRemove = autoRemove,
                    // mounts = source.mounts,
                ),
            ),
        )
        val body = when (response.status) {
            HttpStatusCode.Created -> response.body()
            HttpStatusCode.Conflict -> {
                val error = response.typedBody<ErrorResponse>()
                logger.warn { "Conflict on creation container($name): ${error.message}" }
                return@withContext false to ""
            }

            else -> throw DockerApiException.from(response)
        }
        body.warnings.forEach { message ->
            logger.warn { "On creation container($name): $message" }
        }

        return@withContext true to body.id
    }

    override suspend fun deleteContainer(idOrName: String): Boolean = withContext(Dispatchers.IO) {
        val response = containerApi.containerDelete(idOrName)
        return@withContext when (response.status) {
            HttpStatusCode.NoContent -> true
            HttpStatusCode.NotFound -> false
            else -> throw DockerApiException.from(response)
        }
    }

    override suspend fun startContainer(idOrName: String): Boolean = withContext(Dispatchers.IO) {
        val response = containerApi.containerStart(idOrName)
        return@withContext when (response.status) {
            HttpStatusCode.NoContent, HttpStatusCode.NotModified -> true
            HttpStatusCode.NotFound -> false
            else -> throw DockerApiException.from(response)
        }
    }
}
