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
    ): String?

    /**
     * @return true - container deleted; false - container not found
     */
    suspend fun deleteContainer(idOrName: String): Boolean

    /**
     * @return true - container started; false - container not found
     */
    suspend fun startContainer(idOrName: String): Boolean
}
