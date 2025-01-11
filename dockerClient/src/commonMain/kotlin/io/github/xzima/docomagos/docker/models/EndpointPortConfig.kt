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
 *
 *
 * @param name
 * @param protocol
 * @param targetPort The port inside the container.
 * @param publishedPort The port on the swarm hosts.
 * @param publishMode The mode in which port is published.  <p><br /></p>  - \"ingress\" makes the target port accessible on every node,   regardless of whether there is a task for the service running on   that node or not. - \"host\" bypasses the routing mesh and publish the port directly on   the swarm node where that service is running.
 */
@Serializable
data class EndpointPortConfig(
    @SerialName(value = "Name") val name: String? = null,
    @SerialName(value = "Protocol") val protocol: Protocol? = null,
    // The port inside the container.
    @SerialName(value = "TargetPort") val targetPort: Int? = null,
    // The port on the swarm hosts.
    @SerialName(value = "PublishedPort") val publishedPort: Int? = null,
    // The mode in which port is published.  <p><br /></p>  - \"ingress\" makes the target port accessible on every node,   regardless of whether there is a task for the service running on   that node or not. - \"host\" bypasses the routing mesh and publish the port directly on   the swarm node where that service is running.
    @SerialName(value = "PublishMode") val publishMode: PublishMode? = PublishMode.INGRESS,
) {

    /**
     *
     *
     * Values: tcp,udp,sctp
     */
    @Serializable
    enum class Protocol(
        val value: String,
    ) {
        @SerialName(value = "tcp")
        TCP("tcp"),

        @SerialName(value = "udp")
        UDP("udp"),

        @SerialName(value = "sctp")
        SCTP("sctp"),
    }

    /**
     * The mode in which port is published.  <p><br /></p>  - \"ingress\" makes the target port accessible on every node,   regardless of whether there is a task for the service running on   that node or not. - \"host\" bypasses the routing mesh and publish the port directly on   the swarm node where that service is running.
     *
     * Values: ingress,host
     */
    @Serializable
    enum class PublishMode(
        val value: String,
    ) {
        @SerialName(value = "ingress")
        INGRESS("ingress"),

        @SerialName(value = "host")
        HOST("host"),
    }
}
