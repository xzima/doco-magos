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
 * Response of Engine API: GET \"/version\"
 *
 * @param platform
 * @param components Information about system components
 * @param version The version of the daemon
 * @param apiVersion The default (and highest) API version that is supported by the daemon
 * @param minAPIVersion The minimum API version that is supported by the daemon
 * @param gitCommit The Git commit of the source code that was used to build the daemon
 * @param goVersion The version Go used to compile the daemon, and the version of the Go runtime in use.
 * @param os The operating system that the daemon is running on (\"linux\" or \"windows\")
 * @param arch The architecture that the daemon is running on
 * @param kernelVersion The kernel version (`uname -r`) that the daemon is running on.  This field is omitted when empty.
 * @param experimental Indicates if the daemon is started with experimental features enabled.  This field is omitted when empty / false.
 * @param buildTime The date and time that the daemon was compiled.
 */
@Serializable
data class SystemVersion(
    @SerialName(value = "Platform") val platform: SystemVersionPlatform? = null,
    // Information about system components
    @SerialName(value = "Components") val components: List<SystemVersionComponentsInner>? = null,
    // The version of the daemon
    @SerialName(value = "Version") val version: String? = null,
    // The default (and highest) API version that is supported by the daemon
    @SerialName(value = "ApiVersion") val apiVersion: String? = null,
    // The minimum API version that is supported by the daemon
    @SerialName(value = "MinAPIVersion") val minAPIVersion: String? = null,
    // The Git commit of the source code that was used to build the daemon
    @SerialName(value = "GitCommit") val gitCommit: String? = null,
    // The version Go used to compile the daemon, and the version of the Go runtime in use.
    @SerialName(value = "GoVersion") val goVersion: String? = null,
    // The operating system that the daemon is running on (\"linux\" or \"windows\")
    @SerialName(value = "Os") val os: String? = null,
    // The architecture that the daemon is running on
    @SerialName(value = "Arch") val arch: String? = null,
    // The kernel version (`uname -r`) that the daemon is running on.  This field is omitted when empty.
    @SerialName(value = "KernelVersion") val kernelVersion: String? = null,
    // Indicates if the daemon is started with experimental features enabled.  This field is omitted when empty / false.
    @SerialName(value = "Experimental") val experimental: Boolean? = null,
    // The date and time that the daemon was compiled.
    @SerialName(value = "BuildTime") val buildTime: String? = null,
)
