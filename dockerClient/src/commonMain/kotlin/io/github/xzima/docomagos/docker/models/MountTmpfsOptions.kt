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
 * Optional configuration for the `tmpfs` type.
 *
 * @param sizeBytes The size for the tmpfs mount in bytes.
 * @param mode The permission mode for the tmpfs mount in an integer.
 * @param options The options to be passed to the tmpfs mount. An array of arrays. Flag options should be provided as 1-length arrays. Other types should be provided as 2-length arrays, where the first item is the key and the second the value.
 */
@Serializable
data class MountTmpfsOptions(
    // The size for the tmpfs mount in bytes.
    @SerialName(value = "SizeBytes") val sizeBytes: Long? = null,
    // The permission mode for the tmpfs mount in an integer.
    @SerialName(value = "Mode") val mode: Int? = null,
    // The options to be passed to the tmpfs mount. An array of arrays. Flag options should be provided as 1-length arrays. Other types should be provided as 2-length arrays, where the first item is the key and the second the value.
    @SerialName(value = "Options") val options: List<List<String>>? = null,
)
