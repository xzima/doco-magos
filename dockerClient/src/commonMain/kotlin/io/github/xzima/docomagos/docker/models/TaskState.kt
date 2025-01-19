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

import io.github.xzima.docomagos.docker.models.TaskState.entries
import kotlinx.serialization.*

/**
 *
 *
 * Values: new,allocated,pending,assigned,accepted,preparing,ready,starting,running,complete,shutdown,failed,rejected,remove,orphaned
 */
@Serializable
enum class TaskState(
    val value: String,
) {

    @SerialName(value = "new")
    NEW("new"),

    @SerialName(value = "allocated")
    ALLOCATED("allocated"),

    @SerialName(value = "pending")
    PENDING("pending"),

    @SerialName(value = "assigned")
    ASSIGNED("assigned"),

    @SerialName(value = "accepted")
    ACCEPTED("accepted"),

    @SerialName(value = "preparing")
    PREPARING("preparing"),

    @SerialName(value = "ready")
    READY("ready"),

    @SerialName(value = "starting")
    STARTING("starting"),

    @SerialName(value = "running")
    RUNNING("running"),

    @SerialName(value = "complete")
    COMPLETE("complete"),

    @SerialName(value = "shutdown")
    SHUTDOWN("shutdown"),

    @SerialName(value = "failed")
    FAILED("failed"),

    @SerialName(value = "rejected")
    REJECTED("rejected"),

    @SerialName(value = "remove")
    REMOVE("remove"),

    @SerialName(value = "orphaned")
    ORPHANED("orphaned"),
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
        fun encode(data: Any?): String? = if (data is TaskState) "$data" else null

        /**
         * Returns a valid [TaskState] for [data], null otherwise.
         */
        fun decode(data: Any?): TaskState? = data?.let {
            val normalizedData = "$it".lowercase()
            entries.firstOrNull { value ->
                it == value || normalizedData == "$value".lowercase()
            }
        }
    }
}
