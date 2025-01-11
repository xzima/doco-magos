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

import io.github.xzima.docomagos.docker.models.Reachability.entries
import kotlinx.serialization.*

/**
 * Reachability represents the reachability of a node.
 *
 * Values: unknown,unreachable,reachable
 */
@Serializable
enum class Reachability(
    val value: String,
) {

    @SerialName(value = "unknown")
    UNKNOWN("unknown"),

    @SerialName(value = "unreachable")
    UNREACHABLE("unreachable"),

    @SerialName(value = "reachable")
    REACHABLE("reachable"),
    ;

    /**
     * Override [toString()] to avoid using the enum variable name as the value, and instead use
     * the actual value defined in the API spec file.
     *
     * This solves a problem when the variable name and its value are different, and ensures that
     * the client sends the correct enum values to the server always.
     */
    override fun toString(): String = value

    companion object {
        /**
         * Converts the provided [data] to a [String] on success, null otherwise.
         */
        fun encode(data: Any?): String? = if (data is Reachability) "$data" else null

        /**
         * Returns a valid [Reachability] for [data], null otherwise.
         */
        fun decode(data: Any?): Reachability? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
