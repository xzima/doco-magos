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
 * An object describing the resources which can be advertised by a node and requested by a task.
 *
 * @param nanoCPUs
 * @param memoryBytes
 * @param genericResources User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
 */
@Serializable
data class ResourceObject(
    @SerialName(value = "NanoCPUs") val nanoCPUs: Long? = null,
    @SerialName(value = "MemoryBytes") val memoryBytes: Long? = null,
    // User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
    @SerialName(value = "GenericResources") val genericResources: List<GenericResourcesInner>? = null,
)
