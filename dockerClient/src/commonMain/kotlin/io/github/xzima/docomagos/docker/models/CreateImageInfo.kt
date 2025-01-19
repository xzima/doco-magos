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
 * @param id
 * @param error
 * @param errorDetail
 * @param status
 * @param progress
 * @param progressDetail
 */
@Serializable
data class CreateImageInfo(
    @SerialName(value = "id") val id: String? = null,
    @SerialName(value = "error") val error: String? = null,
    @SerialName(value = "errorDetail") val errorDetail: ErrorDetail? = null,
    @SerialName(value = "status") val status: String? = null,
    @SerialName(value = "progress") val progress: String? = null,
    @SerialName(value = "progressDetail") val progressDetail: ProgressDetail? = null,
)
