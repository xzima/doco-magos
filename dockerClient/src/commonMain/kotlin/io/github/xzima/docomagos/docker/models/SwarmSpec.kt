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
package io.github.xzima.docomagos.docker.models

import kotlinx.serialization.*

/**
 * User modifiable swarm configuration.
 *
 * @param name Name of the swarm.
 * @param labels User-defined key/value metadata.
 * @param orchestration
 * @param raft
 * @param dispatcher
 * @param caConfig
 * @param encryptionConfig
 * @param taskDefaults
 */
@Serializable
data class SwarmSpec(
    // Name of the swarm.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    @SerialName(value = "Orchestration") val orchestration: SwarmSpecOrchestration? = null,
    @SerialName(value = "Raft") val raft: SwarmSpecRaft? = null,
    @SerialName(value = "Dispatcher") val dispatcher: SwarmSpecDispatcher? = null,
    @SerialName(value = "CAConfig") val caConfig: SwarmSpecCAConfig? = null,
    @SerialName(value = "EncryptionConfig") val encryptionConfig: SwarmSpecEncryptionConfig? = null,
    @SerialName(value = "TaskDefaults") val taskDefaults: SwarmSpecTaskDefaults? = null,
)
