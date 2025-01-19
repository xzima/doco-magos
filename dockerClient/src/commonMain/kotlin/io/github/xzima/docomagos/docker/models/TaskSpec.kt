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
 * User modifiable task configuration.
 *
 * @param pluginSpec
 * @param containerSpec
 * @param networkAttachmentSpec
 * @param resources
 * @param restartPolicy
 * @param placement
 * @param forceUpdate A counter that triggers an update even if no relevant parameters have been changed.
 * @param runtime Runtime is the type of runtime specified for the task executor.
 * @param networks Specifies which networks the service should attach to.
 * @param logDriver
 */
@Serializable
data class TaskSpec(
    @SerialName(value = "PluginSpec") val pluginSpec: TaskSpecPluginSpec? = null,
    @SerialName(value = "ContainerSpec") val containerSpec: TaskSpecContainerSpec? = null,
    @SerialName(value = "NetworkAttachmentSpec") val networkAttachmentSpec: TaskSpecNetworkAttachmentSpec? = null,
    @SerialName(value = "Resources") val resources: TaskSpecResources? = null,
    @SerialName(value = "RestartPolicy") val restartPolicy: TaskSpecRestartPolicy? = null,
    @SerialName(value = "Placement") val placement: TaskSpecPlacement? = null,
    // A counter that triggers an update even if no relevant parameters have been changed.
    @SerialName(value = "ForceUpdate") val forceUpdate: Int? = null,
    // Runtime is the type of runtime specified for the task executor.
    @SerialName(value = "Runtime") val runtime: String? = null,
    // Specifies which networks the service should attach to.
    @SerialName(value = "Networks") val networks: List<NetworkAttachmentConfig>? = null,
    @SerialName(value = "LogDriver") val logDriver: TaskSpecLogDriver? = null,
)
