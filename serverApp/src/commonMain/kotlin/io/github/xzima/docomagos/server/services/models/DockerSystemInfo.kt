package io.github.xzima.docomagos.server.services.models

import io.github.xzima.docomagos.docker.models.LocalNodeState

data class DockerSystemInfo(
    val daemonId: String?,
    val swarmNodeState: LocalNodeState?,
    val serverVersion: String?,
)
