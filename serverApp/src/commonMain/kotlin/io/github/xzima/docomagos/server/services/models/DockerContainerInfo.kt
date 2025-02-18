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
package io.github.xzima.docomagos.server.services.models

import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.docker.models.Mount
import io.github.xzima.docomagos.docker.models.RestartPolicy

data class DockerContainerInfo(
    val id: String,
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
    val mounts: List<Mount>,
    val autoRemove: Boolean,
    val restartPolicy: RestartPolicy.Name,
)
