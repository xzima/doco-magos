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
 * The status of a service update.
 *
 * @param state
 * @param startedAt
 * @param completedAt
 * @param message
 */
@Serializable
data class ServiceUpdateStatus(
    @SerialName(value = "State") val state: State? = null,
    @SerialName(value = "StartedAt") val startedAt: String? = null,
    @SerialName(value = "CompletedAt") val completedAt: String? = null,
    @SerialName(value = "Message") val message: String? = null,
) {

    /**
     *
     *
     * Values: updating,paused,completed
     */
    @Serializable
    enum class State(
        val value: String,
    ) {
        @SerialName(value = "updating")
        UPDATING("updating"),

        @SerialName(value = "paused")
        PAUSED("paused"),

        @SerialName(value = "completed")
        COMPLETED("completed"),
    }
}
