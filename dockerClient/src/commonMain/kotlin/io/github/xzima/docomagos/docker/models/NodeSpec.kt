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
 * @param name Name for the node.
 * @param labels User-defined key/value metadata.
 * @param role Role of the node.
 * @param availability Availability of the node.
 */
@Serializable
data class NodeSpec(
    // Name for the node.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // Role of the node.
    @SerialName(value = "Role") val role: Role? = null,
    // Availability of the node.
    @SerialName(value = "Availability") val availability: Availability? = null,
) {

    /**
     * Role of the node.
     *
     * Values: worker,manager
     */
    @Serializable
    enum class Role(
        val value: String,
    ) {
        @SerialName(value = "worker")
        WORKER("worker"),

        @SerialName(value = "manager")
        MANAGER("manager"),
    }

    /**
     * Availability of the node.
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
