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
 * Additional metadata of the image in the local cache. This information is local to the daemon, and not part of the image itself.
 *
 * @param lastTagTime Date and time at which the image was last tagged in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.  This information is only available if the image was tagged locally, and omitted otherwise.
 */
@Serializable
data class ImageInspectMetadata(
    // Date and time at which the image was last tagged in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.  This information is only available if the image was tagged locally, and omitted otherwise.
    @SerialName(value = "LastTagTime") val lastTagTime: String? = null,
)
