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
 * ManagerStatus represents the status of a manager.  It provides the current status of a node's manager component, if the node is a manager.
 *
 * @param leader
 * @param reachability
 * @param addr The IP address and port at which the manager is reachable.
 */
@Serializable
data class ManagerStatus(
    @SerialName(value = "Leader") val leader: Boolean? = false,
    @SerialName(value = "Reachability") val reachability: Reachability? = null,
    // The IP address and port at which the manager is reachable.
    @SerialName(value = "Addr") val addr: String? = null,
)
