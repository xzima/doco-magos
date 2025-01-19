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
 * @param description
 * @param isOfficial
 * @param isAutomated Whether this repository has automated builds enabled.  <p><br /></p>  > **Deprecated**: This field is deprecated and will always be \"false\".
 * @param name
 * @param starCount
 */
@Serializable
data class ImageSearchResponseItem(
    @SerialName(value = "description") val description: String? = null,
    @SerialName(value = "is_official") val isOfficial: Boolean? = null,
    // Whether this repository has automated builds enabled.  <p><br /></p>  > **Deprecated**: This field is deprecated and will always be \"false\".
    @SerialName(value = "is_automated") val isAutomated: Boolean? = null,
    @SerialName(value = "name") val name: String? = null,
    @SerialName(value = "star_count") val starCount: Int? = null,
)
