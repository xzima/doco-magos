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
 * Raft configuration.
 *
 * @param snapshotInterval The number of log entries between snapshots.
 * @param keepOldSnapshots The number of snapshots to keep beyond the current snapshot.
 * @param logEntriesForSlowFollowers The number of log entries to keep around to sync up slow followers after a snapshot is created.
 * @param electionTick The number of ticks that a follower will wait for a message from the leader before becoming a candidate and starting an election. `ElectionTick` must be greater than `HeartbeatTick`.  A tick currently defaults to one second, so these translate directly to seconds currently, but this is NOT guaranteed.
 * @param heartbeatTick The number of ticks between heartbeats. Every HeartbeatTick ticks, the leader will send a heartbeat to the followers.  A tick currently defaults to one second, so these translate directly to seconds currently, but this is NOT guaranteed.
 */
@Serializable
data class SwarmSpecRaft(
    // The number of log entries between snapshots.
    @SerialName(value = "SnapshotInterval") val snapshotInterval: Int? = null,
    // The number of snapshots to keep beyond the current snapshot.
    @SerialName(value = "KeepOldSnapshots") val keepOldSnapshots: Int? = null,
    // The number of log entries to keep around to sync up slow followers after a snapshot is created.
    @SerialName(value = "LogEntriesForSlowFollowers") val logEntriesForSlowFollowers: Int? = null,
    // The number of ticks that a follower will wait for a message from the leader before becoming a candidate and starting an election. `ElectionTick` must be greater than `HeartbeatTick`.  A tick currently defaults to one second, so these translate directly to seconds currently, but this is NOT guaranteed.
    @SerialName(value = "ElectionTick") val electionTick: Int? = null,
    // The number of ticks between heartbeats. Every HeartbeatTick ticks, the leader will send a heartbeat to the followers.  A tick currently defaults to one second, so these translate directly to seconds currently, but this is NOT guaranteed.
    @SerialName(value = "HeartbeatTick") val heartbeatTick: Int? = null,
)
