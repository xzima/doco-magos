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
 * File represents a specific target that is backed by a file.  <p><br /><p>  > **Note**: `Configs.File` and `Configs.Runtime` are mutually exclusive
 *
 * @param name Name represents the final filename in the filesystem.
 * @param uid UID represents the file UID.
 * @param gid GID represents the file GID.
 * @param mode Mode represents the FileMode of the file.
 */
@Serializable
data class TaskSpecContainerSpecConfigsInnerFile(
    // Name represents the final filename in the filesystem.
    @SerialName(value = "Name") val name: String? = null,
    // UID represents the file UID.
    @SerialName(value = "UID") val uid: String? = null,
    // GID represents the file GID.
    @SerialName(value = "GID") val gid: String? = null,
    // Mode represents the FileMode of the file.
    @SerialName(value = "Mode") val mode: Int? = null,
)
