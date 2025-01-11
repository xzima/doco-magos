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
 * Properties that can be configured to access and load balance a service.
 *
 * @param mode The mode of resolution to use for internal load balancing between tasks.
 * @param ports List of exposed ports that this service is accessible on from the outside. Ports can only be provided if `vip` resolution mode is used.
 */
@Serializable
data class EndpointSpec(
    // The mode of resolution to use for internal load balancing between tasks.
    @SerialName(value = "Mode") val mode: Mode? = Mode.VIP,
    // List of exposed ports that this service is accessible on from the outside. Ports can only be provided if `vip` resolution mode is used.
    @SerialName(value = "Ports") val ports: List<EndpointPortConfig>? = null,
) {

    /**
     * The mode of resolution to use for internal load balancing between tasks.
     *
     * Values: vip,dnsrr
     */
    @Serializable
    enum class Mode(
        val value: String,
    ) {
        @SerialName(value = "vip")
        VIP("vip"),

        @SerialName(value = "dnsrr")
        DNSRR("dnsrr"),
    }
}
