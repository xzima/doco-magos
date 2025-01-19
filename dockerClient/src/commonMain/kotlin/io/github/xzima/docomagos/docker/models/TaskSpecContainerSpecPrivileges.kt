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
 * Security options for the container
 *
 * @param credentialSpec
 * @param seLinuxContext
 * @param seccomp
 * @param appArmor
 * @param noNewPrivileges Configuration of the no_new_privs bit in the container
 */
@Serializable
data class TaskSpecContainerSpecPrivileges(
    @SerialName(value = "CredentialSpec") val credentialSpec: TaskSpecContainerSpecPrivilegesCredentialSpec? = null,
    @SerialName(value = "SELinuxContext") val seLinuxContext: TaskSpecContainerSpecPrivilegesSELinuxContext? = null,
    @SerialName(value = "Seccomp") val seccomp: TaskSpecContainerSpecPrivilegesSeccomp? = null,
    @SerialName(value = "AppArmor") val appArmor: TaskSpecContainerSpecPrivilegesAppArmor? = null,
    // Configuration of the no_new_privs bit in the container
    @SerialName(value = "NoNewPrivileges") val noNewPrivileges: Boolean? = null,
)
