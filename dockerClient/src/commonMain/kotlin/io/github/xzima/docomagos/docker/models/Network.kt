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
 * @param name Name of the network.
 * @param id ID that uniquely identifies a network on a single machine.
 * @param created Date and time at which the network was created in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
 * @param scope The level at which the network exists (e.g. `swarm` for cluster-wide or `local` for machine level)
 * @param driver The name of the driver used to create the network (e.g. `bridge`, `overlay`).
 * @param enableIPv4 Whether the network was created with IPv4 enabled.
 * @param enableIPv6 Whether the network was created with IPv6 enabled.
 * @param ipam
 * @param `internal` Whether the network is created to only allow internal networking connectivity.
 * @param attachable Whether a global / swarm scope network is manually attachable by regular containers from workers in swarm mode.
 * @param ingress Whether the network is providing the routing-mesh for the swarm cluster.
 * @param configFrom
 * @param configOnly Whether the network is a config-only network. Config-only networks are placeholder networks for network configurations to be used by other networks. Config-only networks cannot be used directly to run containers or services.
 * @param containers Contains endpoints attached to the network.
 * @param options Network-specific options uses when creating the network.
 * @param labels User-defined key/value metadata.
 * @param peers List of peer nodes for an overlay network. This field is only present for overlay networks, and omitted for other network types.
 */
@Serializable
data class Network(
    // Name of the network.
    @SerialName(value = "Name") val name: String? = null,
    // ID that uniquely identifies a network on a single machine.
    @SerialName(value = "Id") val id: String? = null,
    // Date and time at which the network was created in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
    @SerialName(value = "Created") val created: String? = null,
    // The level at which the network exists (e.g. `swarm` for cluster-wide or `local` for machine level)
    @SerialName(value = "Scope") val scope: String? = null,
    // The name of the driver used to create the network (e.g. `bridge`, `overlay`).
    @SerialName(value = "Driver") val driver: String? = null,
    // Whether the network was created with IPv4 enabled.
    @SerialName(value = "EnableIPv4") val enableIPv4: Boolean? = null,
    // Whether the network was created with IPv6 enabled.
    @SerialName(value = "EnableIPv6") val enableIPv6: Boolean? = null,
    @SerialName(value = "IPAM") val ipam: IPAM? = null,
    // Whether the network is created to only allow internal networking connectivity.
    @SerialName(value = "Internal") val `internal`: Boolean? = false,
    // Whether a global / swarm scope network is manually attachable by regular containers from workers in swarm mode.
    @SerialName(value = "Attachable") val attachable: Boolean? = false,
    // Whether the network is providing the routing-mesh for the swarm cluster.
    @SerialName(value = "Ingress") val ingress: Boolean? = false,
    @SerialName(value = "ConfigFrom") val configFrom: ConfigReference? = null,
    // Whether the network is a config-only network. Config-only networks are placeholder networks for network configurations to be used by other networks. Config-only networks cannot be used directly to run containers or services.
    @SerialName(value = "ConfigOnly") val configOnly: Boolean? = false,
    // Contains endpoints attached to the network.
    @SerialName(value = "Containers") val containers: Map<String, NetworkContainer>? = null,
    // Network-specific options uses when creating the network.
    @SerialName(value = "Options") val options: Map<String, String>? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // List of peer nodes for an overlay network. This field is only present for overlay networks, and omitted for other network types.
    @SerialName(value = "Peers") val peers: List<PeerInfo>? = null,
)
