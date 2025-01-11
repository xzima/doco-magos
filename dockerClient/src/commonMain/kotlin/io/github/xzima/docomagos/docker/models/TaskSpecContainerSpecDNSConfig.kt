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
 * Specification for DNS related configurations in resolver configuration file (`resolv.conf`).
 *
 * @param nameservers The IP addresses of the name servers.
 * @param search A search list for host-name lookup.
 * @param options A list of internal resolver variables to be modified (e.g., `debug`, `ndots:3`, etc.).
 */
@Serializable
data class TaskSpecContainerSpecDNSConfig(
    // The IP addresses of the name servers.
    @SerialName(value = "Nameservers") val nameservers: List<String>? = null,
    // A search list for host-name lookup.
    @SerialName(value = "Search") val search: List<String>? = null,
    // A list of internal resolver variables to be modified (e.g., `debug`, `ndots:3`, etc.).
    @SerialName(value = "Options") val options: List<String>? = null,
)
