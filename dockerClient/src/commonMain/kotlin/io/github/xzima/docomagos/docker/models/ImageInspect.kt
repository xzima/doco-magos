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
 * Information about an image in the local image cache.
 *
 * @param id ID is the content-addressable ID of an image.  This identifier is a content-addressable digest calculated from the image's configuration (which includes the digests of layers used by the image).  Note that this digest differs from the `RepoDigests` below, which holds digests of image manifests that reference the image.
 * @param repoTags List of image names/tags in the local image cache that reference this image.  Multiple image tags can refer to the same image, and this list may be empty if no tags reference the image, in which case the image is \"untagged\", in which case it can still be referenced by its ID.
 * @param repoDigests List of content-addressable digests of locally available image manifests that the image is referenced from. Multiple manifests can refer to the same image.  These digests are usually only available if the image was either pulled from a registry, or if the image was pushed to a registry, which is when the manifest is generated and its digest calculated.
 * @param parent ID of the parent image.  Depending on how the image was created, this field may be empty and is only set for images that were built/created locally. This field is empty if the image was pulled from an image registry.
 * @param comment Optional message that was set when committing or importing the image.
 * @param created Date and time at which the image was created, formatted in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.  This information is only available if present in the image, and omitted otherwise.
 * @param dockerVersion The version of Docker that was used to build the image.  Depending on how the image was created, this field may be empty.
 * @param author Name of the author that was specified when committing the image, or as specified through MAINTAINER (deprecated) in the Dockerfile.
 * @param config
 * @param architecture Hardware CPU architecture that the image runs on.
 * @param variant CPU architecture variant (presently ARM-only).
 * @param os Operating System the image is built to run on.
 * @param osVersion Operating System version the image is built to run on (especially for Windows).
 * @param propertySize Total size of the image including all layers it is composed of.
 * @param virtualSize Total size of the image including all layers it is composed of.  Deprecated: this field is omitted in API v1.44, but kept for backward compatibility. Use Size instead.
 * @param graphDriver
 * @param rootFS
 * @param metadata
 */
@Serializable
data class ImageInspect(
    // ID is the content-addressable ID of an image.  This identifier is a content-addressable digest calculated from the image's configuration (which includes the digests of layers used by the image).  Note that this digest differs from the `RepoDigests` below, which holds digests of image manifests that reference the image.
    @SerialName(value = "Id") val id: String? = null,
    // List of image names/tags in the local image cache that reference this image.  Multiple image tags can refer to the same image, and this list may be empty if no tags reference the image, in which case the image is \"untagged\", in which case it can still be referenced by its ID.
    @SerialName(value = "RepoTags") val repoTags: List<String>? = null,
    // List of content-addressable digests of locally available image manifests that the image is referenced from. Multiple manifests can refer to the same image.  These digests are usually only available if the image was either pulled from a registry, or if the image was pushed to a registry, which is when the manifest is generated and its digest calculated.
    @SerialName(value = "RepoDigests") val repoDigests: List<String>? = null,
    // ID of the parent image.  Depending on how the image was created, this field may be empty and is only set for images that were built/created locally. This field is empty if the image was pulled from an image registry.
    @SerialName(value = "Parent") val parent: String? = null,
    // Optional message that was set when committing or importing the image.
    @SerialName(value = "Comment") val comment: String? = null,
    // Date and time at which the image was created, formatted in [RFC 3339](https://www.ietf.org/rfc/rfc3339.txt) format with nanoseconds.  This information is only available if present in the image, and omitted otherwise.
    @SerialName(value = "Created") val created: String? = null,
    // The version of Docker that was used to build the image.  Depending on how the image was created, this field may be empty.
    @SerialName(value = "DockerVersion") val dockerVersion: String? = null,
    // Name of the author that was specified when committing the image, or as specified through MAINTAINER (deprecated) in the Dockerfile.
    @SerialName(value = "Author") val author: String? = null,
    @SerialName(value = "Config") val config: ImageConfig? = null,
    // Hardware CPU architecture that the image runs on.
    @SerialName(value = "Architecture") val architecture: String? = null,
    // CPU architecture variant (presently ARM-only).
    @SerialName(value = "Variant") val variant: String? = null,
    // Operating System the image is built to run on.
    @SerialName(value = "Os") val os: String? = null,
    // Operating System version the image is built to run on (especially for Windows).
    @SerialName(value = "OsVersion") val osVersion: String? = null,
    // Total size of the image including all layers it is composed of.
    @SerialName(value = "Size") val propertySize: Long? = null,
    // Total size of the image including all layers it is composed of.  Deprecated: this field is omitted in API v1.44, but kept for backward compatibility. Use Size instead.
    @SerialName(value = "VirtualSize") val virtualSize: Long? = null,
    @SerialName(value = "GraphDriver") val graphDriver: DriverData? = null,
    @SerialName(value = "RootFS") val rootFS: ImageInspectRootFS? = null,
    @SerialName(value = "Metadata") val metadata: ImageInspectMetadata? = null,
)
