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
 * SELinux labels of the container
 *
 * @param disable Disable SELinux
 * @param user SELinux user label
 * @param role SELinux role label
 * @param type SELinux type label
 * @param level SELinux level label
 */
@Serializable
data class TaskSpecContainerSpecPrivilegesSELinuxContext(
    // Disable SELinux
    @SerialName(value = "Disable") val disable: Boolean? = null,
    // SELinux user label
    @SerialName(value = "User") val user: String? = null,
    // SELinux role label
    @SerialName(value = "Role") val role: String? = null,
    // SELinux type label
    @SerialName(value = "Type") val type: String? = null,
    // SELinux level label
    @SerialName(value = "Level") val level: String? = null,
)
