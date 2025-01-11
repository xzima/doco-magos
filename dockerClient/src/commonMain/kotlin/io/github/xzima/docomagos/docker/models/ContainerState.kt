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
 * ContainerState stores container's running state. It's part of ContainerJSONBase and will be returned by the \"inspect\" command.
 *
 * @param status String representation of the container state. Can be one of \"created\", \"running\", \"paused\", \"restarting\", \"removing\", \"exited\", or \"dead\".
 * @param running Whether this container is running.  Note that a running container can be _paused_. The `Running` and `Paused` booleans are not mutually exclusive:  When pausing a container (on Linux), the freezer cgroup is used to suspend all processes in the container. Freezing the process requires the process to be running. As a result, paused containers are both `Running` _and_ `Paused`.  Use the `Status` field instead to determine if a container's state is \"running\".
 * @param paused Whether this container is paused.
 * @param restarting Whether this container is restarting.
 * @param ooMKilled Whether a process within this container has been killed because it ran out of memory since the container was last started.
 * @param dead
 * @param pid The process ID of this container
 * @param exitCode The last exit code of this container
 * @param error
 * @param startedAt The time when this container was last started.
 * @param finishedAt The time when this container last exited.
 * @param health
 */
@Serializable
data class ContainerState(
    // String representation of the container state. Can be one of \"created\", \"running\", \"paused\", \"restarting\", \"removing\", \"exited\", or \"dead\".
    @SerialName(value = "Status") val status: Status? = null,
    // Whether this container is running.  Note that a running container can be _paused_. The `Running` and `Paused` booleans are not mutually exclusive:  When pausing a container (on Linux), the freezer cgroup is used to suspend all processes in the container. Freezing the process requires the process to be running. As a result, paused containers are both `Running` _and_ `Paused`.  Use the `Status` field instead to determine if a container's state is \"running\".
    @SerialName(value = "Running") val running: Boolean? = null,
    // Whether this container is paused.
    @SerialName(value = "Paused") val paused: Boolean? = null,
    // Whether this container is restarting.
    @SerialName(value = "Restarting") val restarting: Boolean? = null,
    // Whether a process within this container has been killed because it ran out of memory since the container was last started.
    @SerialName(value = "OOMKilled") val ooMKilled: Boolean? = null,
    @SerialName(value = "Dead") val dead: Boolean? = null,
    // The process ID of this container
    @SerialName(value = "Pid") val pid: Int? = null,
    // The last exit code of this container
    @SerialName(value = "ExitCode") val exitCode: Int? = null,
    @SerialName(value = "Error") val error: String? = null,
    // The time when this container was last started.
    @SerialName(value = "StartedAt") val startedAt: String? = null,
    // The time when this container last exited.
    @SerialName(value = "FinishedAt") val finishedAt: String? = null,
    @SerialName(value = "Health") val health: Health? = null,
) {

    /**
     * String representation of the container state. Can be one of \"created\", \"running\", \"paused\", \"restarting\", \"removing\", \"exited\", or \"dead\".
     *
     * Values: created,running,paused,restarting,removing,exited,dead
     */
    @Serializable
    enum class Status(
        val value: String,
    ) {
        @SerialName(value = "created")
        CREATED("created"),

        @SerialName(value = "running")
        RUNNING("running"),

        @SerialName(value = "paused")
        PAUSED("paused"),

        @SerialName(value = "restarting")
        RESTARTING("restarting"),

        @SerialName(value = "removing")
        REMOVING("removing"),

        @SerialName(value = "exited")
        EXITED("exited"),

        @SerialName(value = "dead")
        DEAD("dead"),
    }
}
