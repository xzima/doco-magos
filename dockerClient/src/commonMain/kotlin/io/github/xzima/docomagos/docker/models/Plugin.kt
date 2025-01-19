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
 * A plugin for the Engine API
 *
 * @param name
 * @param enabled True if the plugin is running. False if the plugin is not running, only installed.
 * @param settings
 * @param config
 * @param id
 * @param pluginReference plugin remote reference used to push/pull the plugin
 */
@Serializable
data class Plugin(
    @SerialName(value = "Name") @Required val name: String,
    // True if the plugin is running. False if the plugin is not running, only installed.
    @SerialName(value = "Enabled") @Required val enabled: Boolean,
    @SerialName(value = "Settings") @Required val settings: PluginSettings,
    @SerialName(value = "Config") @Required val config: PluginConfig,
    @SerialName(value = "Id") val id: String? = null,
    // plugin remote reference used to push/pull the plugin
    @SerialName(value = "PluginReference") val pluginReference: String? = null,
)
