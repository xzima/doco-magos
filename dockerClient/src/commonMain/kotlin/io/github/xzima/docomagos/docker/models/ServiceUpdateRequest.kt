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
 * @param name Name of the service.
 * @param labels User-defined key/value metadata.
 * @param taskTemplate
 * @param mode
 * @param updateConfig
 * @param rollbackConfig
 * @param networks Specifies which networks the service should attach to.  Deprecated: This field is deprecated since v1.44. The Networks field in TaskSpec should be used instead.
 * @param endpointSpec
 */
@Serializable
data class ServiceUpdateRequest(
    // Name of the service.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    @SerialName(value = "TaskTemplate") val taskTemplate: TaskSpec? = null,
    @SerialName(value = "Mode") val mode: ServiceSpecMode? = null,
    @SerialName(value = "UpdateConfig") val updateConfig: ServiceSpecUpdateConfig? = null,
    @SerialName(value = "RollbackConfig") val rollbackConfig: ServiceSpecRollbackConfig? = null,
    // Specifies which networks the service should attach to.  Deprecated: This field is deprecated since v1.44. The Networks field in TaskSpec should be used instead.
    @SerialName(value = "Networks") val networks: List<NetworkAttachmentConfig>? = null,
    @SerialName(value = "EndpointSpec") val endpointSpec: EndpointSpec? = null,
)
