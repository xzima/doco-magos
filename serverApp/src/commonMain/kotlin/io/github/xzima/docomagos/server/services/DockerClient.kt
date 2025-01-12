package io.github.xzima.docomagos.server.services

import io.github.xzima.docomagos.server.services.models.DockerContainerInfo
import io.github.xzima.docomagos.server.services.models.DockerSystemInfo

interface DockerClient {
    suspend fun systemInfo(): DockerSystemInfo

    suspend fun containerInfoOrNull(idOrName: String): DockerContainerInfo?

    /**
     * @return (isSuccess, newContainerId)
     */
    suspend fun copyContainer(
        name: String,
        cmd: String,
        source: DockerContainerInfo,
        autoRemove: Boolean = true,
    ): Pair<Boolean, String>

    /**
     * @return true - container deleted; false - container not found
     */
    suspend fun deleteContainer(idOrName: String): Boolean

    /**
     * @return true - container started; false - container not found
     */
    suspend fun startContainer(idOrName: String): Boolean
}
