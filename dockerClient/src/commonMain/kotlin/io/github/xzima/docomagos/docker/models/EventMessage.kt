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
 * EventMessage represents the information an event contains.
 *
 * @param type The type of object emitting the event
 * @param action The type of event
 * @param actor
 * @param scope Scope of the event. Engine events are `local` scope. Cluster (Swarm) events are `swarm` scope.
 * @param time Timestamp of event
 * @param timeNano Timestamp of event, with nanosecond accuracy
 */
@Serializable
data class EventMessage(
    // The type of object emitting the event
    @SerialName(value = "Type") val type: Type? = null,
    // The type of event
    @SerialName(value = "Action") val action: String? = null,
    @SerialName(value = "Actor") val actor: EventActor? = null,
    // Scope of the event. Engine events are `local` scope. Cluster (Swarm) events are `swarm` scope.
    @SerialName(value = "scope") val scope: Scope? = null,
    // Timestamp of event
    @SerialName(value = "time") val time: Long? = null,
    // Timestamp of event, with nanosecond accuracy
    @SerialName(value = "timeNano") val timeNano: Long? = null,
) {

    /**
     * The type of object emitting the event
     *
     * Values: builder,config,container,daemon,image,network,node,plugin,secret,service,volume
     */
    @Serializable
    enum class Type(
        val value: String,
    ) {
        @SerialName(value = "builder")
        BUILDER("builder"),

        @SerialName(value = "config")
        CONFIG("config"),

        @SerialName(value = "container")
        CONTAINER("container"),

        @SerialName(value = "daemon")
        DAEMON("daemon"),

        @SerialName(value = "image")
        IMAGE("image"),

        @SerialName(value = "network")
        NETWORK("network"),

        @SerialName(value = "node")
        NODE("node"),

        @SerialName(value = "plugin")
        PLUGIN("plugin"),

        @SerialName(value = "secret")
        SECRET("secret"),

        @SerialName(value = "service")
        SERVICE("service"),

        @SerialName(value = "volume")
        VOLUME("volume"),
    }

    /**
     * Scope of the event. Engine events are `local` scope. Cluster (Swarm) events are `swarm` scope.
     *
     * Values: local,swarm
     */
    @Serializable
    enum class Scope(
        val value: String,
    ) {
        @SerialName(value = "local")
        LOCAL("local"),

        @SerialName(value = "swarm")
        SWARM("swarm"),
    }
}
