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
 * @param base The network address in CIDR format
 * @param propertySize The network pool size
 */
@Serializable
data class SystemInfoDefaultAddressPoolsInner(
    // The network address in CIDR format
    @SerialName(value = "Base") val base: String? = null,
    // The network pool size
    @SerialName(value = "Size") val propertySize: Int? = null,
)
