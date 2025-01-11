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
 * Platform represents the platform (Arch/OS).
 *
 * @param architecture Architecture represents the hardware architecture (for example, `x86_64`).
 * @param os OS represents the Operating System (for example, `linux` or `windows`).
 */
@Serializable
data class Platform(
    // Architecture represents the hardware architecture (for example, `x86_64`).
    @SerialName(value = "Architecture") val architecture: String? = null,
    // OS represents the Operating System (for example, `linux` or `windows`).
    @SerialName(value = "OS") val os: String? = null,
)
