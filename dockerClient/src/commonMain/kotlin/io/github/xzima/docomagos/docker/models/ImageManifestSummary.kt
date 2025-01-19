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
 * ImageManifestSummary represents a summary of an image manifest.
 *
 * @param ID ID is the content-addressable ID of an image and is the same as the digest of the image manifest.
 * @param descriptor
 * @param available Indicates whether all the child content (image config, layers) is fully available locally.
 * @param propertySize
 * @param kind The kind of the manifest.  kind         | description -------------|----------------------------------------------------------- image        | Image manifest that can be used to start a container. attestation  | Attestation manifest produced by the Buildkit builder for a specific image manifest.
 * @param imageData
 * @param attestationData
 */
@Serializable
data class ImageManifestSummary(
    // ID is the content-addressable ID of an image and is the same as the digest of the image manifest.
    @SerialName(value = "ID") @Required val ID: String,
    @SerialName(value = "Descriptor") @Required val descriptor: OCIDescriptor,
    // Indicates whether all the child content (image config, layers) is fully available locally.
    @SerialName(value = "Available") @Required val available: Boolean,
    @SerialName(value = "Size") @Required val propertySize: ImageManifestSummarySize,
    // The kind of the manifest.  kind         | description -------------|----------------------------------------------------------- image        | Image manifest that can be used to start a container. attestation  | Attestation manifest produced by the Buildkit builder for a specific image manifest.
    @SerialName(value = "Kind") @Required val kind: Kind,
    @SerialName(value = "ImageData") val imageData: ImageManifestSummaryImageData? = null,
    @SerialName(value = "AttestationData") val attestationData: ImageManifestSummaryAttestationData? = null,
) {

    /**
     * The kind of the manifest.  kind         | description -------------|----------------------------------------------------------- image        | Image manifest that can be used to start a container. attestation  | Attestation manifest produced by the Buildkit builder for a specific image manifest.
     *
     * Values: image,attestation,unknown
     */
    @Serializable
    enum class Kind(
        val value: String,
    ) {
        @SerialName(value = "image")
        IMAGE("image"),

        @SerialName(value = "attestation")
        ATTESTATION("attestation"),

        @SerialName(value = "unknown")
        UNKNOWN("unknown"),
    }
}
