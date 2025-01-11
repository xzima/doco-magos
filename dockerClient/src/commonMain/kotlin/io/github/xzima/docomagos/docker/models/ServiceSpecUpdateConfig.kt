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
 * Specification for the update strategy of the service.
 *
 * @param parallelism Maximum number of tasks to be updated in one iteration (0 means unlimited parallelism).
 * @param delay Amount of time between updates, in nanoseconds.
 * @param failureAction Action to take if an updated task fails to run, or stops running during the update.
 * @param monitor Amount of time to monitor each updated task for failures, in nanoseconds.
 * @param maxFailureRatio The fraction of tasks that may fail during an update before the failure action is invoked, specified as a floating point number between 0 and 1.
 * @param order The order of operations when rolling out an updated task. Either the old task is shut down before the new task is started, or the new task is started before the old task is shut down.
 */
@Serializable
data class ServiceSpecUpdateConfig(
    // Maximum number of tasks to be updated in one iteration (0 means unlimited parallelism).
    @SerialName(value = "Parallelism") val parallelism: Long? = null,
    // Amount of time between updates, in nanoseconds.
    @SerialName(value = "Delay") val delay: Long? = null,
    // Action to take if an updated task fails to run, or stops running during the update.
    @SerialName(value = "FailureAction") val failureAction: FailureAction? = null,
    // Amount of time to monitor each updated task for failures, in nanoseconds.
    @SerialName(value = "Monitor") val monitor: Long? = null,
    // The fraction of tasks that may fail during an update before the failure action is invoked, specified as a floating point number between 0 and 1.
    @SerialName(value = "MaxFailureRatio") val maxFailureRatio: Double? = null,
    // The order of operations when rolling out an updated task. Either the old task is shut down before the new task is started, or the new task is started before the old task is shut down.
    @SerialName(value = "Order") val order: Order? = null,
) {

    /**
     * Action to take if an updated task fails to run, or stops running during the update.
     *
     * Values: `continue`,pause,rollback
     */
    @Serializable
    enum class FailureAction(
        val value: String,
    ) {
        @SerialName(value = "continue")
        CONTINUE("continue"),

        @SerialName(value = "pause")
        PAUSE("pause"),

        @SerialName(value = "rollback")
        ROLLBACK("rollback"),
    }

    /**
     * The order of operations when rolling out an updated task. Either the old task is shut down before the new task is started, or the new task is started before the old task is shut down.
     *
     * Values: stopMinusFirst,startMinusFirst
     */
    @Serializable
    enum class Order(
        val value: String,
    ) {
        @SerialName(value = "stop-first")
        STOP_MINUS_FIRST("stop-first"),

        @SerialName(value = "start-first")
        START_MINUS_FIRST("start-first"),
    }
}
