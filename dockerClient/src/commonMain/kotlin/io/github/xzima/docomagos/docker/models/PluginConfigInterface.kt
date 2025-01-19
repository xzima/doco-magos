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
 * The interface between Docker and the plugin
 *
 * @param types
 * @param socket
 * @param protocolScheme Protocol to use for clients connecting to the plugin.
 */
@Serializable
data class PluginConfigInterface(
    @SerialName(value = "Types") @Required val types: List<PluginInterfaceType>,
    @SerialName(value = "Socket") @Required val socket: String,
    // Protocol to use for clients connecting to the plugin.
    @SerialName(value = "ProtocolScheme") val protocolScheme: ProtocolScheme? = null,
) {

    /**
     * Protocol to use for clients connecting to the plugin.
     *
     * Values: ,mobyPeriodPluginsPeriodHttpSlashV1
     */
    @Serializable
    enum class ProtocolScheme(
        val value: String,
    ) {
        @SerialName(value = "")
        UNKNOWN(""),

        @SerialName(value = "moby.plugins.http/v1")
        MOBY_PERIOD_PLUGINS_PERIOD_HTTP_SLASH_V_1("moby.plugins.http/v1"),
    }
}
