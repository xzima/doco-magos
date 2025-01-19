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
 * @param layersSize
 * @param images
 * @param containers
 * @param volumes
 * @param buildCache
 */
@Serializable
data class SystemDataUsageResponse(
    @SerialName(value = "LayersSize") val layersSize: Long? = null,
    @SerialName(value = "Images") val images: List<ImageSummary>? = null,
    @SerialName(value = "Containers") val containers: List<ContainerSummary>? = null,
    @SerialName(value = "Volumes") val volumes: List<Volume>? = null,
    @SerialName(value = "BuildCache") val buildCache: List<BuildCache>? = null,
)
