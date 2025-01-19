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
 * The log driver to use for tasks created in the orchestrator if unspecified by a service.  Updating this value only affects new tasks. Existing tasks continue to use their previously configured log driver until recreated.
 *
 * @param name The log driver to use as a default for new tasks.
 * @param options Driver-specific options for the selected log driver, specified as key/value pairs.
 */
@Serializable
data class SwarmSpecTaskDefaultsLogDriver(
    // The log driver to use as a default for new tasks.
    @SerialName(value = "Name") val name: String? = null,
    // Driver-specific options for the selected log driver, specified as key/value pairs.
    @SerialName(value = "Options") val options: Map<String, String>? = null,
)
