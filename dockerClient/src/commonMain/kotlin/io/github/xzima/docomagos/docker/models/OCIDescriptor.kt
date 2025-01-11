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
 * A descriptor struct containing digest, media type, and size, as defined in the [OCI Content Descriptors Specification](https://github.com/opencontainers/image-spec/blob/v1.0.1/descriptor.md).
 *
 * @param mediaType The media type of the object this schema refers to.
 * @param digest The digest of the targeted content.
 * @param propertySize The size in bytes of the blob.
 */
@Serializable
data class OCIDescriptor(
    // The media type of the object this schema refers to.
    @SerialName(value = "mediaType") val mediaType: String? = null,
    // The digest of the targeted content.
    @SerialName(value = "digest") val digest: String? = null,
    // The size in bytes of the blob.
    @SerialName(value = "size") val propertySize: Long? = null,
)
