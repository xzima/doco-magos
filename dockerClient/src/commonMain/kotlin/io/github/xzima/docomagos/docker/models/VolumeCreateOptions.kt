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
 * Volume configuration
 *
 * @param name The new volume's name. If not specified, Docker generates a name.
 * @param driver Name of the volume driver to use.
 * @param driverOpts A mapping of driver options and values. These options are passed directly to the driver and are driver specific.
 * @param labels User-defined key/value metadata.
 * @param clusterVolumeSpec
 */
@Serializable
data class VolumeCreateOptions(
    // The new volume's name. If not specified, Docker generates a name.
    @SerialName(value = "Name") val name: String? = null,
    // Name of the volume driver to use.
    @SerialName(value = "Driver") val driver: String? = "local",
    // A mapping of driver options and values. These options are passed directly to the driver and are driver specific.
    @SerialName(value = "DriverOpts") val driverOpts: Map<String, String>? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    @SerialName(value = "ClusterVolumeSpec") val clusterVolumeSpec: ClusterVolumeSpec? = null,
)
