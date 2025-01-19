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
 * @param canRemove
 * @param detachKeys
 * @param id
 * @param running
 * @param exitCode
 * @param processConfig
 * @param openStdin
 * @param openStderr
 * @param openStdout
 * @param containerID
 * @param pid The system process ID for the exec process.
 */
@Serializable
data class ExecInspectResponse(
    @SerialName(value = "CanRemove") val canRemove: Boolean? = null,
    @SerialName(value = "DetachKeys") val detachKeys: String? = null,
    @SerialName(value = "ID") val id: String? = null,
    @SerialName(value = "Running") val running: Boolean? = null,
    @SerialName(value = "ExitCode") val exitCode: Int? = null,
    @SerialName(value = "ProcessConfig") val processConfig: ProcessConfig? = null,
    @SerialName(value = "OpenStdin") val openStdin: Boolean? = null,
    @SerialName(value = "OpenStderr") val openStderr: Boolean? = null,
    @SerialName(value = "OpenStdout") val openStdout: Boolean? = null,
    @SerialName(value = "ContainerID") val containerID: String? = null,
    // The system process ID for the exec process.
    @SerialName(value = "Pid") val pid: Int? = null,
)
