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
 * Options and information specific to, and only present on, Swarm CSI cluster volumes.
 *
 * @param id The Swarm ID of this volume. Because cluster volumes are Swarm objects, they have an ID, unlike non-cluster volumes. This ID can be used to refer to the Volume instead of the name.
 * @param version
 * @param createdAt
 * @param updatedAt
 * @param spec
 * @param info
 * @param publishStatus The status of the volume as it pertains to its publishing and use on specific nodes
 */
@Serializable
data class ClusterVolume(
    // The Swarm ID of this volume. Because cluster volumes are Swarm objects, they have an ID, unlike non-cluster volumes. This ID can be used to refer to the Volume instead of the name.
    @SerialName(value = "ID") val id: String? = null,
    @SerialName(value = "Version") val version: ObjectVersion? = null,
    @SerialName(value = "CreatedAt") val createdAt: String? = null,
    @SerialName(value = "UpdatedAt") val updatedAt: String? = null,
    @SerialName(value = "Spec") val spec: ClusterVolumeSpec? = null,
    @SerialName(value = "Info") val info: ClusterVolumeInfo? = null,
    // The status of the volume as it pertains to its publishing and use on specific nodes
    @SerialName(value = "PublishStatus") val publishStatus: List<ClusterVolumePublishStatusInner>? = null,
)
