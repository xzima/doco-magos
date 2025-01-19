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
 * Scheduling mode for the service.
 *
 * @param replicated
 * @param global
 * @param replicatedJob
 * @param globalJob The mode used for services which run a task to the completed state on each valid node.
 */
@Serializable
data class ServiceSpecMode(
    @SerialName(value = "Replicated") val replicated: ServiceSpecModeReplicated? = null,
    @SerialName(value = "Global") val global: String? = null,
    @SerialName(value = "ReplicatedJob") val replicatedJob: ServiceSpecModeReplicatedJob? = null,
    // The mode used for services which run a task to the completed state on each valid node.
    @SerialName(value = "GlobalJob") val globalJob: String? = null,
)
