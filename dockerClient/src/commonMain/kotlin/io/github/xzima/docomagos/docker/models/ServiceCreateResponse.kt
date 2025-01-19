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
 * contains the information returned to a client on the creation of a new service.
 *
 * @param id The ID of the created service.
 * @param warnings Optional warning message.
 */
@Serializable
data class ServiceCreateResponse(
    // The ID of the created service.
    @SerialName(value = "ID") val id: String? = null,
    // Optional warning message.  FIXME(thaJeztah): this should have \"omitempty\" in the generated type.
    @SerialName(value = "Warnings") val warnings: List<String>? = null,
)
