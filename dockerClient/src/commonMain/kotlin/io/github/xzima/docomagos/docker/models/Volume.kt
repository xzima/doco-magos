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
 * @param name Name of the volume.
 * @param driver Name of the volume driver used by the volume.
 * @param mountpoint Mount path of the volume on the host.
 * @param labels User-defined key/value metadata.
 * @param scope The level at which the volume exists. Either `global` for cluster-wide, or `local` for machine level.
 * @param options The driver specific options used when creating the volume.
 * @param createdAt Date/Time the volume was created.
 * @param status Low-level details about the volume, provided by the volume driver. Details are returned as a map with key/value pairs: `{\"key\":\"value\",\"key2\":\"value2\"}`.  The `Status` field is optional, and is omitted if the volume driver does not support this feature.
 * @param clusterVolume
 * @param usageData
 */
@Serializable
data class Volume(
    // Name of the volume.
    @SerialName(value = "Name") @Required val name: String,
    // Name of the volume driver used by the volume.
    @SerialName(value = "Driver") @Required val driver: String,
    // Mount path of the volume on the host.
    @SerialName(value = "Mountpoint") @Required val mountpoint: String,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") @Required val labels: Map<String, String>,
    // The level at which the volume exists. Either `global` for cluster-wide, or `local` for machine level.
    @SerialName(value = "Scope") @Required val scope: Scope = Scope.LOCAL,
    // The driver specific options used when creating the volume.
    @SerialName(value = "Options") @Required val options: Map<String, String>,
    // Date/Time the volume was created.
    @SerialName(value = "CreatedAt") val createdAt: String? = null,
    // Low-level details about the volume, provided by the volume driver. Details are returned as a map with key/value pairs: `{\"key\":\"value\",\"key2\":\"value2\"}`.  The `Status` field is optional, and is omitted if the volume driver does not support this feature.
    @SerialName(value = "Status") val status: Map<String, String>? = null,
    @SerialName(value = "ClusterVolume") val clusterVolume: ClusterVolume? = null,
    @SerialName(value = "UsageData") val usageData: VolumeUsageData? = null,
) {

    /**
     * The level at which the volume exists. Either `global` for cluster-wide, or `local` for machine level.
     *
     * Values: local,global
     */
    @Serializable
    enum class Scope(
        val value: String,
    ) {
        @SerialName(value = "local")
        LOCAL("local"),

        @SerialName(value = "global")
        GLOBAL("global"),
    }
}
