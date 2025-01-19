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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Container spec for the service.  <p><br /></p>  > **Note**: ContainerSpec, NetworkAttachmentSpec, and PluginSpec are > mutually exclusive. PluginSpec is only used when the Runtime field > is set to `plugin`. NetworkAttachmentSpec is used when the Runtime > field is set to `attachment`.
 *
 * @param image The image name to use for the container
 * @param labels User-defined key/value data.
 * @param command The command to be run in the image.
 * @param args Arguments to the command.
 * @param hostname The hostname to use for the container, as a valid [RFC 1123](https://tools.ietf.org/html/rfc1123) hostname.
 * @param env A list of environment variables in the form `VAR=value`.
 * @param dir The working directory for commands to run in.
 * @param user The user inside the container.
 * @param groups A list of additional groups that the container process will run as.
 * @param privileges
 * @param TTY Whether a pseudo-TTY should be allocated.
 * @param openStdin Open `stdin`
 * @param readOnly Mount the container's root filesystem as read only.
 * @param mounts Specification for mounts to be added to containers created as part of the service.
 * @param stopSignal Signal to stop the container.
 * @param stopGracePeriod Amount of time to wait for the container to terminate before forcefully killing it.
 * @param healthCheck
 * @param hosts A list of hostname/IP mappings to add to the container's `hosts` file. The format of extra hosts is specified in the [hosts(5)](http://man7.org/linux/man-pages/man5/hosts.5.html)
 * @param dnSConfig
 * @param secrets Secrets contains references to zero or more secrets that will be exposed to the service.
 * @param oomScoreAdj An integer value containing the score given to the container in order to tune OOM killer preferences.
 * @param configs Configs contains references to zero or more configs that will be exposed to the service.
 * @param isolation Isolation technology of the containers running the service. (Windows only)
 * @param `init` Run an init inside the container that forwards signals and reaps processes. This field is omitted if empty, and the default (as configured on the daemon) is used.
 * @param sysctls Set kernel namedspaced parameters (sysctls) in the container. The Sysctls option on services accepts the same sysctls as the are supported on containers. Note that while the same sysctls are supported, no guarantees or checks are made about their suitability for a clustered environment, and it's up to the user to determine whether a given sysctl will work properly in a Service.
 * @param capabilityAdd A list of kernel capabilities to add to the default set for the container.
 * @param capabilityDrop A list of kernel capabilities to drop from the default set for the container.
 * @param ulimits A list of resource limits to set in the container. For example: `{\"Name\": \"nofile\", \"Soft\": 1024, \"Hard\": 2048}`\"
 */
@Serializable
data class TaskSpecContainerSpec(
    // The image name to use for the container
    @SerialName(value = "Image") val image: String? = null,
    // User-defined key/value data.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // The command to be run in the image.
    @SerialName(value = "Command") val command: List<String>? = null,
    // Arguments to the command.
    @SerialName(value = "Args") val args: List<String>? = null,
    // The hostname to use for the container, as a valid [RFC 1123](https://tools.ietf.org/html/rfc1123) hostname.
    @SerialName(value = "Hostname") val hostname: String? = null,
    // A list of environment variables in the form `VAR=value`.
    @SerialName(value = "Env") val env: List<String>? = null,
    // The working directory for commands to run in.
    @SerialName(value = "Dir") val dir: String? = null,
    // The user inside the container.
    @SerialName(value = "User") val user: String? = null,
    // A list of additional groups that the container process will run as.
    @SerialName(value = "Groups") val groups: List<String>? = null,
    @SerialName(value = "Privileges") val privileges: TaskSpecContainerSpecPrivileges? = null,
    // Whether a pseudo-TTY should be allocated.
    @SerialName(value = "TTY") val TTY: Boolean? = null,
    // Open `stdin`
    @SerialName(value = "OpenStdin") val openStdin: Boolean? = null,
    // Mount the container's root filesystem as read only.
    @SerialName(value = "ReadOnly") val readOnly: Boolean? = null,
    // Specification for mounts to be added to containers created as part of the service.
    @SerialName(value = "Mounts") val mounts: List<Mount>? = null,
    // Signal to stop the container.
    @SerialName(value = "StopSignal") val stopSignal: String? = null,
    // Amount of time to wait for the container to terminate before forcefully killing it.
    @SerialName(value = "StopGracePeriod") val stopGracePeriod: Long? = null,
    @SerialName(value = "HealthCheck") val healthCheck: HealthConfig? = null,
    // A list of hostname/IP mappings to add to the container's `hosts` file. The format of extra hosts is specified in the [hosts(5)](http://man7.org/linux/man-pages/man5/hosts.5.html) man page:      IP_address canonical_hostname [aliases...]
    @SerialName(value = "Hosts") val hosts: List<String>? = null,
    @SerialName(value = "DNSConfig") val dnSConfig: TaskSpecContainerSpecDNSConfig? = null,
    // Secrets contains references to zero or more secrets that will be exposed to the service.
    @SerialName(value = "Secrets") val secrets: List<TaskSpecContainerSpecSecretsInner>? = null,
    // An integer value containing the score given to the container in order to tune OOM killer preferences.
    @SerialName(value = "OomScoreAdj") val oomScoreAdj: Long? = null,
    // Configs contains references to zero or more configs that will be exposed to the service.
    @SerialName(value = "Configs") val configs: List<TaskSpecContainerSpecConfigsInner>? = null,
    // Isolation technology of the containers running the service. (Windows only)
    @SerialName(value = "Isolation") val isolation: Isolation? = null,
    // Run an init inside the container that forwards signals and reaps processes. This field is omitted if empty, and the default (as configured on the daemon) is used.
    @SerialName(value = "Init") val `init`: Boolean? = null,
    // Set kernel namedspaced parameters (sysctls) in the container. The Sysctls option on services accepts the same sysctls as the are supported on containers. Note that while the same sysctls are supported, no guarantees or checks are made about their suitability for a clustered environment, and it's up to the user to determine whether a given sysctl will work properly in a Service.
    @SerialName(value = "Sysctls") val sysctls: Map<String, String>? = null,
    // A list of kernel capabilities to add to the default set for the container.
    @SerialName(value = "CapabilityAdd") val capabilityAdd: List<String>? = null,
    // A list of kernel capabilities to drop from the default set for the container.
    @SerialName(value = "CapabilityDrop") val capabilityDrop: List<String>? = null,
    // A list of resource limits to set in the container. For example: `{\"Name\": \"nofile\", \"Soft\": 1024, \"Hard\": 2048}`\"
    @SerialName(value = "Ulimits") val ulimits: List<ResourcesUlimitsInner>? = null,
) {

    /**
     * Isolation technology of the containers running the service. (Windows only)
     *
     * Values: default,process,hyperv
     */
    @Serializable
    enum class Isolation(
        val value: String,
    ) {
        @SerialName(value = "default")
        DEFAULT("default"),

        @SerialName(value = "process")
        PROCESS("process"),

        @SerialName(value = "hyperv")
        HYPERV("hyperv"),
    }
}
