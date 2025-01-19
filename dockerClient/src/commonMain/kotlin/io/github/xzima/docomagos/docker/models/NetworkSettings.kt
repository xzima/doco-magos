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
 * NetworkSettings exposes the network settings in the API
 *
 * @param bridge Name of the default bridge interface when dockerd's --bridge flag is set.
 * @param sandboxID SandboxID uniquely represents a container's network stack.
 * @param hairpinMode Indicates if hairpin NAT should be enabled on the virtual interface.  Deprecated: This field is never set and will be removed in a future release.
 * @param linkLocalIPv6Address IPv6 unicast address using the link-local prefix.  Deprecated: This field is never set and will be removed in a future release.
 * @param linkLocalIPv6PrefixLen Prefix length of the IPv6 unicast address.  Deprecated: This field is never set and will be removed in a future release.
 * @param ports PortMap describes the mapping of container ports to host ports, using the container's port-number and protocol as key in the format `<port>/<protocol>`, for example, `80/udp`.  If a container's port is mapped for multiple protocols, separate entries are added to the mapping table.
 * @param sandboxKey SandboxKey is the full path of the netns handle
 * @param secondaryIPAddresses Deprecated: This field is never set and will be removed in a future release.
 * @param secondaryIPv6Addresses Deprecated: This field is never set and will be removed in a future release.
 * @param endpointID EndpointID uniquely represents a service endpoint in a Sandbox.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param gateway Gateway address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param globalIPv6Address Global IPv6 address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param globalIPv6PrefixLen Mask length of the global IPv6 address.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param ipAddress IPv4 address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param ipPrefixLen Mask length of the IPv4 address.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param ipv6Gateway IPv6 gateway address for this network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param macAddress MAC address for the container on the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
 * @param networks Information about all networks that the container is connected to.
 */
@Serializable
data class NetworkSettings(
    // Name of the default bridge interface when dockerd's --bridge flag is set.
    @SerialName(value = "Bridge") val bridge: String? = null,
    // SandboxID uniquely represents a container's network stack.
    @SerialName(value = "SandboxID") val sandboxID: String? = null,
    // Indicates if hairpin NAT should be enabled on the virtual interface.  Deprecated: This field is never set and will be removed in a future release.
    @SerialName(value = "HairpinMode") val hairpinMode: Boolean? = null,
    // IPv6 unicast address using the link-local prefix.  Deprecated: This field is never set and will be removed in a future release.
    @SerialName(value = "LinkLocalIPv6Address") val linkLocalIPv6Address: String? = null,
    // Prefix length of the IPv6 unicast address.  Deprecated: This field is never set and will be removed in a future release.
    @SerialName(value = "LinkLocalIPv6PrefixLen") val linkLocalIPv6PrefixLen: Int? = null,
    // PortMap describes the mapping of container ports to host ports, using the container's port-number and protocol as key in the format `<port>/<protocol>`, for example, `80/udp`.  If a container's port is mapped for multiple protocols, separate entries are added to the mapping table.
    @SerialName(value = "Ports") val ports: Map<String, List<PortBinding>>? = null,
    // SandboxKey is the full path of the netns handle
    @SerialName(value = "SandboxKey") val sandboxKey: String? = null,
    // Deprecated: This field is never set and will be removed in a future release.
    @SerialName(value = "SecondaryIPAddresses") val secondaryIPAddresses: List<Address>? = null,
    // Deprecated: This field is never set and will be removed in a future release.
    @SerialName(value = "SecondaryIPv6Addresses") val secondaryIPv6Addresses: List<Address>? = null,
    // EndpointID uniquely represents a service endpoint in a Sandbox.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "EndpointID") val endpointID: String? = null,
    // Gateway address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "Gateway") val gateway: String? = null,
    // Global IPv6 address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "GlobalIPv6Address") val globalIPv6Address: String? = null,
    // Mask length of the global IPv6 address.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "GlobalIPv6PrefixLen") val globalIPv6PrefixLen: Int? = null,
    // IPv4 address for the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "IPAddress") val ipAddress: String? = null,
    // Mask length of the IPv4 address.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "IPPrefixLen") val ipPrefixLen: Int? = null,
    // IPv6 gateway address for this network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "IPv6Gateway") val ipv6Gateway: String? = null,
    // MAC address for the container on the default \"bridge\" network.  <p><br /></p>  > **Deprecated**: This field is only propagated when attached to the > default \"bridge\" network. Use the information from the \"bridge\" > network inside the `Networks` map instead, which contains the same > information. This field was deprecated in Docker 1.9 and is scheduled > to be removed in Docker 17.12.0
    @SerialName(value = "MacAddress") val macAddress: String? = null,
    // Information about all networks that the container is connected to.
    @SerialName(value = "Networks") val networks: Map<String, EndpointSettings>? = null,
)
