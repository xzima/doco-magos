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
 * Specification for the restart policy which applies to containers created as part of this service.
 *
 * @param condition Condition for restart.
 * @param delay Delay between restart attempts.
 * @param maxAttempts Maximum attempts to restart a given container before giving up (default value is 0, which is ignored).
 * @param window Windows is the time window used to evaluate the restart policy (default value is 0, which is unbounded).
 */
@Serializable
data class TaskSpecRestartPolicy(
    // Condition for restart.
    @SerialName(value = "Condition") val condition: Condition? = null,
    // Delay between restart attempts.
    @SerialName(value = "Delay") val delay: Long? = null,
    // Maximum attempts to restart a given container before giving up (default value is 0, which is ignored).
    @SerialName(value = "MaxAttempts") val maxAttempts: Long? = 0L,
    // Windows is the time window used to evaluate the restart policy (default value is 0, which is unbounded).
    @SerialName(value = "Window") val window: Long? = 0L,
) {

    /**
     * Condition for restart.
     *
     * Values: none,onMinusFailure,any
     */
    @Serializable
    enum class Condition(
        val value: String,
    ) {
        @SerialName(value = "none")
        NONE("none"),

        @SerialName(value = "on-failure")
        ON_MINUS_FAILURE("on-failure"),

        @SerialName(value = "any")
        ANY("any"),
    }
}
