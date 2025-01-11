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
 * @param id The ID of the container
 * @param created The time the container was created
 * @param path The path to the command being run
 * @param args The arguments to the command being run
 * @param state
 * @param image The container's image ID
 * @param resolvConfPath
 * @param hostnamePath
 * @param hostsPath
 * @param logPath
 * @param name
 * @param restartCount
 * @param driver
 * @param platform
 * @param mountLabel
 * @param processLabel
 * @param appArmorProfile
 * @param execIDs IDs of exec instances that are running in the container.
 * @param hostConfig
 * @param graphDriver
 * @param sizeRw The size of files that have been created or changed by this container.
 * @param sizeRootFs The total size of all the files in this container.
 * @param mounts
 * @param config
 * @param networkSettings
 */
@Serializable
data class ContainerInspectResponse(
    // The ID of the container
    @SerialName(value = "Id") val id: String? = null,
    // The time the container was created
    @SerialName(value = "Created") val created: String? = null,
    // The path to the command being run
    @SerialName(value = "Path") val path: String? = null,
    // The arguments to the command being run
    @SerialName(value = "Args") val args: List<String>? = null,
    @SerialName(value = "State") val state: ContainerState? = null,
    // The container's image ID
    @SerialName(value = "Image") val image: String? = null,
    @SerialName(value = "ResolvConfPath") val resolvConfPath: String? = null,
    @SerialName(value = "HostnamePath") val hostnamePath: String? = null,
    @SerialName(value = "HostsPath") val hostsPath: String? = null,
    @SerialName(value = "LogPath") val logPath: String? = null,
    @SerialName(value = "Name") val name: String? = null,
    @SerialName(value = "RestartCount") val restartCount: Int? = null,
    @SerialName(value = "Driver") val driver: String? = null,
    @SerialName(value = "Platform") val platform: String? = null,
    @SerialName(value = "MountLabel") val mountLabel: String? = null,
    @SerialName(value = "ProcessLabel") val processLabel: String? = null,
    @SerialName(value = "AppArmorProfile") val appArmorProfile: String? = null,
    // IDs of exec instances that are running in the container.
    @SerialName(value = "ExecIDs") val execIDs: List<String>? = null,
    @SerialName(value = "HostConfig") val hostConfig: HostConfig? = null,
    @SerialName(value = "GraphDriver") val graphDriver: DriverData? = null,
    // The size of files that have been created or changed by this container.
    @SerialName(value = "SizeRw") val sizeRw: Long? = null,
    // The total size of all the files in this container.
    @SerialName(value = "SizeRootFs") val sizeRootFs: Long? = null,
    @SerialName(value = "Mounts") val mounts: List<MountPoint>? = null,
    @SerialName(value = "Config") val config: ContainerConfig? = null,
    @SerialName(value = "NetworkSettings") val networkSettings: NetworkSettings? = null,
)
