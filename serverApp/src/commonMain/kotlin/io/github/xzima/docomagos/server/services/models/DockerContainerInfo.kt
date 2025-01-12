package io.github.xzima.docomagos.server.services.models

import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.docker.models.RestartPolicy

data class DockerContainerInfo(
    val id: String?,
    val name: String?,
    val imageName: String?,
    val hostname: String?,
    val createdAt: String?,
    val startedAt: String?,
    val finishedAt: String?,
    val status: ContainerState.Status?,
    val restartCount: Int,
    val envs: List<String>,
    val labels: Map<String, String>,
    val binds: List<String>,
    // val mounts: List<Mount>,
    val autoRemove: Boolean,
    val restartPolicy: RestartPolicy.Name,
)
