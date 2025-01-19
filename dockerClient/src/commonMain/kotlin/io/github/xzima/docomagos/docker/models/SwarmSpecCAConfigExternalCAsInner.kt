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
 * @param protocol Protocol for communication with the external CA (currently only `cfssl` is supported).
 * @param url URL where certificate signing requests should be sent.
 * @param options An object with key/value pairs that are interpreted as protocol-specific options for the external CA driver.
 * @param caCert The root CA certificate (in PEM format) this external CA uses to issue TLS certificates (assumed to be to the current swarm root CA certificate if not provided).
 */
@Serializable
data class SwarmSpecCAConfigExternalCAsInner(
    // Protocol for communication with the external CA (currently only `cfssl` is supported).
    @SerialName(value = "Protocol") val protocol: Protocol? = Protocol.CFSSL,
    // URL where certificate signing requests should be sent.
    @SerialName(value = "URL") val url: String? = null,
    // An object with key/value pairs that are interpreted as protocol-specific options for the external CA driver.
    @SerialName(value = "Options") val options: Map<String, String>? = null,
    // The root CA certificate (in PEM format) this external CA uses to issue TLS certificates (assumed to be to the current swarm root CA certificate if not provided).
    @SerialName(value = "CACert") val caCert: String? = null,
) {

    /**
     * Protocol for communication with the external CA (currently only `cfssl` is supported).
     *
     * Values: CFSSL
     */
    @Serializable
    enum class Protocol(
        val value: String,
    ) {
        @SerialName(value = "cfssl")
        CFSSL("cfssl"),
    }
}
