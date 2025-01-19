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
 * @param name
 * @param description
 * @param settable
 * @param source
 * @param destination
 * @param type
 * @param options
 */
@Serializable
data class PluginMount(
    @SerialName(value = "Name") @Required val name: String,
    @SerialName(value = "Description") @Required val description: String,
    @SerialName(value = "Settable") @Required val settable: List<String>,
    @SerialName(value = "Source") @Required val source: String,
    @SerialName(value = "Destination") @Required val destination: String,
    @SerialName(value = "Type") @Required val type: String,
    @SerialName(value = "Options") @Required val options: List<String>,
)
