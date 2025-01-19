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
 * A container's resources (cgroups config, ulimits, etc)
 *
 * @param cpuShares An integer value representing this container's relative CPU weight versus other containers.
 * @param memory Memory limit in bytes.
 * @param cgroupParent Path to `cgroups` under which the container's `cgroup` is created. If the path is not absolute, the path is considered to be relative to the `cgroups` path of the init process. Cgroups are created if they do not already exist.
 * @param blkioWeight Block IO weight (relative weight).
 * @param blkioWeightDevice Block IO weight (relative device weight) in the form:  ``` [{\"Path\": \"device_path\", \"Weight\": weight}] ``` 
 * @param blkioDeviceReadBps Limit read rate (bytes per second) from a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
 * @param blkioDeviceWriteBps Limit write rate (bytes per second) to a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
 * @param blkioDeviceReadIOps Limit read rate (IO per second) from a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
 * @param blkioDeviceWriteIOps Limit write rate (IO per second) to a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
 * @param cpuPeriod The length of a CPU period in microseconds.
 * @param cpuQuota Microseconds of CPU time that the container can get in a CPU period.
 * @param cpuRealtimePeriod The length of a CPU real-time period in microseconds. Set to 0 to allocate no time allocated to real-time tasks.
 * @param cpuRealtimeRuntime The length of a CPU real-time runtime in microseconds. Set to 0 to allocate no time allocated to real-time tasks.
 * @param cpusetCpus CPUs in which to allow execution (e.g., `0-3`, `0,1`).
 * @param cpusetMems Memory nodes (MEMs) in which to allow execution (0-3, 0,1). Only effective on NUMA systems.
 * @param devices A list of devices to add to the container.
 * @param deviceCgroupRules a list of cgroup rules to apply to the container
 * @param deviceRequests A list of requests for devices to be sent to device drivers.
 * @param kernelMemoryTCP Hard limit for kernel TCP buffer memory (in bytes). Depending on the OCI runtime in use, this option may be ignored. It is no longer supported by the default (runc) runtime.  This field is omitted when empty.
 * @param memoryReservation Memory soft limit in bytes.
 * @param memorySwap Total memory limit (memory + swap). Set as `-1` to enable unlimited swap.
 * @param memorySwappiness Tune a container's memory swappiness behavior. Accepts an integer between 0 and 100.
 * @param nanoCpus CPU quota in units of 10<sup>-9</sup> CPUs.
 * @param oomKillDisable Disable OOM Killer for the container.
 * @param `init` Run an init inside the container that forwards signals and reaps processes. This field is omitted if empty, and the default (as configured on the daemon) is used.
 * @param pidsLimit Tune a container's PIDs limit. Set `0` or `-1` for unlimited, or `null` to not change.
 * @param ulimits A list of resource limits to set in the container. For example:  ``` {\"Name\": \"nofile\", \"Soft\": 1024, \"Hard\": 2048} ```
 * @param cpuCount The number of usable CPUs (Windows only).  On Windows Server containers, the processor resource controls are mutually exclusive. The order of precedence is `CPUCount` first, then `CPUShares`, and `CPUPercent` last.
 * @param cpuPercent The usable percentage of the available CPUs (Windows only).  On Windows Server containers, the processor resource controls are mutually exclusive. The order of precedence is `CPUCount` first, then `CPUShares`, and `CPUPercent` last.
 * @param ioMaximumIOps Maximum IOps for the container system drive (Windows only)
 * @param ioMaximumBandwidth Maximum IO in bytes per second for the container system drive (Windows only).
 */
@Serializable
data class Resources(
    // An integer value representing this container's relative CPU weight versus other containers.
    @SerialName(value = "CpuShares") val cpuShares: Int? = null,
    // Memory limit in bytes.
    @SerialName(value = "Memory") val memory: Long? = 0L,
    // Path to `cgroups` under which the container's `cgroup` is created. If the path is not absolute, the path is considered to be relative to the `cgroups` path of the init process. Cgroups are created if they do not already exist.
    @SerialName(value = "CgroupParent") val cgroupParent: String? = null,
    // Block IO weight (relative weight).
    @SerialName(value = "BlkioWeight") val blkioWeight: Int? = null,
    // Block IO weight (relative device weight) in the form:  ``` [{\"Path\": \"device_path\", \"Weight\": weight}] ```
    @SerialName(value = "BlkioWeightDevice") val blkioWeightDevice: List<ResourcesBlkioWeightDeviceInner>? = null,
    // Limit read rate (bytes per second) from a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
    @SerialName(value = "BlkioDeviceReadBps") val blkioDeviceReadBps: List<ThrottleDevice>? = null,
    // Limit write rate (bytes per second) to a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
    @SerialName(value = "BlkioDeviceWriteBps") val blkioDeviceWriteBps: List<ThrottleDevice>? = null,
    // Limit read rate (IO per second) from a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
    @SerialName(value = "BlkioDeviceReadIOps") val blkioDeviceReadIOps: List<ThrottleDevice>? = null,
    // Limit write rate (IO per second) to a device, in the form:  ``` [{\"Path\": \"device_path\", \"Rate\": rate}] ```
    @SerialName(value = "BlkioDeviceWriteIOps") val blkioDeviceWriteIOps: List<ThrottleDevice>? = null,
    // The length of a CPU period in microseconds.
    @SerialName(value = "CpuPeriod") val cpuPeriod: Long? = null,
    // Microseconds of CPU time that the container can get in a CPU period.
    @SerialName(value = "CpuQuota") val cpuQuota: Long? = null,
    // The length of a CPU real-time period in microseconds. Set to 0 to allocate no time allocated to real-time tasks.
    @SerialName(value = "CpuRealtimePeriod") val cpuRealtimePeriod: Long? = null,
    // The length of a CPU real-time runtime in microseconds. Set to 0 to allocate no time allocated to real-time tasks.
    @SerialName(value = "CpuRealtimeRuntime") val cpuRealtimeRuntime: Long? = null,
    // CPUs in which to allow execution (e.g., `0-3`, `0,1`).
    @SerialName(value = "CpusetCpus") val cpusetCpus: String? = null,
    // Memory nodes (MEMs) in which to allow execution (0-3, 0,1). Only effective on NUMA systems.
    @SerialName(value = "CpusetMems") val cpusetMems: String? = null,
    // A list of devices to add to the container.
    @SerialName(value = "Devices") val devices: List<DeviceMapping>? = null,
    // a list of cgroup rules to apply to the container
    @SerialName(value = "DeviceCgroupRules") val deviceCgroupRules: List<String>? = null,
    // A list of requests for devices to be sent to device drivers.
    @SerialName(value = "DeviceRequests") val deviceRequests: List<DeviceRequest>? = null,
    // Hard limit for kernel TCP buffer memory (in bytes). Depending on the OCI runtime in use, this option may be ignored. It is no longer supported by the default (runc) runtime.  This field is omitted when empty.
    @SerialName(value = "KernelMemoryTCP") val kernelMemoryTCP: Long? = null,
    // Memory soft limit in bytes.
    @SerialName(value = "MemoryReservation") val memoryReservation: Long? = null,
    // Total memory limit (memory + swap). Set as `-1` to enable unlimited swap.
    @SerialName(value = "MemorySwap") val memorySwap: Long? = null,
    // Tune a container's memory swappiness behavior. Accepts an integer between 0 and 100.
    @SerialName(value = "MemorySwappiness") val memorySwappiness: Long? = null,
    // CPU quota in units of 10<sup>-9</sup> CPUs.
    @SerialName(value = "NanoCpus") val nanoCpus: Long? = null,
    // Disable OOM Killer for the container.
    @SerialName(value = "OomKillDisable") val oomKillDisable: Boolean? = null,
    // Run an init inside the container that forwards signals and reaps processes. This field is omitted if empty, and the default (as configured on the daemon) is used.
    @SerialName(value = "Init") val `init`: Boolean? = null,
    // Tune a container's PIDs limit. Set `0` or `-1` for unlimited, or `null` to not change.
    @SerialName(value = "PidsLimit") val pidsLimit: Long? = null,
    // A list of resource limits to set in the container. For example:  ``` {\"Name\": \"nofile\", \"Soft\": 1024, \"Hard\": 2048} ```
    @SerialName(value = "Ulimits") val ulimits: List<ResourcesUlimitsInner>? = null,
    // The number of usable CPUs (Windows only).  On Windows Server containers, the processor resource controls are mutually exclusive. The order of precedence is `CPUCount` first, then `CPUShares`, and `CPUPercent` last.
    @SerialName(value = "CpuCount") val cpuCount: Long? = null,
    // The usable percentage of the available CPUs (Windows only).  On Windows Server containers, the processor resource controls are mutually exclusive. The order of precedence is `CPUCount` first, then `CPUShares`, and `CPUPercent` last.
    @SerialName(value = "CpuPercent") val cpuPercent: Long? = null,
    // Maximum IOps for the container system drive (Windows only)
    @SerialName(value = "IOMaximumIOps") val ioMaximumIOps: Long? = null,
    // Maximum IO in bytes per second for the container system drive (Windows only).
    @SerialName(value = "IOMaximumBandwidth") val ioMaximumBandwidth: Long? = null,
)
