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
 * One cluster volume secret entry. Defines a key-value pair that is passed to the plugin.
 *
 * @param key Key is the name of the key of the key-value pair passed to the plugin.
 * @param secret Secret is the swarm Secret object from which to read data. This can be a Secret name or ID. The Secret data is retrieved by swarm and used as the value of the key-value pair passed to the plugin.
 */
@Serializable
data class ClusterVolumeSpecAccessModeSecretsInner(
    // Key is the name of the key of the key-value pair passed to the plugin.
    @SerialName(value = "Key") val key: String? = null,
    // Secret is the swarm Secret object from which to read data. This can be a Secret name or ID. The Secret data is retrieved by swarm and used as the value of the key-value pair passed to the plugin.
    @SerialName(value = "Secret") val secret: String? = null,
)
