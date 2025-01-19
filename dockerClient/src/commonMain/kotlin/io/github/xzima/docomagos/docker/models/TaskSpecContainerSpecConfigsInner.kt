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
 * @param runtime Runtime represents a target that is not mounted into the container but is used by the task  <p><br /><p>  > **Note**: `Configs.File` and `Configs.Runtime` are mutually > exclusive
 * @param configID ConfigID represents the ID of the specific config that we're referencing.
 * @param configName ConfigName is the name of the config that this references, but this is just provided for lookup/display purposes. The config in the reference will be identified by its ID.
 */
@Serializable
data class TaskSpecContainerSpecConfigsInner(
    @SerialName(value = "File") val file: TaskSpecContainerSpecConfigsInnerFile? = null,
    // Runtime represents a target that is not mounted into the container but is used by the task  <p><br /><p>  > **Note**: `Configs.File` and `Configs.Runtime` are mutually > exclusive
    @SerialName(value = "Runtime") val runtime: String? = null,
    // ConfigID represents the ID of the specific config that we're referencing.
    @SerialName(value = "ConfigID") val configID: String? = null,
    // ConfigName is the name of the config that this references, but this is just provided for lookup/display purposes. The config in the reference will be identified by its ID.
    @SerialName(value = "ConfigName") val configName: String? = null,
)
