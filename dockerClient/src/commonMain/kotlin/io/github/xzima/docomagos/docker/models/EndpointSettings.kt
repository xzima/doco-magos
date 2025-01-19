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
 * Configuration for a network endpoint.
 *
 * @param ipAMConfig
 * @param links
 * @param macAddress MAC address for the endpoint on this network. The network driver might ignore this parameter.
 * @param aliases
 * @param driverOpts DriverOpts is a mapping of driver options and values. These options are passed directly to the driver and are driver specific.
 * @param networkID Unique ID of the network.
 * @param endpointID Unique ID for the service endpoint in a Sandbox.
 * @param gateway Gateway address for this network.
 * @param ipAddress IPv4 address.
 * @param ipPrefixLen Mask length of the IPv4 address.
 * @param ipv6Gateway IPv6 gateway address.
 * @param globalIPv6Address Global IPv6 address.
 * @param globalIPv6PrefixLen Mask length of the global IPv6 address.
 * @param dnSNames List of all DNS names an endpoint has on a specific network. This list is based on the container name, network aliases, container short ID, and hostname.  These DNS names are non-fully qualified but can contain several dots. You can get fully qualified DNS names by appending `.<network-name>`. For instance, if container name is `my.ctr` and the network is named `testnet`, `DNSNames` will contain `my.ctr` and the FQDN will be `my.ctr.testnet`.
 */
@Serializable
data class EndpointSettings(
    @SerialName(value = "IPAMConfig") val ipAMConfig: EndpointIPAMConfig? = null,
    @SerialName(value = "Links") val links: List<String>? = null,
    // MAC address for the endpoint on this network. The network driver might ignore this parameter.
    @SerialName(value = "MacAddress") val macAddress: String? = null,
    @SerialName(value = "Aliases") val aliases: List<String>? = null,
    // DriverOpts is a mapping of driver options and values. These options are passed directly to the driver and are driver specific.
    @SerialName(value = "DriverOpts") val driverOpts: Map<String, String>? = null,
    // Unique ID of the network.
    @SerialName(value = "NetworkID") val networkID: String? = null,
    // Unique ID for the service endpoint in a Sandbox.
    @SerialName(value = "EndpointID") val endpointID: String? = null,
    // Gateway address for this network.
    @SerialName(value = "Gateway") val gateway: String? = null,
    // IPv4 address.
    @SerialName(value = "IPAddress") val ipAddress: String? = null,
    // Mask length of the IPv4 address.
    @SerialName(value = "IPPrefixLen") val ipPrefixLen: Int? = null,
    // IPv6 gateway address.
    @SerialName(value = "IPv6Gateway") val ipv6Gateway: String? = null,
    // Global IPv6 address.
    @SerialName(value = "GlobalIPv6Address") val globalIPv6Address: String? = null,
    // Mask length of the global IPv6 address.
    @SerialName(value = "GlobalIPv6PrefixLen") val globalIPv6PrefixLen: Long? = null,
    // List of all DNS names an endpoint has on a specific network. This list is based on the container name, network aliases, container short ID, and hostname.  These DNS names are non-fully qualified but can contain several dots. You can get fully qualified DNS names by appending `.<network-name>`. For instance, if container name is `my.ctr` and the network is named `testnet`, `DNSNames` will contain `my.ctr` and the FQDN will be `my.ctr.testnet`.
    @SerialName(value = "DNSNames") val dnSNames: List<String>? = null,
)
