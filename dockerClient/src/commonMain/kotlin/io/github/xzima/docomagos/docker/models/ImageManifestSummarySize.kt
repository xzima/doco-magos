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
 * @param total Total is the total size (in bytes) of all the locally present data (both distributable and non-distributable) that's related to this manifest and its children. This equal to the sum of Content size AND all the sizes in the Size struct present in the Kind-specific data struct. For example, for an image kind (Kind == \"image\") this would include the size of the image content and unpacked image snapshots (Size.Content + ImageData.Size.Unpacked).
 * @param content Content is the size (in bytes) of all the locally present content in the content store (e.g. image config, layers) referenced by this manifest and its children. This only includes blobs in the content store.
 */
@Serializable
data class ImageManifestSummarySize(
    // Total is the total size (in bytes) of all the locally present data (both distributable and non-distributable) that's related to this manifest and its children. This equal to the sum of [Content] size AND all the sizes in the [Size] struct present in the Kind-specific data struct. For example, for an image kind (Kind == \"image\") this would include the size of the image content and unpacked image snapshots ([Size.Content] + [ImageData.Size.Unpacked]).
    @SerialName(value = "Total") @Required val total: Long,
    // Content is the size (in bytes) of all the locally present content in the content store (e.g. image config, layers) referenced by this manifest and its children. This only includes blobs in the content store.
    @SerialName(value = "Content") @Required val content: Long,
)
