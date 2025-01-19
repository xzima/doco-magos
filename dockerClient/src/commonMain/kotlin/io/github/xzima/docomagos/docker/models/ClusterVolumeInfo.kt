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
 * Information about the global status of the volume.
 *
 * @param capacityBytes The capacity of the volume in bytes. A value of 0 indicates that the capacity is unknown.
 * @param volumeContext A map of strings to strings returned from the storage plugin when the volume is created.
 * @param volumeID The ID of the volume as returned by the CSI storage plugin. This is distinct from the volume's ID as provided by Docker. This ID is never used by the user when communicating with Docker to refer to this volume. If the ID is blank, then the Volume has not been successfully created in the plugin yet.
 * @param accessibleTopology The topology this volume is actually accessible from.
 */
@Serializable
data class ClusterVolumeInfo(
    // The capacity of the volume in bytes. A value of 0 indicates that the capacity is unknown.
    @SerialName(value = "CapacityBytes") val capacityBytes: Long? = null,
    // A map of strings to strings returned from the storage plugin when the volume is created.
    @SerialName(value = "VolumeContext") val volumeContext: Map<String, String>? = null,
    // The ID of the volume as returned by the CSI storage plugin. This is distinct from the volume's ID as provided by Docker. This ID is never used by the user when communicating with Docker to refer to this volume. If the ID is blank, then the Volume has not been successfully created in the plugin yet.
    @SerialName(value = "VolumeID") val volumeID: String? = null,
    // The topology this volume is actually accessible from.
    @SerialName(value = "AccessibleTopology") val accessibleTopology: List<Map<String, String>>? = null,
)
