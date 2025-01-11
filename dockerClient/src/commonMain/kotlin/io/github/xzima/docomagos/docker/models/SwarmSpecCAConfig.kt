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
 * CA configuration.
 *
 * @param nodeCertExpiry The duration node certificates are issued for.
 * @param externalCAs Configuration for forwarding signing requests to an external certificate authority.
 * @param signingCACert The desired signing CA certificate for all swarm node TLS leaf certificates, in PEM format.
 * @param signingCAKey The desired signing CA key for all swarm node TLS leaf certificates, in PEM format.
 * @param forceRotate An integer whose purpose is to force swarm to generate a new signing CA certificate and key, if none have been specified in `SigningCACert` and `SigningCAKey`
 */
@Serializable
data class SwarmSpecCAConfig(
    // The duration node certificates are issued for.
    @SerialName(value = "NodeCertExpiry") val nodeCertExpiry: Long? = null,
    // Configuration for forwarding signing requests to an external certificate authority.
    @SerialName(value = "ExternalCAs") val externalCAs: List<SwarmSpecCAConfigExternalCAsInner>? = null,
    // The desired signing CA certificate for all swarm node TLS leaf certificates, in PEM format.
    @SerialName(value = "SigningCACert") val signingCACert: String? = null,
    // The desired signing CA key for all swarm node TLS leaf certificates, in PEM format.
    @SerialName(value = "SigningCAKey") val signingCAKey: String? = null,
    // An integer whose purpose is to force swarm to generate a new signing CA certificate and key, if none have been specified in `SigningCACert` and `SigningCAKey`
    @SerialName(value = "ForceRotate") val forceRotate: Int? = null,
)
