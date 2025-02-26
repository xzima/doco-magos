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
 * OK response to ContainerCreate operation
 *
 * @param id The ID of the created container
 * @param warnings Warnings encountered when creating the container
 */
@Serializable
data class ContainerCreateResponse(
    // The ID of the created container
    @SerialName(value = "Id") @Required val id: String,
    // Warnings encountered when creating the container
    @SerialName(value = "Warnings") @Required val warnings: List<String>,
)
