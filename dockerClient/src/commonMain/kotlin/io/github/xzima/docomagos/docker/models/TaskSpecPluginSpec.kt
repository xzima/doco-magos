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
 * Plugin spec for the service.  *(Experimental release only.)*  <p><br /></p>  > **Note**: ContainerSpec, NetworkAttachmentSpec, and PluginSpec are > mutually exclusive. PluginSpec is only used when the Runtime field > is set to `plugin`. NetworkAttachmentSpec is used when the Runtime > field is set to `attachment`.
 *
 * @param name The name or 'alias' to use for the plugin.
 * @param remote The plugin image reference to use.
 * @param disabled Disable the plugin once scheduled.
 * @param pluginPrivilege
 */
@Serializable
data class TaskSpecPluginSpec(
    // The name or 'alias' to use for the plugin.
    @SerialName(value = "Name") val name: String? = null,
    // The plugin image reference to use.
    @SerialName(value = "Remote") val remote: String? = null,
    // Disable the plugin once scheduled.
    @SerialName(value = "Disabled") val disabled: Boolean? = null,
    @SerialName(value = "PluginPrivilege") val pluginPrivilege: List<PluginPrivilege>? = null,
)
