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
 *
 *
 * @param id The ID of this container
 * @param names The names that this container has been given
 * @param image The name of the image used when creating this container
 * @param imageID The ID of the image that this container was created from
 * @param command Command to run when starting the container
 * @param created When the container was created
 * @param ports The ports exposed by this container
 * @param sizeRw The size of files that have been created or changed by this container
 * @param sizeRootFs The total size of all the files in this container
 * @param labels User-defined key/value metadata.
 * @param state The state of this container (e.g. `Exited`)
 * @param status Additional human-readable status of this container (e.g. `Exit 0`)
 * @param hostConfig
 * @param networkSettings
 * @param mounts
 */
@Serializable
data class ContainerSummary(
    // The ID of this container
    @SerialName(value = "Id") val id: String? = null,
    // The names that this container has been given
    @SerialName(value = "Names") val names: List<String>? = null,
    // The name of the image used when creating this container
    @SerialName(value = "Image") val image: String? = null,
    // The ID of the image that this container was created from
    @SerialName(value = "ImageID") val imageID: String? = null,
    // Command to run when starting the container
    @SerialName(value = "Command") val command: String? = null,
    // When the container was created
    @SerialName(value = "Created") val created: Long? = null,
    // The ports exposed by this container
    @SerialName(value = "Ports") val ports: List<Port>? = null,
    // The size of files that have been created or changed by this container
    @SerialName(value = "SizeRw") val sizeRw: Long? = null,
    // The total size of all the files in this container
    @SerialName(value = "SizeRootFs") val sizeRootFs: Long? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // The state of this container (e.g. `Exited`)
    @SerialName(value = "State") val state: String? = null,
    // Additional human-readable status of this container (e.g. `Exit 0`)
    @SerialName(value = "Status") val status: String? = null,
    @SerialName(value = "HostConfig") val hostConfig: ContainerSummaryHostConfig? = null,
    @SerialName(value = "NetworkSettings") val networkSettings: ContainerSummaryNetworkSettings? = null,
    @SerialName(value = "Mounts") val mounts: List<MountPoint>? = null,
)
