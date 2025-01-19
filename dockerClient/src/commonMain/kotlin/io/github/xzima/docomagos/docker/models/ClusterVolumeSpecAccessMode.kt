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
 * Defines how the volume is used by tasks.
 *
 * @param scope The set of nodes this volume can be used on at one time. - `single` The volume may only be scheduled to one node at a time. - `multi` the volume may be scheduled to any supported number of nodes at a time.
 * @param sharing The number and way that different tasks can use this volume at one time. - `none` The volume may only be used by one task at a time. - `readonly` The volume may be used by any number of tasks, but they all must mount the volume as readonly - `onewriter` The volume may be used by any number of tasks, but only one may mount it as read/write. - `all` The volume may have any number of readers and writers.
 * @param mountVolume Options for using this volume as a Mount-type volume.      Either MountVolume or BlockVolume, but not both, must be     present.   properties:     FsType:       type: \"string\"       description: |         Specifies the filesystem type for the mount volume.         Optional.     MountFlags:       type: \"array\"       description: |         Flags to pass when mounting the volume. Optional.       items:         type: \"string\" BlockVolume:   type: \"object\"   description: |     Options for using this volume as a Block-type volume.     Intentionally empty.
 * @param secrets Swarm Secrets that are passed to the CSI storage plugin when operating on this volume.
 * @param accessibilityRequirements
 * @param capacityRange
 * @param availability The availability of the volume for use in tasks. - `active` The volume is fully available for scheduling on the cluster - `pause` No new workloads should use the volume, but existing workloads are not stopped. - `drain` All workloads using this volume should be stopped and rescheduled, and no new ones should be started.
 */
@Serializable
data class ClusterVolumeSpecAccessMode(
    // The set of nodes this volume can be used on at one time. - `single` The volume may only be scheduled to one node at a time. - `multi` the volume may be scheduled to any supported number of nodes at a time.
    @SerialName(value = "Scope") val scope: Scope? = Scope.SINGLE,
    // The number and way that different tasks can use this volume at one time. - `none` The volume may only be used by one task at a time. - `readonly` The volume may be used by any number of tasks, but they all must mount the volume as readonly - `onewriter` The volume may be used by any number of tasks, but only one may mount it as read/write. - `all` The volume may have any number of readers and writers.
    @SerialName(value = "Sharing") val sharing: Sharing? = Sharing.NONE,
    // Options for using this volume as a Mount-type volume.      Either MountVolume or BlockVolume, but not both, must be     present.   properties:     FsType:       type: \"string\"       description: |         Specifies the filesystem type for the mount volume.         Optional.     MountFlags:       type: \"array\"       description: |         Flags to pass when mounting the volume. Optional.       items:         type: \"string\" BlockVolume:   type: \"object\"   description: |     Options for using this volume as a Block-type volume.     Intentionally empty.
    @SerialName(value = "MountVolume") val mountVolume: String? = null,
    // Swarm Secrets that are passed to the CSI storage plugin when operating on this volume.
    @SerialName(value = "Secrets") val secrets: List<ClusterVolumeSpecAccessModeSecretsInner>? = null,
    @SerialName(value = "AccessibilityRequirements") val accessibilityRequirements:
        ClusterVolumeSpecAccessModeAccessibilityRequirements? = null,
    @SerialName(value = "CapacityRange") val capacityRange: ClusterVolumeSpecAccessModeCapacityRange? = null,
    // The availability of the volume for use in tasks. - `active` The volume is fully available for scheduling on the cluster - `pause` No new workloads should use the volume, but existing workloads are not stopped. - `drain` All workloads using this volume should be stopped and rescheduled, and no new ones should be started.
    @SerialName(value = "Availability") val availability: Availability? = Availability.ACTIVE,
) {

    /**
     * The set of nodes this volume can be used on at one time. - `single` The volume may only be scheduled to one node at a time. - `multi` the volume may be scheduled to any supported number of nodes at a time.
     *
     * Values: single,multi
     */
    @Serializable
    enum class Scope(
        val value: String,
    ) {
        @SerialName(value = "single")
        SINGLE("single"),

        @SerialName(value = "multi")
        MULTI("multi"),
    }

    /**
     * The number and way that different tasks can use this volume at one time. - `none` The volume may only be used by one task at a time. - `readonly` The volume may be used by any number of tasks, but they all must mount the volume as readonly - `onewriter` The volume may be used by any number of tasks, but only one may mount it as read/write. - `all` The volume may have any number of readers and writers.
     *
     * Values: none,readonly,onewriter,all
     */
    @Serializable
    enum class Sharing(
        val value: String,
    ) {
        @SerialName(value = "none")
        NONE("none"),

        @SerialName(value = "readonly")
        READONLY("readonly"),

        @SerialName(value = "onewriter")
        ONEWRITER("onewriter"),

        @SerialName(value = "all")
        ALL("all"),
    }

    /**
     * The availability of the volume for use in tasks. - `active` The volume is fully available for scheduling on the cluster - `pause` No new workloads should use the volume, but existing workloads are not stopped. - `drain` All workloads using this volume should be stopped and rescheduled, and no new ones should be started.
     *
     * Values: active,pause,drain
     */
    @Serializable
    enum class Availability(
        val value: String,
    ) {
        @SerialName(value = "active")
        ACTIVE("active"),

        @SerialName(value = "pause")
        PAUSE("pause"),

        @SerialName(value = "drain")
        DRAIN("drain"),
    }
}
