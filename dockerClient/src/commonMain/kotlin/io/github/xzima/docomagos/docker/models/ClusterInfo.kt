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
 * ClusterInfo represents information about the swarm as is returned by the \"/info\" endpoint. Join-tokens are not included.
 *
 * @param id The ID of the swarm.
 * @param version
 * @param createdAt Date and time at which the swarm was initialised in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
 * @param updatedAt Date and time at which the swarm was last updated in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
 * @param spec
 * @param tlSInfo
 * @param rootRotationInProgress Whether there is currently a root CA rotation in progress for the swarm
 * @param dataPathPort DataPathPort specifies the data path port number for data traffic. Acceptable port range is 1024 to 49151. If no port is set or is set to 0, the default port (4789) is used.
 * @param defaultAddrPool Default Address Pool specifies default subnet pools for global scope networks.
 * @param subnetSize SubnetSize specifies the subnet size of the networks created from the default subnet pool.
 */
@Serializable
data class ClusterInfo(
    // The ID of the swarm.
    @SerialName(value = "ID") val id: String? = null,
    @SerialName(value = "Version") val version: ObjectVersion? = null,
    // Date and time at which the swarm was initialised in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
    @SerialName(value = "CreatedAt") val createdAt: String? = null,
    // Date and time at which the swarm was last updated in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.
    @SerialName(value = "UpdatedAt") val updatedAt: String? = null,
    @SerialName(value = "Spec") val spec: SwarmSpec? = null,
    @SerialName(value = "TLSInfo") val tlSInfo: TLSInfo? = null,
    // Whether there is currently a root CA rotation in progress for the swarm
    @SerialName(value = "RootRotationInProgress") val rootRotationInProgress: Boolean? = null,
    // DataPathPort specifies the data path port number for data traffic. Acceptable port range is 1024 to 49151. If no port is set or is set to 0, the default port (4789) is used.
    @SerialName(value = "DataPathPort") val dataPathPort: Int? = null,
    // Default Address Pool specifies default subnet pools for global scope networks.
    @SerialName(value = "DefaultAddrPool") val defaultAddrPool: List<String>? = null,
    // SubnetSize specifies the subnet size of the networks created from the default subnet pool.
    @SerialName(value = "SubnetSize") val subnetSize: Int? = null,
)
