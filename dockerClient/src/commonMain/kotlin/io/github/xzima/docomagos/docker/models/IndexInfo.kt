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
 * IndexInfo contains information about a registry.
 *
 * @param name Name of the registry, such as \"docker.io\".
 * @param mirrors List of mirrors, expressed as URIs.
 * @param secure Indicates if the registry is part of the list of insecure registries.  If `false`, the registry is insecure. Insecure registries accept un-encrypted (HTTP) and/or untrusted (HTTPS with certificates from unknown CAs) communication.  > **Warning**: Insecure registries can be useful when running a local > registry. However, because its use creates security vulnerabilities > it should ONLY be enabled for testing purposes. For increased > security, users should add their CA to their system's list of > trusted CAs instead of enabling this option.
 * @param official Indicates whether this is an official registry (i.e., Docker Hub / docker.io)
 */
@Serializable
data class IndexInfo(
    // Name of the registry, such as \"docker.io\".
    @SerialName(value = "Name") val name: String? = null,
    // List of mirrors, expressed as URIs.
    @SerialName(value = "Mirrors") val mirrors: List<String>? = null,
    // Indicates if the registry is part of the list of insecure registries.  If `false`, the registry is insecure. Insecure registries accept un-encrypted (HTTP) and/or untrusted (HTTPS with certificates from unknown CAs) communication.  > **Warning**: Insecure registries can be useful when running a local > registry. However, because its use creates security vulnerabilities > it should ONLY be enabled for testing purposes. For increased > security, users should add their CA to their system's list of > trusted CAs instead of enabling this option.
    @SerialName(value = "Secure") val secure: Boolean? = null,
    // Indicates whether this is an official registry (i.e., Docker Hub / docker.io)
    @SerialName(value = "Official") val official: Boolean? = null,
)
