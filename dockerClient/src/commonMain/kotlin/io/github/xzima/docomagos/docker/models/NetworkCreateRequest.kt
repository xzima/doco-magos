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
 * @param name The network's name.
 * @param driver Name of the network driver plugin to use.
 * @param scope The level at which the network exists (e.g. `swarm` for cluster-wide or `local` for machine level).
 * @param `internal` Restrict external access to the network.
 * @param attachable Globally scoped network is manually attachable by regular containers from workers in swarm mode.
 * @param ingress Ingress network is the network which provides the routing-mesh in swarm mode.
 * @param configOnly Creates a config-only network. Config-only networks are placeholder networks for network configurations to be used by other networks. Config-only networks cannot be used directly to run containers or services.
 * @param configFrom
 * @param ipam
 * @param enableIPv4 Enable IPv4 on the network. To disable IPv4, the daemon must be started with experimental features enabled.
 * @param enableIPv6 Enable IPv6 on the network.
 * @param options Network specific options to be used by the drivers.
 * @param labels User-defined key/value metadata.
 */
@Serializable
data class NetworkCreateRequest(
    // The network's name.
    @SerialName(value = "Name") @Required val name: String,
    // Name of the network driver plugin to use.
    @SerialName(value = "Driver") val driver: String? = "bridge",
    // The level at which the network exists (e.g. `swarm` for cluster-wide or `local` for machine level).
    @SerialName(value = "Scope") val scope: String? = null,
    // Restrict external access to the network.
    @SerialName(value = "Internal") val `internal`: Boolean? = null,
    // Globally scoped network is manually attachable by regular containers from workers in swarm mode.
    @SerialName(value = "Attachable") val attachable: Boolean? = null,
    // Ingress network is the network which provides the routing-mesh in swarm mode.
    @SerialName(value = "Ingress") val ingress: Boolean? = null,
    // Creates a config-only network. Config-only networks are placeholder networks for network configurations to be used by other networks. Config-only networks cannot be used directly to run containers or services.
    @SerialName(value = "ConfigOnly") val configOnly: Boolean? = false,
    @SerialName(value = "ConfigFrom") val configFrom: ConfigReference? = null,
    @SerialName(value = "IPAM") val ipam: IPAM? = null,
    // Enable IPv4 on the network. To disable IPv4, the daemon must be started with experimental features enabled.
    @SerialName(value = "EnableIPv4") val enableIPv4: Boolean? = null,
    // Enable IPv6 on the network.
    @SerialName(value = "EnableIPv6") val enableIPv6: Boolean? = null,
    // Network specific options to be used by the drivers.
    @SerialName(value = "Options") val options: Map<String, String>? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
)
