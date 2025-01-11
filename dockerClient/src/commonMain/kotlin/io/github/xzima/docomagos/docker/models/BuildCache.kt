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
 * BuildCache contains information about a build cache record.
 *
 * @param ID Unique ID of the build cache record.
 * @param parent ID of the parent build cache record.  > **Deprecated**: This field is deprecated, and omitted if empty.
 * @param parents List of parent build cache record IDs.
 * @param type Cache record type.
 * @param description Description of the build-step that produced the build cache.
 * @param inUse Indicates if the build cache is in use.
 * @param shared Indicates if the build cache is shared.
 * @param propertySize Amount of disk space used by the build cache (in bytes).
 * @param createdAt Date and time at which the build cache was created in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
 * @param lastUsedAt Date and time at which the build cache was last used in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
 * @param usageCount
 */
@Serializable
data class BuildCache(
    // Unique ID of the build cache record.
    @SerialName(value = "ID") val ID: String? = null,
    // ID of the parent build cache record.  > **Deprecated**: This field is deprecated, and omitted if empty.
    @SerialName(value = "Parent") val parent: String? = null,
    // List of parent build cache record IDs.
    @SerialName(value = "Parents") val parents: List<String>? = null,
    // Cache record type.
    @SerialName(value = "Type") val type: Type? = null,
    // Description of the build-step that produced the build cache.
    @SerialName(value = "Description") val description: String? = null,
    // Indicates if the build cache is in use.
    @SerialName(value = "InUse") val inUse: Boolean? = null,
    // Indicates if the build cache is shared.
    @SerialName(value = "Shared") val shared: Boolean? = null,
    // Amount of disk space used by the build cache (in bytes).
    @SerialName(value = "Size") val propertySize: Int? = null,
    // Date and time at which the build cache was created in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
    @SerialName(value = "CreatedAt") val createdAt: String? = null,
    // Date and time at which the build cache was last used in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
    @SerialName(value = "LastUsedAt") val lastUsedAt: String? = null,
    @SerialName(value = "UsageCount") val usageCount: Int? = null,
) {

    /**
     * Cache record type.
     *
     * Values: `internal`,frontend,sourcePeriodLocal,sourcePeriodGitPeriodCheckout,execPeriodCachemount,regular
     */
    @Serializable
    enum class Type(
        val value: String,
    ) {
        @SerialName(value = "internal")
        INTERNAL("internal"),

        @SerialName(value = "frontend")
        FRONTEND("frontend"),

        @SerialName(value = "source.local")
        SOURCE_PERIOD_LOCAL("source.local"),

        @SerialName(value = "source.git.checkout")
        SOURCE_PERIOD_GIT_PERIOD_CHECKOUT("source.git.checkout"),

        @SerialName(value = "exec.cachemount")
        EXEC_PERIOD_CACHEMOUNT("exec.cachemount"),

        @SerialName(value = "regular")
        REGULAR("regular"),
    }
}
