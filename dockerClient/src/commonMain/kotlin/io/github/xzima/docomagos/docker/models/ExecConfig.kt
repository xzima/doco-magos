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
 * @param attachStdin Attach to `stdin` of the exec command.
 * @param attachStdout Attach to `stdout` of the exec command.
 * @param attachStderr Attach to `stderr` of the exec command.
 * @param consoleSize Initial console size, as an `[height, width]` array.
 * @param detachKeys Override the key sequence for detaching a container. Format is a single character `[a-Z]` or `ctrl-<value>` where `<value>` is one of: `a-z`, `@`, `^`, `[`, `,` or `_`.
 * @param tty Allocate a pseudo-TTY.
 * @param env A list of environment variables in the form `[\"VAR=value\", ...]`.
 * @param cmd Command to run, as a string or array of strings.
 * @param privileged Runs the exec process with extended privileges.
 * @param user The user, and optionally, group to run the exec process inside the container. Format is one of: `user`, `user:group`, `uid`, or `uid:gid`.
 * @param workingDir The working directory for the exec process inside the container.
 */
@Serializable
data class ExecConfig(
    // Attach to `stdin` of the exec command.
    @SerialName(value = "AttachStdin") val attachStdin: Boolean? = null,
    // Attach to `stdout` of the exec command.
    @SerialName(value = "AttachStdout") val attachStdout: Boolean? = null,
    // Attach to `stderr` of the exec command.
    @SerialName(value = "AttachStderr") val attachStderr: Boolean? = null,
    // Initial console size, as an `[height, width]` array.
    @SerialName(value = "ConsoleSize") val consoleSize: List<Int>? = null,
    // Override the key sequence for detaching a container. Format is a single character `[a-Z]` or `ctrl-<value>` where `<value>` is one of: `a-z`, `@`, `^`, `[`, `,` or `_`.
    @SerialName(value = "DetachKeys") val detachKeys: String? = null,
    // Allocate a pseudo-TTY.
    @SerialName(value = "Tty") val tty: Boolean? = null,
    // A list of environment variables in the form `[\"VAR=value\", ...]`.
    @SerialName(value = "Env") val env: List<String>? = null,
    // Command to run, as a string or array of strings.
    @SerialName(value = "Cmd") val cmd: List<String>? = null,
    // Runs the exec process with extended privileges.
    @SerialName(value = "Privileged") val privileged: Boolean? = false,
    // The user, and optionally, group to run the exec process inside the container. Format is one of: `user`, `user:group`, `uid`, or `uid:gid`.
    @SerialName(value = "User") val user: String? = null,
    // The working directory for the exec process inside the container.
    @SerialName(value = "WorkingDir") val workingDir: String? = null,
)
