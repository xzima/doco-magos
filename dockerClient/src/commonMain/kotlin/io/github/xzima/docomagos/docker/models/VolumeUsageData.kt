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
 * Usage details about the volume. This information is used by the `GET /system/df` endpoint, and omitted in other endpoints.
 *
 * @param propertySize Amount of disk space used by the volume (in bytes). This information is only available for volumes created with the `\"local\"` volume driver. For volumes created with other volume drivers, this field is set to `-1` (\"not available\")
 * @param refCount The number of containers referencing this volume. This field is set to `-1` if the reference-count is not available.
 */
@Serializable
data class VolumeUsageData(
    // Amount of disk space used by the volume (in bytes). This information is only available for volumes created with the `\"local\"` volume driver. For volumes created with other volume drivers, this field is set to `-1` (\"not available\")
    @SerialName(value = "Size") @Required val propertySize: Long = -1L,
    // The number of containers referencing this volume. This field is set to `-1` if the reference-count is not available.
    @SerialName(value = "RefCount") @Required val refCount: Long = -1L,
)
