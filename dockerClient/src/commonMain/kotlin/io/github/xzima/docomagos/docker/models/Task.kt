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
 * @param id The ID of the task.
 * @param version
 * @param createdAt
 * @param updatedAt
 * @param name Name of the task.
 * @param labels User-defined key/value metadata.
 * @param spec
 * @param serviceID The ID of the service this task is part of.
 * @param slot
 * @param nodeID The ID of the node that this task is on.
 * @param assignedGenericResources User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
 * @param status
 * @param desiredState
 * @param jobIteration
 */
@Serializable
data class Task(
    // The ID of the task.
    @SerialName(value = "ID") val id: String? = null,
    @SerialName(value = "Version") val version: ObjectVersion? = null,
    @SerialName(value = "CreatedAt") val createdAt: String? = null,
    @SerialName(value = "UpdatedAt") val updatedAt: String? = null,
    // Name of the task.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    @SerialName(value = "Spec") val spec: TaskSpec? = null,
    // The ID of the service this task is part of.
    @SerialName(value = "ServiceID") val serviceID: String? = null,
    @SerialName(value = "Slot") val slot: Int? = null,
    // The ID of the node that this task is on.
    @SerialName(value = "NodeID") val nodeID: String? = null,
    // User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
    @SerialName(value = "AssignedGenericResources") val assignedGenericResources: List<GenericResourcesInner>? = null,
    @SerialName(value = "Status") val status: TaskStatus? = null,
    @SerialName(value = "DesiredState") val desiredState: TaskState? = null,
    @SerialName(value = "JobIteration") val jobIteration: ObjectVersion? = null,
)
