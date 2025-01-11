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
 * Specifies how a service should be attached to a particular network.
 *
 * @param target The target network for attachment. Must be a network name or ID.
 * @param aliases Discoverable alternate names for the service on this network.
 * @param driverOpts Driver attachment options for the network target.
 */
@Serializable
data class NetworkAttachmentConfig(
    // The target network for attachment. Must be a network name or ID.
    @SerialName(value = "Target") val target: String? = null,
    // Discoverable alternate names for the service on this network.
    @SerialName(value = "Aliases") val aliases: List<String>? = null,
    // Driver attachment options for the network target.
    @SerialName(value = "DriverOpts") val driverOpts: Map<String, String>? = null,
)
