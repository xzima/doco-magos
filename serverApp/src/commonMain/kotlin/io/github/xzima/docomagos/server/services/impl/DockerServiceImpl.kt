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
import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.ext.docker.runningStatuses
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.SyncJobProps
import io.github.xzima.docomagos.server.services.DockerClient
import io.github.xzima.docomagos.server.services.DockerService

private val logger = KotlinLogging.from(DockerServiceImpl::class)

class DockerServiceImpl(
    private val appProps: AppProps,
    private val syncJobProps: SyncJobProps,
    private val dockerClient: DockerClient,
) : DockerService {

    override suspend fun tryStartSyncJobService() {
        logger.debug { "try get info about current container by hostname: ${appProps.hostname}" }
        val current = dockerClient.containerInfoOrNull(appProps.hostname) ?: run {
            logger.error {
                "container by hostname/id: ${appProps.hostname} not found. Looks like app running outside of current docker"
            }
            return
        }

        logger.debug {
            "try create sync-job container(name=${syncJobProps.containerName}) imageName: ${current.imageName}"
        }
        val syncJobId = dockerClient.copyContainer(
            name = syncJobProps.containerName,
            cmd = syncJobProps.containerCmd,
            source = current,
        ) ?: run {
            logger.debug { "check container(name=${syncJobProps.containerName}) image and status" }
            val syncJob = dockerClient.containerInfoOrNull(syncJobProps.containerName) ?: run {
                logger.error { "container(name=${syncJobProps.containerName}) not found" }
                return
            }

            logger.debug { "fund container(name=${syncJobProps.containerName}) with id: ${syncJob.id}" }
            if (current.imageName != syncJob.imageName) {
                logger.error {
                    """
                    container(id=${syncJob.id}) use image: ${syncJob.imageName}, but expected: ${current.imageName}.
                    Looks like container name ${syncJobProps.containerName} used by another application.
                    Change sync-job container name to another and restart application manually.
                    """.trimIndent()
                }
                return
            }

            logger.info { "container(id=${syncJob.id}) status: ${syncJob.status}" }
            if (syncJob.status in ContainerState.Status.runningStatuses) {
                logger.info { "looks like sync-job container already working" }
                return
            }

            logger.debug { "try delete container(id=${syncJob.id})" }
            val isFound = dockerClient.deleteContainer(syncJob.id)
            if (!isFound) {
                logger.error { "container(id=${syncJob.id}) not found" }
                return
            }
            logger.debug { "container(id=${syncJob.id}) successfully deleted" }

            logger.debug {
                "try create sync-job container(name=${syncJobProps.containerName}) imageName: ${current.imageName} again"
            }
            return@run dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = current,
            ) ?: run {
                logger.error { "container(name=${syncJobProps.containerName}) still exist" }
                return
            }
        }
        logger.debug { "sync-job container(name=${syncJobProps.containerName}) created with id: $syncJobId" }

        logger.debug { "try start container(id=$syncJobId)" }
        val isFound = dockerClient.startContainer(idOrName = syncJobId)
        if (!isFound) {
            logger.error { "container(id=$syncJobId) not found" }
        } else {
            logger.debug { "container(id=$syncJobId) successfully started" }
        }
    }
}
