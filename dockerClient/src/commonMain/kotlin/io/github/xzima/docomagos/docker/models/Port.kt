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
 * An open port on a container
 *
 * @param privatePort Port on the container
 * @param type
 * @param ip Host IP address that the container's port is mapped to
 * @param publicPort Port exposed on the host
 */
@Serializable
data class Port(
    // Port on the container
    @SerialName(value = "PrivatePort") @Required val privatePort: Int,
    @SerialName(value = "Type") @Required val type: Type,
    // Host IP address that the container's port is mapped to
    @SerialName(value = "IP") val ip: String? = null,
    // Port exposed on the host
    @SerialName(value = "PublicPort") val publicPort: Int? = null,
) {

    /**
     *
     *
     * Values: tcp,udp,sctp
     */
    @Serializable
    enum class Type(
        val value: String,
    ) {
        @SerialName(value = "tcp")
        TCP("tcp"),

        @SerialName(value = "udp")
        UDP("udp"),

        @SerialName(value = "sctp")
        SCTP("sctp"),
    }
}
