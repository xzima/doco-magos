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
 * individual image layer information in response to ImageHistory operation
 *
 * @param id
 * @param created
 * @param createdBy
 * @param tags
 * @param propertySize
 * @param comment
 */
@Serializable
data class HistoryResponseItem(
    @SerialName(value = "Id") @Required val id: String,
    @SerialName(value = "Created") @Required val created: Long,
    @SerialName(value = "CreatedBy") @Required val createdBy: String,
    @SerialName(value = "Tags") @Required val tags: List<String>,
    @SerialName(value = "Size") @Required val propertySize: Long,
    @SerialName(value = "Comment") @Required val comment: String,
)
