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
 * Health stores information about the container's healthcheck results.
 *
 * @param status Status is one of `none`, `starting`, `healthy` or `unhealthy`  - \"none\"      Indicates there is no healthcheck - \"starting\"  Starting indicates that the container is not yet ready - \"healthy\"   Healthy indicates that the container is running correctly - \"unhealthy\" Unhealthy indicates that the container has a problem
 * @param failingStreak FailingStreak is the number of consecutive failures
 * @param log Log contains the last few results (oldest first)
 */
@Serializable
data class Health(
    // Status is one of `none`, `starting`, `healthy` or `unhealthy`  - \"none\"      Indicates there is no healthcheck - \"starting\"  Starting indicates that the container is not yet ready - \"healthy\"   Healthy indicates that the container is running correctly - \"unhealthy\" Unhealthy indicates that the container has a problem
    @SerialName(value = "Status") val status: Status? = null,
    // FailingStreak is the number of consecutive failures
    @SerialName(value = "FailingStreak") val failingStreak: Int? = null,
    // Log contains the last few results (oldest first)
    @SerialName(value = "Log") val log: List<HealthcheckResult>? = null,
) {

    /**
     * Status is one of `none`, `starting`, `healthy` or `unhealthy`  - \"none\"      Indicates there is no healthcheck - \"starting\"  Starting indicates that the container is not yet ready - \"healthy\"   Healthy indicates that the container is running correctly - \"unhealthy\" Unhealthy indicates that the container has a problem
     *
     * Values: none,starting,healthy,unhealthy
     */
    @Serializable
    enum class Status(
        val value: String,
    ) {
        @SerialName(value = "none")
        NONE("none"),

        @SerialName(value = "starting")
        STARTING("starting"),

        @SerialName(value = "healthy")
        HEALTHY("healthy"),

        @SerialName(value = "unhealthy")
        UNHEALTHY("unhealthy"),
    }
}
