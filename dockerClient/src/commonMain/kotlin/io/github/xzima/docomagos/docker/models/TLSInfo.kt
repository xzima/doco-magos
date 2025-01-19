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
 * Information about the issuer of leaf TLS certificates and the trusted root CA certificate.
 *
 * @param trustRoot The root CA certificate(s) that are used to validate leaf TLS certificates.
 * @param certIssuerSubject The base64-url-safe-encoded raw subject bytes of the issuer.
 * @param certIssuerPublicKey The base64-url-safe-encoded raw public key bytes of the issuer.
 */
@Serializable
data class TLSInfo(
    // The root CA certificate(s) that are used to validate leaf TLS certificates.
    @SerialName(value = "TrustRoot") val trustRoot: String? = null,
    // The base64-url-safe-encoded raw subject bytes of the issuer.
    @SerialName(value = "CertIssuerSubject") val certIssuerSubject: String? = null,
    // The base64-url-safe-encoded raw public key bytes of the issuer.
    @SerialName(value = "CertIssuerPublicKey") val certIssuerPublicKey: String? = null,
)
