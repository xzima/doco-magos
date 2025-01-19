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
 * The namespaces that the daemon uses for running containers and plugins in containerd. These namespaces can be configured in the daemon configuration, and are considered to be used exclusively by the daemon, Tampering with the containerd instance may cause unexpected behavior.  As these namespaces are considered to be exclusively accessed by the daemon, it is not recommended to change these values, or to change them to a value that is used by other systems, such as cri-containerd.
 *
 * @param containers The default containerd namespace used for containers managed by the daemon.  The default namespace for containers is \"moby\", but will be suffixed with the `<uid>.<gid>` of the remapped `root` if user-namespaces are enabled and the containerd image-store is used.
 * @param plugins The default containerd namespace used for plugins managed by the daemon.  The default namespace for plugins is \"plugins.moby\", but will be suffixed with the `<uid>.<gid>` of the remapped `root` if user-namespaces are enabled and the containerd image-store is used.
 */
@Serializable
data class ContainerdInfoNamespaces(
    // The default containerd namespace used for containers managed by the daemon.  The default namespace for containers is \"moby\", but will be suffixed with the `<uid>.<gid>` of the remapped `root` if user-namespaces are enabled and the containerd image-store is used.
    @SerialName(value = "Containers") val containers: String? = "moby",
    // The default containerd namespace used for plugins managed by the daemon.  The default namespace for plugins is \"plugins.moby\", but will be suffixed with the `<uid>.<gid>` of the remapped `root` if user-namespaces are enabled and the containerd image-store is used.
    @SerialName(value = "Plugins") val plugins: String? = "plugins.moby",
)
