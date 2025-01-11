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
 * A request for devices to be sent to device drivers
 *
 * @param driver
 * @param count
 * @param deviceIDs
 * @param capabilities A list of capabilities; an OR list of AND lists of capabilities.
 * @param options Driver-specific options, specified as a key/value pairs. These options are passed directly to the driver.
 */
@Serializable
data class DeviceRequest(
    @SerialName(value = "Driver") val driver: String? = null,
    @SerialName(value = "Count") val count: Int? = null,
    @SerialName(value = "DeviceIDs") val deviceIDs: List<String>? = null,
    // A list of capabilities; an OR list of AND lists of capabilities.
    @SerialName(value = "Capabilities") val capabilities: List<List<String>>? = null,
    // Driver-specific options, specified as a key/value pairs. These options are passed directly to the driver.
    @SerialName(value = "Options") val options: Map<String, String>? = null,
)
