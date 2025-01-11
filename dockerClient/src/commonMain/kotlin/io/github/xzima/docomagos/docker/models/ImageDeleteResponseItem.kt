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
 * @param untagged The image ID of an image that was untagged
 * @param deleted The image ID of an image that was deleted
 */
@Serializable
data class ImageDeleteResponseItem(
    // The image ID of an image that was untagged
    @SerialName(value = "Untagged") val untagged: String? = null,
    // The image ID of an image that was deleted
    @SerialName(value = "Deleted") val deleted: String? = null,
)
