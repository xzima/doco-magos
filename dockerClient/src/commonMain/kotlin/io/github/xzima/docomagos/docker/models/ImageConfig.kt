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
 * Configuration of the image. These fields are used as defaults when starting a container from the image.
 *
 * @param hostname The hostname to use for the container, as a valid RFC 1123 hostname.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
 * @param domainname The domain name to use for the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
 * @param user The user that commands are run as inside the container.
 * @param attachStdin Whether to attach to `stdin`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param attachStdout Whether to attach to `stdout`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param attachStderr Whether to attach to `stderr`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param exposedPorts An object mapping ports to an empty object in the form:  `{\"<port>/<tcp|udp|sctp>\": {}}`
 * @param tty Attach standard streams to a TTY, including `stdin` if it is not closed.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param openStdin Open `stdin`  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param stdinOnce Close `stdin` after one attached client disconnects.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
 * @param env A list of environment variables to set inside the container in the form `[\"VAR=value\", ...]`. A variable without `=` is removed from the environment, rather than to have an empty value.
 * @param cmd Command to run specified as a string or an array of strings.
 * @param healthcheck
 * @param argsEscaped Command is already escaped (Windows only)
 * @param image The name (or reference) of the image to use when creating the container, or which was used when the container was created.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
 * @param volumes An object mapping mount point paths inside the container to empty objects.
 * @param workingDir The working directory for commands to run in.
 * @param entrypoint The entry point for the container as a string or an array of strings.  If the array consists of exactly one empty string (`[\"\"]`) then the entry point is reset to system default (i.e., the entry point used by docker when there is no `ENTRYPOINT` instruction in the `Dockerfile`).
 * @param networkDisabled Disable networking for the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
 * @param macAddress MAC address of the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
 * @param onBuild `ONBUILD` metadata that were defined in the image's `Dockerfile`.
 * @param labels User-defined key/value metadata.
 * @param stopSignal Signal to stop a container as a string or unsigned integer.
 * @param stopTimeout Timeout to stop a container in seconds.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
 * @param shell Shell for when `RUN`, `CMD`, and `ENTRYPOINT` uses a shell.
 */
@Serializable
data class ImageConfig(
    // The hostname to use for the container, as a valid RFC 1123 hostname.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "Hostname") val hostname: String? = null,
    // The domain name to use for the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "Domainname") val domainname: String? = null,
    // The user that commands are run as inside the container.
    @SerialName(value = "User") val user: String? = null,
    // Whether to attach to `stdin`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "AttachStdin") val attachStdin: Boolean? = false,
    // Whether to attach to `stdout`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "AttachStdout") val attachStdout: Boolean? = false,
    // Whether to attach to `stderr`.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "AttachStderr") val attachStderr: Boolean? = false,
    // An object mapping ports to an empty object in the form:  `{\"<port>/<tcp|udp|sctp>\": {}}`
    @SerialName(value = "ExposedPorts") val exposedPorts: Map<String, String>? = null,
    // Attach standard streams to a TTY, including `stdin` if it is not closed.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "Tty") val tty: Boolean? = false,
    // Open `stdin`  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "OpenStdin") val openStdin: Boolean? = false,
    // Close `stdin` after one attached client disconnects.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always false. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "StdinOnce") val stdinOnce: Boolean? = false,
    // A list of environment variables to set inside the container in the form `[\"VAR=value\", ...]`. A variable without `=` is removed from the environment, rather than to have an empty value.
    @SerialName(value = "Env") val env: List<String>? = null,
    // Command to run specified as a string or an array of strings.
    @SerialName(value = "Cmd") val cmd: List<String>? = null,
    @SerialName(value = "Healthcheck") val healthcheck: HealthConfig? = null,
    // Command is already escaped (Windows only)
    @SerialName(value = "ArgsEscaped") val argsEscaped: Boolean? = false,
    // The name (or reference) of the image to use when creating the container, or which was used when the container was created.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always empty. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "Image") val image: String? = "",
    // An object mapping mount point paths inside the container to empty objects.
    @SerialName(value = "Volumes") val volumes: Map<String, String>? = null,
    // The working directory for commands to run in.
    @SerialName(value = "WorkingDir") val workingDir: String? = null,
    // The entry point for the container as a string or an array of strings.  If the array consists of exactly one empty string (`[\"\"]`) then the entry point is reset to system default (i.e., the entry point used by docker when there is no `ENTRYPOINT` instruction in the `Dockerfile`).
    @SerialName(value = "Entrypoint") val entrypoint: List<String>? = null,
    // Disable networking for the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "NetworkDisabled") val networkDisabled: Boolean? = false,
    // MAC address of the container.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "MacAddress") val macAddress: String? = "",
    // `ONBUILD` metadata that were defined in the image's `Dockerfile`.
    @SerialName(value = "OnBuild") val onBuild: List<String>? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // Signal to stop a container as a string or unsigned integer.
    @SerialName(value = "StopSignal") val stopSignal: String? = null,
    // Timeout to stop a container in seconds.  <p><br /></p>  > **Deprecated**: this field is not part of the image specification and is > always omitted. It must not be used, and will be removed in API v1.48.
    @SerialName(value = "StopTimeout") val stopTimeout: Int? = null,
    // Shell for when `RUN`, `CMD`, and `ENTRYPOINT` uses a shell.
    @SerialName(value = "Shell") val shell: List<String>? = null,
)
