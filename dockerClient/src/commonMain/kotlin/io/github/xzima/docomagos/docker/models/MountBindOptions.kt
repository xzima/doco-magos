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
 * Optional configuration for the `bind` type.
 *
 * @param propagation A propagation mode with the value `(r)private`, `(r)shared`, or `(r)slave`.
 * @param nonRecursive Disable recursive bind mount.
 * @param createMountpoint Create mount point on host if missing
 * @param readOnlyNonRecursive Make the mount non-recursively read-only, but still leave the mount recursive (unless NonRecursive is set to `true` in conjunction).  Added in v1.44, before that version all read-only mounts were non-recursive by default. To match the previous behaviour this will default to `true` for clients on versions prior to v1.44.
 * @param readOnlyForceRecursive Raise an error if the mount cannot be made recursively read-only.
 */
@Serializable
data class MountBindOptions(
    // A propagation mode with the value `(r)private`, `(r)shared`, or `(r)slave`.
    @SerialName(value = "Propagation") val propagation: Propagation? = null,
    // Disable recursive bind mount.
    @SerialName(value = "NonRecursive") val nonRecursive: Boolean? = false,
    // Create mount point on host if missing
    @SerialName(value = "CreateMountpoint") val createMountpoint: Boolean? = false,
    // Make the mount non-recursively read-only, but still leave the mount recursive (unless NonRecursive is set to `true` in conjunction).  Added in v1.44, before that version all read-only mounts were non-recursive by default. To match the previous behaviour this will default to `true` for clients on versions prior to v1.44.
    @SerialName(value = "ReadOnlyNonRecursive") val readOnlyNonRecursive: Boolean? = false,
    // Raise an error if the mount cannot be made recursively read-only.
    @SerialName(value = "ReadOnlyForceRecursive") val readOnlyForceRecursive: Boolean? = false,
) {

    /**
     * A propagation mode with the value `(r)private`, `(r)shared`, or `(r)slave`.
     *
     * Values: PRIVATE,R_PRIVATE,SHARED,R_SHARED,SLAVE,R_SLAVE
     */
    @Serializable
    enum class Propagation(
        val value: String,
    ) {
        @SerialName(value = "private")
        PRIVATE("private"),

        @SerialName(value = "rprivate")
        R_PRIVATE("rprivate"),

        @SerialName(value = "shared")
        SHARED("shared"),

        @SerialName(value = "rshared")
        R_SHARED("rshared"),

        @SerialName(value = "slave")
        SLAVE("slave"),

        @SerialName(value = "rslave")
        R_SLAVE("rslave"),
    }
}
