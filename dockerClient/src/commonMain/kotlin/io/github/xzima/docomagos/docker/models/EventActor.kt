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
 * Actor describes something that generates events, like a container, network, or a volume.
 *
 * @param id The ID of the object emitting the event
 * @param attributes Various key/value attributes of the object, depending on its type.
 */
@Serializable
data class EventActor(
    // The ID of the object emitting the event
    @SerialName(value = "ID") val id: String? = null,
    // Various key/value attributes of the object, depending on its type.
    @SerialName(value = "Attributes") val attributes: Map<String, String>? = null,
)
