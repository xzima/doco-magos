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
 * @param file
 * @param secretID SecretID represents the ID of the specific secret that we're referencing.
 * @param secretName SecretName is the name of the secret that this references, but this is just provided for lookup/display purposes. The secret in the reference will be identified by its ID.
 */
@Serializable
data class TaskSpecContainerSpecSecretsInner(
    @SerialName(value = "File") val file: TaskSpecContainerSpecSecretsInnerFile? = null,
    // SecretID represents the ID of the specific secret that we're referencing.
    @SerialName(value = "SecretID") val secretID: String? = null,
    // SecretName is the name of the secret that this references, but this is just provided for lookup/display purposes. The secret in the reference will be identified by its ID.
    @SerialName(value = "SecretName") val secretName: String? = null,
)
