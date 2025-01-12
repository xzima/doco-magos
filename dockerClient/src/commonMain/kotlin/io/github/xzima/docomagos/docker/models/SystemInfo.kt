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
 * @param id Unique identifier of the daemon.  <p><br /></p>  > **Note**: The format of the ID itself is not part of the API, and > should not be considered stable.
 * @param containers Total number of containers on the host.
 * @param containersRunning Number of containers with status `\"running\"`.
 * @param containersPaused Number of containers with status `\"paused\"`.
 * @param containersStopped Number of containers with status `\"stopped\"`.
 * @param images Total number of images on the host.  Both _tagged_ and _untagged_ (dangling) images are counted.
 * @param driver Name of the storage driver in use.
 * @param driverStatus Information specific to the storage driver, provided as \"label\" / \"value\" pairs.  This information is provided by the storage driver, and formatted in a way consistent with the output of `docker info` on the command line.  <p><br /></p>  > **Note**: The information returned in this field, including the > formatting of values and labels, should not be considered stable, > and may change without notice.
 * @param dockerRootDir Root directory of persistent Docker state.  Defaults to `/var/lib/docker` on Linux, and `C:\\ProgramData\\docker` on Windows.
 * @param plugins
 * @param memoryLimit Indicates if the host has memory limit support enabled.
 * @param swapLimit Indicates if the host has memory swap limit support enabled.
 * @param kernelMemoryTCP Indicates if the host has kernel memory TCP limit support enabled. This field is omitted if not supported.  Kernel memory TCP limits are not supported when using cgroups v2, which does not support the corresponding `memory.kmem.tcp.limit_in_bytes` cgroup.
 * @param cpuCfsPeriod Indicates if CPU CFS(Completely Fair Scheduler) period is supported by the host.
 * @param cpuCfsQuota Indicates if CPU CFS(Completely Fair Scheduler) quota is supported by the host.
 * @param cpUShares Indicates if CPU Shares limiting is supported by the host.
 * @param cpUSet Indicates if CPUsets (cpuset.cpus, cpuset.mems) are supported by the host.  See [cpuset(7)](https://www.kernel.org/doc/Documentation/cgroup-v1/cpusets.txt)
 * @param pidsLimit Indicates if the host kernel has PID limit support enabled.
 * @param oomKillDisable Indicates if OOM killer disable is supported on the host.
 * @param ipv4Forwarding Indicates IPv4 forwarding is enabled.
 * @param bridgeNfIptables Indicates if `bridge-nf-call-iptables` is available on the host.
 * @param bridgeNfIp6tables Indicates if `bridge-nf-call-ip6tables` is available on the host.
 * @param debug Indicates if the daemon is running in debug-mode / with debug-level logging enabled.
 * @param nfd The total number of file Descriptors in use by the daemon process.  This information is only returned if debug-mode is enabled.
 * @param ngoroutines The  number of goroutines that currently exist.  This information is only returned if debug-mode is enabled.
 * @param systemTime Current system-time in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
 * @param loggingDriver The logging driver to use as a default for new containers.
 * @param cgroupDriver The driver to use for managing cgroups.
 * @param cgroupVersion The version of the cgroup.
 * @param neventsListener Number of event listeners subscribed.
 * @param kernelVersion Kernel version of the host.  On Linux, this information obtained from `uname`. On Windows this information is queried from the <kbd>HKEY_LOCAL_MACHINE\\\\SOFTWARE\\\\Microsoft\\\\Windows NT\\\\CurrentVersion\\\\</kbd> registry value, for example _\"10.0 14393 (14393.1198.amd64fre.rs1_release_sec.170427-1353)\"_.
 * @param operatingSystem Name of the host's operating system, for example: \"Ubuntu 24.04 LTS\" or \"Windows Server 2016 Datacenter\"
 * @param osVersion Version of the host's operating system  <p><br /></p>  > **Note**: The information returned in this field, including its > very existence, and the formatting of values, should not be considered > stable, and may change without notice.
 * @param osType Generic type of the operating system of the host, as returned by the Go runtime (`GOOS`).  Currently returned values are \"linux\" and \"windows\". A full list of possible values can be found in the [Go documentation](https://go.dev/doc/install/source#environment).
 * @param architecture Hardware architecture of the host, as returned by the Go runtime (`GOARCH`).  A full list of possible values can be found in the [Go documentation](https://go.dev/doc/install/source#environment).
 * @param NCPU The number of logical CPUs usable by the daemon.  The number of available CPUs is checked by querying the operating system when the daemon starts. Changes to operating system CPU allocation after the daemon is started are not reflected.
 * @param memTotal Total amount of physical memory available on the host, in bytes.
 * @param indexServerAddress Address / URL of the index server that is used for image search, and as a default for user authentication for Docker Hub and Docker Cloud.
 * @param registryConfig
 * @param genericResources User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
 * @param httpProxy HTTP-proxy configured for the daemon. This value is obtained from the [`HTTP_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable. Credentials ([user info component](https://tools.ietf.org/html/rfc3986#section-3.2.1)) in the proxy URL are masked in the API response.  Containers do not automatically inherit this configuration.
 * @param httpsProxy HTTPS-proxy configured for the daemon. This value is obtained from the [`HTTPS_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable. Credentials ([user info component](https://tools.ietf.org/html/rfc3986#section-3.2.1)) in the proxy URL are masked in the API response.  Containers do not automatically inherit this configuration.
 * @param noProxy Comma-separated list of domain extensions for which no proxy should be used. This value is obtained from the [`NO_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable.  Containers do not automatically inherit this configuration.
 * @param name Hostname of the host.
 * @param labels User-defined labels (key/value metadata) as set on the daemon.  <p><br /></p>  > **Note**: When part of a Swarm, nodes can both have _daemon_ labels, > set through the daemon configuration, and _node_ labels, set from a > manager node in the Swarm. Node labels are not included in this > field. Node labels can be retrieved using the `/nodes/(id)` endpoint > on a manager node in the Swarm.
 * @param experimentalBuild Indicates if experimental features are enabled on the daemon.
 * @param serverVersion Version string of the daemon.
 * @param runtimes List of [OCI compliant](https://github.com/opencontainers/runtime-spec) runtimes configured on the daemon. Keys hold the \"name\" used to reference the runtime.  The Docker daemon relies on an OCI compliant runtime (invoked via the `containerd` daemon) as its interface to the Linux kernel namespaces, cgroups, and SELinux.  The default runtime is `runc`, and automatically configured. Additional runtimes can be configured by the user and will be listed here.
 * @param defaultRuntime Name of the default OCI runtime that is used when starting containers.  The default can be overridden per-container at create time.
 * @param swarm
 * @param liveRestoreEnabled Indicates if live restore is enabled.  If enabled, containers are kept running when the daemon is shutdown or upon daemon start if running containers are detected.
 * @param isolation Represents the isolation technology to use as a default for containers. The supported values are platform-specific.  If no isolation value is specified on daemon start, on Windows client, the default is `hyperv`, and on Windows server, the default is `process`.  This option is currently not used on other platforms.
 * @param initBinary Name and, optional, path of the `docker-init` binary.  If the path is omitted, the daemon searches the host's `$PATH` for the binary and uses the first result.
 * @param containerdCommit
 * @param runcCommit
 * @param initCommit
 * @param securityOptions List of security features that are enabled on the daemon, such as apparmor, seccomp, SELinux, user-namespaces (userns), rootless and no-new-privileges.  Additional configuration options for each security feature may be present, and are included as a comma-separated list of key/value pairs.
 * @param productLicense Reports a summary of the product license on the daemon.  If a commercial license has been applied to the daemon, information such as number of nodes, and expiration are included.
 * @param defaultAddressPools List of custom default address pools for local networks, which can be specified in the daemon.json file or dockerd option.  Example: a Base \"10.10.0.0/16\" with Size 24 will define the set of 256 10.10.[0-255].0/24 address pools.
 * @param warnings List of warnings / informational messages about missing features, or issues related to the daemon configuration.  These messages can be printed by the client as information to the user.
 * @param cdISpecDirs List of directories where (Container Device Interface) CDI specifications are located.  These specifications define vendor-specific modifications to an OCI runtime specification for a container being created.  An empty list indicates that CDI device injection is disabled.  Note that since using CDI device injection requires the daemon to have experimental enabled. For non-experimental daemons an empty list will always be returned.
 * @param containerd
 */
@Serializable
data class SystemInfo(
    // Unique identifier of the daemon.  <p><br /></p>  > **Note**: The format of the ID itself is not part of the API, and > should not be considered stable.
    @SerialName(value = "ID") val id: String? = null,
    // Total number of containers on the host.
    @SerialName(value = "Containers") val containers: Int? = null,
    // Number of containers with status `\"running\"`.
    @SerialName(value = "ContainersRunning") val containersRunning: Int? = null,
    // Number of containers with status `\"paused\"`.
    @SerialName(value = "ContainersPaused") val containersPaused: Int? = null,
    // Number of containers with status `\"stopped\"`.
    @SerialName(value = "ContainersStopped") val containersStopped: Int? = null,
    // Total number of images on the host.  Both _tagged_ and _untagged_ (dangling) images are counted.
    @SerialName(value = "Images") val images: Int? = null,
    // Name of the storage driver in use.
    @SerialName(value = "Driver") val driver: String? = null,
    // Information specific to the storage driver, provided as \"label\" / \"value\" pairs.  This information is provided by the storage driver, and formatted in a way consistent with the output of `docker info` on the command line.  <p><br /></p>  > **Note**: The information returned in this field, including the > formatting of values and labels, should not be considered stable, > and may change without notice.
    @SerialName(value = "DriverStatus") val driverStatus: List<List<String>>? = null,
    // Root directory of persistent Docker state.  Defaults to `/var/lib/docker` on Linux, and `C:\\ProgramData\\docker` on Windows.
    @SerialName(value = "DockerRootDir") val dockerRootDir: String? = null,
    @SerialName(value = "Plugins") val plugins: PluginsInfo? = null,
    // Indicates if the host has memory limit support enabled.
    @SerialName(value = "MemoryLimit") val memoryLimit: Boolean? = null,
    // Indicates if the host has memory swap limit support enabled.
    @SerialName(value = "SwapLimit") val swapLimit: Boolean? = null,
    // Indicates if the host has kernel memory TCP limit support enabled. This field is omitted if not supported.  Kernel memory TCP limits are not supported when using cgroups v2, which does not support the corresponding `memory.kmem.tcp.limit_in_bytes` cgroup.
    @SerialName(value = "KernelMemoryTCP") val kernelMemoryTCP: Boolean? = null,
    // Indicates if CPU CFS(Completely Fair Scheduler) period is supported by the host.
    @SerialName(value = "CpuCfsPeriod") val cpuCfsPeriod: Boolean? = null,
    // Indicates if CPU CFS(Completely Fair Scheduler) quota is supported by the host.
    @SerialName(value = "CpuCfsQuota") val cpuCfsQuota: Boolean? = null,
    // Indicates if CPU Shares limiting is supported by the host.
    @SerialName(value = "CPUShares") val cpUShares: Boolean? = null,
    // Indicates if CPUsets (cpuset.cpus, cpuset.mems) are supported by the host.  See [cpuset(7)](https://www.kernel.org/doc/Documentation/cgroup-v1/cpusets.txt)
    @SerialName(value = "CPUSet") val cpUSet: Boolean? = null,
    // Indicates if the host kernel has PID limit support enabled.
    @SerialName(value = "PidsLimit") val pidsLimit: Boolean? = null,
    // Indicates if OOM killer disable is supported on the host.
    @SerialName(value = "OomKillDisable") val oomKillDisable: Boolean? = null,
    // Indicates IPv4 forwarding is enabled.
    @SerialName(value = "IPv4Forwarding") val ipv4Forwarding: Boolean? = null,
    // Indicates if `bridge-nf-call-iptables` is available on the host.
    @SerialName(value = "BridgeNfIptables") val bridgeNfIptables: Boolean? = null,
    // Indicates if `bridge-nf-call-ip6tables` is available on the host.
    @SerialName(value = "BridgeNfIp6tables") val bridgeNfIp6tables: Boolean? = null,
    // Indicates if the daemon is running in debug-mode / with debug-level logging enabled.
    @SerialName(value = "Debug") val debug: Boolean? = null,
    // The total number of file Descriptors in use by the daemon process.  This information is only returned if debug-mode is enabled.
    @SerialName(value = "NFd") val nfd: Int? = null,
    // The  number of goroutines that currently exist.  This information is only returned if debug-mode is enabled.
    @SerialName(value = "NGoroutines") val ngoroutines: Int? = null,
    // Current system-time in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nano-seconds.
    @SerialName(value = "SystemTime") val systemTime: String? = null,
    // The logging driver to use as a default for new containers.
    @SerialName(value = "LoggingDriver") val loggingDriver: String? = null,
    // The driver to use for managing cgroups.
    @SerialName(value = "CgroupDriver") val cgroupDriver: CgroupDriver? = CgroupDriver.CGROUPFS,
    // The version of the cgroup.
    @SerialName(value = "CgroupVersion") val cgroupVersion: CgroupVersion? = CgroupVersion.V1,
    // Number of event listeners subscribed.
    @SerialName(value = "NEventsListener") val neventsListener: Int? = null,
    // Kernel version of the host.  On Linux, this information obtained from `uname`. On Windows this information is queried from the <kbd>HKEY_LOCAL_MACHINE\\\\SOFTWARE\\\\Microsoft\\\\Windows NT\\\\CurrentVersion\\\\</kbd> registry value, for example _\"10.0 14393 (14393.1198.amd64fre.rs1_release_sec.170427-1353)\"_.
    @SerialName(value = "KernelVersion") val kernelVersion: String? = null,
    // Name of the host's operating system, for example: \"Ubuntu 24.04 LTS\" or \"Windows Server 2016 Datacenter\"
    @SerialName(value = "OperatingSystem") val operatingSystem: String? = null,
    // Version of the host's operating system  <p><br /></p>  > **Note**: The information returned in this field, including its > very existence, and the formatting of values, should not be considered > stable, and may change without notice.
    @SerialName(value = "OSVersion") val osVersion: String? = null,
    // Generic type of the operating system of the host, as returned by the Go runtime (`GOOS`).  Currently returned values are \"linux\" and \"windows\". A full list of possible values can be found in the [Go documentation](https://go.dev/doc/install/source#environment).
    @SerialName(value = "OSType") val osType: String? = null,
    // Hardware architecture of the host, as returned by the Go runtime (`GOARCH`).  A full list of possible values can be found in the [Go documentation](https://go.dev/doc/install/source#environment).
    @SerialName(value = "Architecture") val architecture: String? = null,
    // The number of logical CPUs usable by the daemon.  The number of available CPUs is checked by querying the operating system when the daemon starts. Changes to operating system CPU allocation after the daemon is started are not reflected.
    @SerialName(value = "NCPU") val NCPU: Int? = null,
    // Total amount of physical memory available on the host, in bytes.
    @SerialName(value = "MemTotal") val memTotal: Long? = null,
    // Address / URL of the index server that is used for image search, and as a default for user authentication for Docker Hub and Docker Cloud.
    @SerialName(value = "IndexServerAddress") val indexServerAddress: String? = "https://index.docker.io/v1/",
    @SerialName(value = "RegistryConfig") val registryConfig: RegistryServiceConfig? = null,
    // User-defined resources can be either Integer resources (e.g, `SSD=3`) or String resources (e.g, `GPU=UUID1`).
    @SerialName(value = "GenericResources") val genericResources: List<GenericResourcesInner>? = null,
    // HTTP-proxy configured for the daemon. This value is obtained from the [`HTTP_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable. Credentials ([user info component](https://tools.ietf.org/html/rfc3986#section-3.2.1)) in the proxy URL are masked in the API response.  Containers do not automatically inherit this configuration.
    @SerialName(value = "HttpProxy") val httpProxy: String? = null,
    // HTTPS-proxy configured for the daemon. This value is obtained from the [`HTTPS_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable. Credentials ([user info component](https://tools.ietf.org/html/rfc3986#section-3.2.1)) in the proxy URL are masked in the API response.  Containers do not automatically inherit this configuration.
    @SerialName(value = "HttpsProxy") val httpsProxy: String? = null,
    // Comma-separated list of domain extensions for which no proxy should be used. This value is obtained from the [`NO_PROXY`](https://www.gnu.org/software/wget/manual/html_node/Proxies.html) environment variable.  Containers do not automatically inherit this configuration.
    @SerialName(value = "NoProxy") val noProxy: String? = null,
    // Hostname of the host.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined labels (key/value metadata) as set on the daemon.  <p><br /></p>  > **Note**: When part of a Swarm, nodes can both have _daemon_ labels, > set through the daemon configuration, and _node_ labels, set from a > manager node in the Swarm. Node labels are not included in this > field. Node labels can be retrieved using the `/nodes/(id)` endpoint > on a manager node in the Swarm.
    @SerialName(value = "Labels") val labels: List<String>? = null,
    // Indicates if experimental features are enabled on the daemon.
    @SerialName(value = "ExperimentalBuild") val experimentalBuild: Boolean? = null,
    // Version string of the daemon.
    @SerialName(value = "ServerVersion") val serverVersion: String? = null,
    // List of [OCI compliant](https://github.com/opencontainers/runtime-spec) runtimes configured on the daemon. Keys hold the \"name\" used to reference the runtime.  The Docker daemon relies on an OCI compliant runtime (invoked via the `containerd` daemon) as its interface to the Linux kernel namespaces, cgroups, and SELinux.  The default runtime is `runc`, and automatically configured. Additional runtimes can be configured by the user and will be listed here.
    @SerialName(value = "Runtimes") val runtimes: Map<String, Runtime>? = null,
    // Name of the default OCI runtime that is used when starting containers.  The default can be overridden per-container at create time.
    @SerialName(value = "DefaultRuntime") val defaultRuntime: String? = "runc",
    @SerialName(value = "Swarm") val swarm: SwarmInfo? = null,
    // Indicates if live restore is enabled.  If enabled, containers are kept running when the daemon is shutdown or upon daemon start if running containers are detected.
    @SerialName(value = "LiveRestoreEnabled") val liveRestoreEnabled: Boolean? = false,
    // Represents the isolation technology to use as a default for containers. The supported values are platform-specific.  If no isolation value is specified on daemon start, on Windows client, the default is `hyperv`, and on Windows server, the default is `process`.  This option is currently not used on other platforms.
    @SerialName(value = "Isolation") val isolation: Isolation? = Isolation.DEFAULT,
    // Name and, optional, path of the `docker-init` binary.  If the path is omitted, the daemon searches the host's `$PATH` for the binary and uses the first result.
    @SerialName(value = "InitBinary") val initBinary: String? = null,
    @SerialName(value = "ContainerdCommit") val containerdCommit: Commit? = null,
    @SerialName(value = "RuncCommit") val runcCommit: Commit? = null,
    @SerialName(value = "InitCommit") val initCommit: Commit? = null,
    // List of security features that are enabled on the daemon, such as apparmor, seccomp, SELinux, user-namespaces (userns), rootless and no-new-privileges.  Additional configuration options for each security feature may be present, and are included as a comma-separated list of key/value pairs.
    @SerialName(value = "SecurityOptions") val securityOptions: List<String>? = null,
    // Reports a summary of the product license on the daemon.  If a commercial license has been applied to the daemon, information such as number of nodes, and expiration are included.
    @SerialName(value = "ProductLicense") val productLicense: String? = null,
    // List of custom default address pools for local networks, which can be specified in the daemon.json file or dockerd option.  Example: a Base \"10.10.0.0/16\" with Size 24 will define the set of 256 10.10.[0-255].0/24 address pools.
    @SerialName(value = "DefaultAddressPools") val defaultAddressPools:
        List<SystemInfoDefaultAddressPoolsInner>? = null,
    // List of warnings / informational messages about missing features, or issues related to the daemon configuration.  These messages can be printed by the client as information to the user.
    @SerialName(value = "Warnings") val warnings: List<String>? = null,
    // List of directories where (Container Device Interface) CDI specifications are located.  These specifications define vendor-specific modifications to an OCI runtime specification for a container being created.  An empty list indicates that CDI device injection is disabled.  Note that since using CDI device injection requires the daemon to have experimental enabled. For non-experimental daemons an empty list will always be returned.
    @SerialName(value = "CDISpecDirs") val cdISpecDirs: List<String>? = null,
    @SerialName(value = "Containerd") val containerd: ContainerdInfo? = null,
) {

    /**
     * The driver to use for managing cgroups.
     *
     * Values: cgroupfs,systemd,none
     */
    @Serializable
    enum class CgroupDriver(
        val value: String,
    ) {
        @SerialName(value = "cgroupfs")
        CGROUPFS("cgroupfs"),

        @SerialName(value = "systemd")
        SYSTEMD("systemd"),

        @SerialName(value = "none")
        NONE("none"),
    }

    /**
     * The version of the cgroup.
     *
     * Values: _1,_2
     */
    @Serializable
    enum class CgroupVersion(
        val value: String,
    ) {
        @SerialName(value = "1")
        V1("1"),

        @SerialName(value = "2")
        V2("2"),
    }

    /**
     * Represents the isolation technology to use as a default for containers. The supported values are platform-specific.  If no isolation value is specified on daemon start, on Windows client, the default is `hyperv`, and on Windows server, the default is `process`.  This option is currently not used on other platforms.
     *
     * Values: default,hyperv,process
     */
    @Serializable
    enum class Isolation(
        val value: String,
    ) {
        @SerialName(value = "")
        UNKNOWN(""),

        @SerialName(value = "default")
        DEFAULT("default"),

        @SerialName(value = "hyperv")
        HYPERV("hyperv"),

        @SerialName(value = "process")
        PROCESS("process"),
    }
}
