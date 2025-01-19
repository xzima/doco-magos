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
 * The desired capacity that the volume should be created with. If empty, the plugin will decide the capacity.
 *
 * @param requiredBytes The volume must be at least this big. The value of 0 indicates an unspecified minimum
 * @param limitBytes The volume must not be bigger than this. The value of 0 indicates an unspecified maximum.
 */
@Serializable
data class ClusterVolumeSpecAccessModeCapacityRange(
    // The volume must be at least this big. The value of 0 indicates an unspecified minimum
    @SerialName(value = "RequiredBytes") val requiredBytes: Long? = null,
    // The volume must not be bigger than this. The value of 0 indicates an unspecified maximum.
    @SerialName(value = "LimitBytes") val limitBytes: Long? = null,
)
