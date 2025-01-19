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
 * @param nodeID The ID of the Swarm node the volume is published on.
 * @param state The published state of the volume. * `pending-publish` The volume should be published to this node, but the call to the controller plugin to do so has not yet been successfully completed. * `published` The volume is published successfully to the node. * `pending-node-unpublish` The volume should be unpublished from the node, and the manager is awaiting confirmation from the worker that it has done so. * `pending-controller-unpublish` The volume is successfully unpublished from the node, but has not yet been successfully unpublished on the controller.
 * @param publishContext A map of strings to strings returned by the CSI controller plugin when a volume is published.
 */
@Serializable
data class ClusterVolumePublishStatusInner(
    // The ID of the Swarm node the volume is published on.
    @SerialName(value = "NodeID") val nodeID: String? = null,
    // The published state of the volume. * `pending-publish` The volume should be published to this node, but the call to the controller plugin to do so has not yet been successfully completed. * `published` The volume is published successfully to the node. * `pending-node-unpublish` The volume should be unpublished from the node, and the manager is awaiting confirmation from the worker that it has done so. * `pending-controller-unpublish` The volume is successfully unpublished from the node, but has not yet been successfully unpublished on the controller.
    @SerialName(value = "State") val state: State? = null,
    // A map of strings to strings returned by the CSI controller plugin when a volume is published.
    @SerialName(value = "PublishContext") val publishContext: Map<String, String>? = null,
) {

    /**
     * The published state of the volume. * `pending-publish` The volume should be published to this node, but the call to the controller plugin to do so has not yet been successfully completed. * `published` The volume is published successfully to the node. * `pending-node-unpublish` The volume should be unpublished from the node, and the manager is awaiting confirmation from the worker that it has done so. * `pending-controller-unpublish` The volume is successfully unpublished from the node, but has not yet been successfully unpublished on the controller.
     *
     * Values: pendingMinusPublish,published,pendingMinusNodeMinusUnpublish,pendingMinusControllerMinusUnpublish
     */
    @Serializable
    enum class State(
        val value: String,
    ) {
        @SerialName(value = "pending-publish")
        PENDING_MINUS_PUBLISH("pending-publish"),

        @SerialName(value = "published")
        PUBLISHED("published"),

        @SerialName(value = "pending-node-unpublish")
        PENDING_MINUS_NODE_MINUS_UNPUBLISH("pending-node-unpublish"),

        @SerialName(value = "pending-controller-unpublish")
        PENDING_MINUS_CONTROLLER_MINUS_UNPUBLISH("pending-controller-unpublish"),
    }
}
