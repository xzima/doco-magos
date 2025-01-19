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
 * The config of a plugin.
 *
 * @param description
 * @param documentation
 * @param `interface`
 * @param entrypoint
 * @param workDir
 * @param network
 * @param linux
 * @param propagatedMount
 * @param ipcHost
 * @param pidHost
 * @param mounts
 * @param env
 * @param args
 * @param dockerVersion Docker Version used to create the plugin
 * @param user
 * @param rootfs
 */
@Serializable
data class PluginConfig(
    @SerialName(value = "Description") @Required val description: String,
    @SerialName(value = "Documentation") @Required val documentation: String,
    @SerialName(value = "Interface") @Required val `interface`: PluginConfigInterface,
    @SerialName(value = "Entrypoint") @Required val entrypoint: List<String>,
    @SerialName(value = "WorkDir") @Required val workDir: String,
    @SerialName(value = "Network") @Required val network: PluginConfigNetwork,
    @SerialName(value = "Linux") @Required val linux: PluginConfigLinux,
    @SerialName(value = "PropagatedMount") @Required val propagatedMount: String,
    @SerialName(value = "IpcHost") @Required val ipcHost: Boolean,
    @SerialName(value = "PidHost") @Required val pidHost: Boolean,
    @SerialName(value = "Mounts") @Required val mounts: List<PluginMount>,
    @SerialName(value = "Env") @Required val env: List<PluginEnv>,
    @SerialName(value = "Args") @Required val args: PluginConfigArgs,
    // Docker Version used to create the plugin
    @SerialName(value = "DockerVersion") val dockerVersion: String? = null,
    @SerialName(value = "User") val user: PluginConfigUser? = null,
    @SerialName(value = "rootfs") val rootfs: PluginConfigRootfs? = null,
)
