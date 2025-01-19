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
 * Represents generic information about swarm.
 *
 * @param nodeID Unique identifier of for this node in the swarm.
 * @param nodeAddr IP address at which this node can be reached by other nodes in the swarm.
 * @param localNodeState
 * @param controlAvailable
 * @param error
 * @param remoteManagers List of ID's and addresses of other managers in the swarm.
 * @param nodes Total number of nodes in the swarm.
 * @param managers Total number of managers in the swarm.
 * @param cluster
 */
@Serializable
data class SwarmInfo(
    // Unique identifier of for this node in the swarm.
    @SerialName(value = "NodeID") val nodeID: String? = "",
    // IP address at which this node can be reached by other nodes in the swarm.
    @SerialName(value = "NodeAddr") val nodeAddr: String? = "",
    @SerialName(value = "LocalNodeState") val localNodeState: LocalNodeState? = LocalNodeState.UNKNOWN,
    @SerialName(value = "ControlAvailable") val controlAvailable: Boolean? = false,
    @SerialName(value = "Error") val error: String? = "",
    // List of ID's and addresses of other managers in the swarm.
    @SerialName(value = "RemoteManagers") val remoteManagers: List<PeerNode>? = null,
    // Total number of nodes in the swarm.
    @SerialName(value = "Nodes") val nodes: Int? = null,
    // Total number of managers in the swarm.
    @SerialName(value = "Managers") val managers: Int? = null,
    @SerialName(value = "Cluster") val cluster: ClusterInfo? = null,
)
