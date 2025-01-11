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
 * NodeDescription encapsulates the properties of the Node as reported by the agent.
 *
 * @param hostname
 * @param platform
 * @param resources
 * @param engine
 * @param tlSInfo
 */
@Serializable
data class NodeDescription(
    @SerialName(value = "Hostname") val hostname: String? = null,
    @SerialName(value = "Platform") val platform: Platform? = null,
    @SerialName(value = "Resources") val resources: ResourceObject? = null,
    @SerialName(value = "Engine") val engine: EngineDescription? = null,
    @SerialName(value = "TLSInfo") val tlSInfo: TLSInfo? = null,
)
