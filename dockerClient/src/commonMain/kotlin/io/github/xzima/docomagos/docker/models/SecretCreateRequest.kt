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
 * @param name User-defined name of the secret.
 * @param labels User-defined key/value metadata.
 * @param `data` Base64-url-safe-encoded ([RFC 4648](https://tools.ietf.org/html/rfc4648#section-5)) data to store as secret.  This field is only used to _create_ a secret, and is not returned by other endpoints.
 * @param driver
 * @param templating
 */
@Serializable
data class SecretCreateRequest(
    // User-defined name of the secret.
    @SerialName(value = "Name") val name: String? = null,
    // User-defined key/value metadata.
    @SerialName(value = "Labels") val labels: Map<String, String>? = null,
    // Base64-url-safe-encoded ([RFC 4648](https://tools.ietf.org/html/rfc4648#section-5)) data to store as secret.  This field is only used to _create_ a secret, and is not returned by other endpoints.
    @SerialName(value = "Data") val `data`: String? = null,
    @SerialName(value = "Driver") val driver: Driver? = null,
    @SerialName(value = "Templating") val templating: Driver? = null,
)
