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
 * Requirements for the accessible topology of the volume. These fields are optional. For an in-depth description of what these fields mean, see the CSI specification.
 *
 * @param requisite A list of required topologies, at least one of which the volume must be accessible from.
 * @param preferred A list of topologies that the volume should attempt to be provisioned in.
 */
@Serializable
data class ClusterVolumeSpecAccessModeAccessibilityRequirements(
    // A list of required topologies, at least one of which the volume must be accessible from.
    @SerialName(value = "Requisite") val requisite: List<Map<String, String>>? = null,
    // A list of topologies that the volume should attempt to be provisioned in.
    @SerialName(value = "Preferred") val preferred: List<Map<String, String>>? = null,
)
