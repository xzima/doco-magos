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
 * @param detach Detach from the command.
 * @param tty Allocate a pseudo-TTY.
 * @param consoleSize Initial console size, as an `[height, width]` array.
 */
@Serializable
data class ExecStartConfig(
    // Detach from the command.
    @SerialName(value = "Detach") val detach: Boolean? = null,
    // Allocate a pseudo-TTY.
    @SerialName(value = "Tty") val tty: Boolean? = null,
    // Initial console size, as an `[height, width]` array.
    @SerialName(value = "ConsoleSize") val consoleSize: List<Int>? = null,
)
