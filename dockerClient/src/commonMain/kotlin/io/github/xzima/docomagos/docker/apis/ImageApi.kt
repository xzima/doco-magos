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
package io.github.xzima.docomagos.docker.apis

import io.github.xzima.docomagos.docker.infrastructure.ApiClient
import io.github.xzima.docomagos.docker.infrastructure.HttpResponse
import io.github.xzima.docomagos.docker.infrastructure.RequestConfig
import io.github.xzima.docomagos.docker.infrastructure.RequestMethod
import io.github.xzima.docomagos.docker.infrastructure.map
import io.github.xzima.docomagos.docker.infrastructure.toMultiValue
import io.github.xzima.docomagos.docker.infrastructure.wrap
import io.github.xzima.docomagos.docker.models.BuildPruneResponse
import io.github.xzima.docomagos.docker.models.ContainerConfig
import io.github.xzima.docomagos.docker.models.HistoryResponseItem
import io.github.xzima.docomagos.docker.models.IdResponse
import io.github.xzima.docomagos.docker.models.ImageDeleteResponseItem
import io.github.xzima.docomagos.docker.models.ImageInspect
import io.github.xzima.docomagos.docker.models.ImagePruneResponse
import io.github.xzima.docomagos.docker.models.ImageSearchResponseItem
import io.github.xzima.docomagos.docker.models.ImageSummary
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class ImageApi : ApiClient {

    constructor(
        baseUrl: String = BASE_URL,
        httpClientEngine: HttpClientEngine? = null,
        httpClientConfig: ((HttpClientConfig<*>) -> Unit)? = null,
        jsonSerializer: Json = JSON_DEFAULT,
    ) : super(
        baseUrl = baseUrl,
        httpClientEngine = httpClientEngine,
        httpClientConfig = httpClientConfig,
        jsonBlock = jsonSerializer,
    )

    constructor(
        baseUrl: String,
        httpClient: HttpClient,
    ) : super(baseUrl = baseUrl, httpClient = httpClient)

    /**
     * Delete builder cache
     *
     * @param keepStorage Amount of disk space in bytes to keep for cache (optional)
     * @param all Remove all types of build cache (optional)
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the list of build cache objects.  Available filters:  - &#x60;until&#x3D;&lt;timestamp&gt;&#x60; remove cache older than &#x60;&lt;timestamp&gt;&#x60;. The &#x60;&lt;timestamp&gt;&#x60; can be Unix timestamps, date formatted timestamps, or Go duration strings (e.g. &#x60;10m&#x60;, &#x60;1h30m&#x60;) computed relative to the daemon&#39;s local time. - &#x60;id&#x3D;&lt;id&gt;&#x60; - &#x60;parent&#x3D;&lt;id&gt;&#x60; - &#x60;type&#x3D;&lt;string&gt;&#x60; - &#x60;description&#x3D;&lt;string&gt;&#x60; - &#x60;inuse&#x60; - &#x60;shared&#x60; - &#x60;private&#x60;  (optional)
     * @return BuildPruneResponse
     */
    open suspend fun buildPrune(
        keepStorage: Long? = null,
        all: Boolean? = null,
        filters: String? = null,
    ): HttpResponse<BuildPruneResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        keepStorage?.apply { localVariableQuery["keep-storage"] = listOf("$keepStorage") }
        all?.apply { localVariableQuery["all"] = listOf("$all") }
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/build/prune",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * enum for parameter contentType
     */
    @Serializable
    enum class ContentTypeImageBuild(
        val value: String,
    ) {

        @SerialName(value = "application/x-tar")
        APPLICATION_SLASH_XMINUS_TAR("application/x-tar"),
    }

    /**
     * enum for parameter version
     */
    @Serializable
    enum class VersionImageBuild(
        val value: String,
    ) {

        @SerialName(value = "1")
        V1("1"),

        @SerialName(value = "2")
        V2("2"),
    }

    /**
     * Build an image
     * Build an image from a tar archive with a &#x60;Dockerfile&#x60; in it.  The &#x60;Dockerfile&#x60; specifies how the image is built from the tar archive. It is typically in the archive&#39;s root, but can be at a different path or have a different name by specifying the &#x60;dockerfile&#x60; parameter. [See the &#x60;Dockerfile&#x60; reference for more information](https://docs.docker.com/engine/reference/builder/).  The Docker daemon performs a preliminary validation of the &#x60;Dockerfile&#x60; before starting the build, and returns an error if the syntax is incorrect. After that, each instruction is run one-by-one until the ID of the new image is output.  The build is canceled if the client drops the connection by quitting or being killed.
     * @param dockerfile Path within the build context to the &#x60;Dockerfile&#x60;. This is ignored if &#x60;remote&#x60; is specified and points to an external &#x60;Dockerfile&#x60;. (optional, default to "Dockerfile")
     * @param t A name and optional tag to apply to the image in the &#x60;name:tag&#x60; format. If you omit the tag the default &#x60;latest&#x60; value is assumed. You can provide several &#x60;t&#x60; parameters. (optional)
     * @param extrahosts Extra hosts to add to /etc/hosts (optional)
     * @param remote A Git repository URI or HTTP/HTTPS context URI. If the URI points to a single text file, the file’s contents are placed into a file called &#x60;Dockerfile&#x60; and the image is built from that file. If the URI points to a tarball, the file is downloaded by the daemon and the contents therein used as the context for the build. If the URI points to a tarball and the &#x60;dockerfile&#x60; parameter is also specified, there must be a file with the corresponding path inside the tarball. (optional)
     * @param q Suppress verbose build output. (optional, default to false)
     * @param nocache Do not use the cache when building the image. (optional, default to false)
     * @param cachefrom JSON array of images used for build cache resolution. (optional)
     * @param pull Attempt to pull the image even if an older image exists locally. (optional)
     * @param rm Remove intermediate containers after a successful build. (optional, default to true)
     * @param forcerm Always remove intermediate containers, even upon failure. (optional, default to false)
     * @param memory Set memory limit for build. (optional)
     * @param memswap Total memory (memory + swap). Set as &#x60;-1&#x60; to disable swap. (optional)
     * @param cpushares CPU shares (relative weight). (optional)
     * @param cpusetcpus CPUs in which to allow execution (e.g., &#x60;0-3&#x60;, &#x60;0,1&#x60;). (optional)
     * @param cpuperiod The length of a CPU period in microseconds. (optional)
     * @param cpuquota Microseconds of CPU time that the container can get in a CPU period. (optional)
     * @param buildargs JSON map of string pairs for build-time variables. Users pass these values at build-time. Docker uses the buildargs as the environment context for commands run via the &#x60;Dockerfile&#x60; RUN instruction, or for variable expansion in other &#x60;Dockerfile&#x60; instructions. This is not meant for passing secret values.  For example, the build arg &#x60;FOO&#x3D;bar&#x60; would become &#x60;{\&quot;FOO\&quot;:\&quot;bar\&quot;}&#x60; in JSON. This would result in the query parameter &#x60;buildargs&#x3D;{\&quot;FOO\&quot;:\&quot;bar\&quot;}&#x60;. Note that &#x60;{\&quot;FOO\&quot;:\&quot;bar\&quot;}&#x60; should be URI component encoded.  [Read more about the buildargs instruction.](https://docs.docker.com/engine/reference/builder/#arg)  (optional)
     * @param shmsize Size of &#x60;/dev/shm&#x60; in bytes. The size must be greater than 0. If omitted the system uses 64MB. (optional)
     * @param squash Squash the resulting images layers into a single layer. *(Experimental release only.)* (optional)
     * @param labels Arbitrary key/value labels to set on the image, as a JSON map of string pairs. (optional)
     * @param networkmode Sets the networking mode for the run commands during build. Supported standard values are: &#x60;bridge&#x60;, &#x60;host&#x60;, &#x60;none&#x60;, and &#x60;container:&lt;name|id&gt;&#x60;. Any other value is taken as a custom network&#39;s name or ID to which this container should connect to.  (optional)
     * @param contentType  (optional, default to application/x-tar)
     * @param xRegistryConfig This is a base64-encoded JSON object with auth configurations for multiple registries that a build may refer to.  The key is a registry URL, and the value is an auth configuration object, [as described in the authentication section](#section/Authentication). For example:  &#x60;&#x60;&#x60; {   \&quot;docker.example.com\&quot;: {     \&quot;username\&quot;: \&quot;janedoe\&quot;,     \&quot;password\&quot;: \&quot;hunter2\&quot;   },   \&quot;https://index.docker.io/v1/\&quot;: {     \&quot;username\&quot;: \&quot;mobydock\&quot;,     \&quot;password\&quot;: \&quot;conta1n3rize14\&quot;   } } &#x60;&#x60;&#x60;  Only the registry domain name (and port if not the default 443) are required. However, for legacy reasons, the Docker Hub registry must be specified with both a &#x60;https://&#x60; prefix and a &#x60;/v1/&#x60; suffix even though Docker will prefer to use the v2 registry API.  (optional)
     * @param platform Platform in the format os[/arch[/variant]] (optional)
     * @param target Target build stage (optional)
     * @param outputs BuildKit output configuration (optional)
     * @param version Version of the builder backend to use.  - &#x60;1&#x60; is the first generation classic (deprecated) builder in the Docker daemon (default) - &#x60;2&#x60; is [BuildKit](https://github.com/moby/buildkit)  (optional, default to 1)
     * @param inputStream A tar archive compressed with one of the following algorithms: identity (no compression), gzip, bzip2, xz. (optional)
     * @return void
     */
    open suspend fun imageBuild(
        dockerfile: String? = "Dockerfile",
        t: String? = null,
        extrahosts: String? = null,
        remote: String? = null,
        q: Boolean? = false,
        nocache: Boolean? = false,
        cachefrom: String? = null,
        pull: String? = null,
        rm: Boolean? = true,
        forcerm: Boolean? = false,
        memory: Int? = null,
        memswap: Int? = null,
        cpushares: Int? = null,
        cpusetcpus: String? = null,
        cpuperiod: Int? = null,
        cpuquota: Int? = null,
        buildargs: String? = null,
        shmsize: Int? = null,
        squash: Boolean? = null,
        labels: String? = null,
        networkmode: String? = null,
        contentType: ContentTypeImageBuild? = ContentTypeImageBuild.APPLICATION_SLASH_XMINUS_TAR,
        xRegistryConfig: String? = null,
        platform: String? = null,
        target: String? = null,
        outputs: String? = null,
        version: VersionImageBuild? = VersionImageBuild.V1,
        inputStream: io.github.xzima.docomagos.docker.infrastructure.OctetByteArray? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = inputStream

        val localVariableQuery = mutableMapOf<String, List<String>>()
        dockerfile?.apply { localVariableQuery["dockerfile"] = listOf(dockerfile) }
        t?.apply { localVariableQuery["t"] = listOf(t) }
        extrahosts?.apply { localVariableQuery["extrahosts"] = listOf(extrahosts) }
        remote?.apply { localVariableQuery["remote"] = listOf(remote) }
        q?.apply { localVariableQuery["q"] = listOf("$q") }
        nocache?.apply { localVariableQuery["nocache"] = listOf("$nocache") }
        cachefrom?.apply { localVariableQuery["cachefrom"] = listOf(cachefrom) }
        pull?.apply { localVariableQuery["pull"] = listOf(pull) }
        rm?.apply { localVariableQuery["rm"] = listOf("$rm") }
        forcerm?.apply { localVariableQuery["forcerm"] = listOf("$forcerm") }
        memory?.apply { localVariableQuery["memory"] = listOf("$memory") }
        memswap?.apply { localVariableQuery["memswap"] = listOf("$memswap") }
        cpushares?.apply { localVariableQuery["cpushares"] = listOf("$cpushares") }
        cpusetcpus?.apply { localVariableQuery["cpusetcpus"] = listOf(cpusetcpus) }
        cpuperiod?.apply { localVariableQuery["cpuperiod"] = listOf("$cpuperiod") }
        cpuquota?.apply { localVariableQuery["cpuquota"] = listOf("$cpuquota") }
        buildargs?.apply { localVariableQuery["buildargs"] = listOf(buildargs) }
        shmsize?.apply { localVariableQuery["shmsize"] = listOf("$shmsize") }
        squash?.apply { localVariableQuery["squash"] = listOf("$squash") }
        labels?.apply { localVariableQuery["labels"] = listOf(labels) }
        networkmode?.apply { localVariableQuery["networkmode"] = listOf(networkmode) }
        platform?.apply { localVariableQuery["platform"] = listOf(platform) }
        target?.apply { localVariableQuery["target"] = listOf(target) }
        outputs?.apply { localVariableQuery["outputs"] = listOf(outputs) }
        version?.apply { localVariableQuery["version"] = listOf(version.value) }
        val localVariableHeaders = mutableMapOf<String, String>()
        contentType?.apply { localVariableHeaders["Content-type"] = this.toString() }
        xRegistryConfig?.apply { localVariableHeaders["X-Registry-Config"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/build",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Create a new image from a container
     *
     * @param container The ID or name of the container to commit (optional)
     * @param repo Repository name for the created image (optional)
     * @param tag Tag name for the create image (optional)
     * @param comment Commit message (optional)
     * @param author Author of the image (e.g., &#x60;John Hannibal Smith &lt;hannibal@a-team.com&gt;&#x60;) (optional)
     * @param pause Whether to pause the container before committing (optional, default to true)
     * @param changes &#x60;Dockerfile&#x60; instructions to apply while committing (optional)
     * @param containerConfig The container configuration (optional)
     * @return IdResponse
     */
    open suspend fun imageCommit(
        container: String? = null,
        repo: String? = null,
        tag: String? = null,
        comment: String? = null,
        author: String? = null,
        pause: Boolean? = true,
        changes: String? = null,
        containerConfig: ContainerConfig? = null,
    ): HttpResponse<IdResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = containerConfig

        val localVariableQuery = mutableMapOf<String, List<String>>()
        container?.apply { localVariableQuery["container"] = listOf(container) }
        repo?.apply { localVariableQuery["repo"] = listOf(repo) }
        tag?.apply { localVariableQuery["tag"] = listOf(tag) }
        comment?.apply { localVariableQuery["comment"] = listOf(comment) }
        author?.apply { localVariableQuery["author"] = listOf(author) }
        pause?.apply { localVariableQuery["pause"] = listOf("$pause") }
        changes?.apply { localVariableQuery["changes"] = listOf(changes) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/commit",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Create an image
     * Pull or import an image.
     * @param fromImage Name of the image to pull. The name may include a tag or digest. This parameter may only be used when pulling an image. The pull is cancelled if the HTTP connection is closed. (optional)
     * @param fromSrc Source to import. The value may be a URL from which the image can be retrieved or &#x60;-&#x60; to read the image from the request body. This parameter may only be used when importing an image. (optional)
     * @param repo Repository name given to an image when it is imported. The repo may include a tag. This parameter may only be used when importing an image. (optional)
     * @param tag Tag or digest. If empty when pulling an image, this causes all tags for the given image to be pulled. (optional)
     * @param message Set commit message for imported image. (optional)
     * @param xRegistryAuth A base64url-encoded auth configuration.  Refer to the [authentication section](#section/Authentication) for details.  (optional)
     * @param changes Apply &#x60;Dockerfile&#x60; instructions to the image that is created, for example: &#x60;changes&#x3D;ENV DEBUG&#x3D;true&#x60;. Note that &#x60;ENV DEBUG&#x3D;true&#x60; should be URI component encoded.  Supported &#x60;Dockerfile&#x60; instructions: &#x60;CMD&#x60;|&#x60;ENTRYPOINT&#x60;|&#x60;ENV&#x60;|&#x60;EXPOSE&#x60;|&#x60;ONBUILD&#x60;|&#x60;USER&#x60;|&#x60;VOLUME&#x60;|&#x60;WORKDIR&#x60;  (optional)
     * @param platform Platform in the format os[/arch[/variant]].  When used in combination with the &#x60;fromImage&#x60; option, the daemon checks if the given image is present in the local image cache with the given OS and Architecture, and otherwise attempts to pull the image. If the option is not set, the host&#39;s native OS and Architecture are used. If the given image does not exist in the local image cache, the daemon attempts to pull the image with the host&#39;s native OS and Architecture. If the given image does exists in the local image cache, but its OS or architecture does not match, a warning is produced.  When used with the &#x60;fromSrc&#x60; option to import an image from an archive, this option sets the platform information for the imported image. If the option is not set, the host&#39;s native OS and Architecture are used for the imported image.  (optional)
     * @param inputImage Image content if the value &#x60;-&#x60; has been specified in fromSrc query parameter (optional)
     * @return void
     */
    open suspend fun imageCreate(
        fromImage: String? = null,
        fromSrc: String? = null,
        repo: String? = null,
        tag: String? = null,
        message: String? = null,
        xRegistryAuth: String? = null,
        changes: List<String>? = null,
        platform: String? = null,
        inputImage: String? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = inputImage

        val localVariableQuery = mutableMapOf<String, List<String>>()
        fromImage?.apply { localVariableQuery["fromImage"] = listOf(fromImage) }
        fromSrc?.apply { localVariableQuery["fromSrc"] = listOf(fromSrc) }
        repo?.apply { localVariableQuery["repo"] = listOf(repo) }
        tag?.apply { localVariableQuery["tag"] = listOf(tag) }
        message?.apply { localVariableQuery["message"] = listOf(message) }
        changes?.apply { localVariableQuery["changes"] = toMultiValue(this, "csv") }
        platform?.apply { localVariableQuery["platform"] = listOf(platform) }
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth?.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/images/create",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Remove an image
     * Remove an image, along with any untagged parent images that were referenced by that image.  Images can&#39;t be removed if they have descendant images, are being used by a running container or are being used by a build.
     * @param name Image name or ID
     * @param force Remove the image even if it is being used by stopped containers or has other tags (optional, default to false)
     * @param noprune Do not delete untagged parent images (optional, default to false)
     * @return kotlin.collections.List<ImageDeleteResponseItem>
     */
    open suspend fun imageDelete(
        name: String,
        force: Boolean? = false,
        noprune: Boolean? = false,
    ): HttpResponse<List<ImageDeleteResponseItem>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        force?.apply { localVariableQuery["force"] = listOf("$force") }
        noprune?.apply { localVariableQuery["noprune"] = listOf("$noprune") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/images/{name}".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ImageDeleteResponse>().map { value }
    }

    @Serializable(ImageDeleteResponse.Companion::class)
    private class ImageDeleteResponse(
        val value: List<ImageDeleteResponseItem>,
    ) {
        companion object : KSerializer<ImageDeleteResponse> {
            private val serializer: KSerializer<List<ImageDeleteResponseItem>> =
                serializer<List<ImageDeleteResponseItem>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ImageDeleteResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ImageDeleteResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Export an image
     * Get a tarball containing all images and metadata for a repository.  If &#x60;name&#x60; is a specific name and tag (e.g. &#x60;ubuntu:latest&#x60;), then only that image (and its parents) are returned. If &#x60;name&#x60; is an image ID, similarly only that image (and its parents) are returned, but with the exclusion of the &#x60;repositories&#x60; file in the tarball, as there were no image names referenced.  ### Image tarball format  An image tarball contains one directory per image layer (named using its long ID), each containing these files:  - &#x60;VERSION&#x60;: currently &#x60;1.0&#x60; - the file format version - &#x60;json&#x60;: detailed layer information, similar to &#x60;docker inspect layer_id&#x60; - &#x60;layer.tar&#x60;: A tarfile containing the filesystem changes in this layer  The &#x60;layer.tar&#x60; file contains &#x60;aufs&#x60; style &#x60;.wh..wh.aufs&#x60; files and directories for storing attribute changes and deletions.  If the tarball defines a repository, the tarball should also include a &#x60;repositories&#x60; file at the root that contains a list of repository and tag names mapped to layer IDs.  &#x60;&#x60;&#x60;json {   \&quot;hello-world\&quot;: {     \&quot;latest\&quot;: \&quot;565a9d68a73f6706862bfe8409a7f659776d4d60a8d096eb4a3cbce6999cc2a1\&quot;   } } &#x60;&#x60;&#x60;
     * @param name Image name or ID
     * @return io.github.xzima.docomagos.docker.infrastructure.OctetByteArray
     */
    open suspend fun imageGet(
        name: String,
    ): HttpResponse<io.github.xzima.docomagos.docker.infrastructure.OctetByteArray> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/{name}/get".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Export several images
     * Get a tarball containing all images and metadata for several image repositories.  For each value of the &#x60;names&#x60; parameter: if it is a specific name and tag (e.g. &#x60;ubuntu:latest&#x60;), then only that image (and its parents) are returned; if it is an image ID, similarly only that image (and its parents) are returned and there would be no names referenced in the &#39;repositories&#39; file for this image ID.  For details on the format, see the [export image endpoint](#operation/ImageGet).
     * @param names Image names to filter by (optional)
     * @return io.github.xzima.docomagos.docker.infrastructure.OctetByteArray
     */
    open suspend fun imageGetAll(
        names: List<String>? = null,
    ): HttpResponse<io.github.xzima.docomagos.docker.infrastructure.OctetByteArray> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        names?.apply { localVariableQuery["names"] = toMultiValue(this, "csv") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/get",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Get the history of an image
     * Return parent layers of an image.
     * @param name Image name or ID
     * @return kotlin.collections.List<HistoryResponseItem>
     */
    open suspend fun imageHistory(name: String): HttpResponse<List<HistoryResponseItem>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/{name}/history".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ImageHistoryResponse>().map { value }
    }

    @Serializable(ImageHistoryResponse.Companion::class)
    private class ImageHistoryResponse(
        val value: List<HistoryResponseItem>,
    ) {
        companion object : KSerializer<ImageHistoryResponse> {
            private val serializer: KSerializer<List<HistoryResponseItem>> = serializer<List<HistoryResponseItem>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ImageHistoryResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ImageHistoryResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Inspect an image
     * Return low-level information about an image.
     * @param name Image name or id
     * @return ImageInspect
     */
    open suspend fun imageInspect(name: String): HttpResponse<ImageInspect> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/{name}/json".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * List Images
     * Returns a list of images on the server. Note that it uses a different, smaller representation of an image than inspecting a single image.
     * @param all Show all images. Only images from a final layer (no children) are shown by default. (optional, default to false)
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the images list.  Available filters:  - &#x60;before&#x60;&#x3D;(&#x60;&lt;image-name&gt;[:&lt;tag&gt;]&#x60;,  &#x60;&lt;image id&gt;&#x60; or &#x60;&lt;image@digest&gt;&#x60;) - &#x60;dangling&#x3D;true&#x60; - &#x60;label&#x3D;key&#x60; or &#x60;label&#x3D;\&quot;key&#x3D;value\&quot;&#x60; of an image label - &#x60;reference&#x60;&#x3D;(&#x60;&lt;image-name&gt;[:&lt;tag&gt;]&#x60;) - &#x60;since&#x60;&#x3D;(&#x60;&lt;image-name&gt;[:&lt;tag&gt;]&#x60;,  &#x60;&lt;image id&gt;&#x60; or &#x60;&lt;image@digest&gt;&#x60;) - &#x60;until&#x3D;&lt;timestamp&gt;&#x60;  (optional)
     * @param sharedSize Compute and show shared size as a &#x60;SharedSize&#x60; field on each image. (optional, default to false)
     * @param digests Show digest information as a &#x60;RepoDigests&#x60; field on each image. (optional, default to false)
     * @param manifests Include &#x60;Manifests&#x60; in the image summary. (optional, default to false)
     * @return kotlin.collections.List<ImageSummary>
     */
    open suspend fun imageList(
        all: Boolean? = false,
        filters: String? = null,
        sharedSize: Boolean? = false,
        digests: Boolean? = false,
        manifests: Boolean? = false,
    ): HttpResponse<List<ImageSummary>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        all?.apply { localVariableQuery["all"] = listOf("$all") }
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        sharedSize?.apply { localVariableQuery["shared-size"] = listOf("$sharedSize") }
        digests?.apply { localVariableQuery["digests"] = listOf("$digests") }
        manifests?.apply { localVariableQuery["manifests"] = listOf("$manifests") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/json",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ImageListResponse>().map { value }
    }

    @Serializable(ImageListResponse.Companion::class)
    private class ImageListResponse(
        val value: List<ImageSummary>,
    ) {
        companion object : KSerializer<ImageListResponse> {
            private val serializer: KSerializer<List<ImageSummary>> = serializer<List<ImageSummary>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ImageListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ImageListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Import images
     * Load a set of images and tags into a repository.  For details on the format, see the [export image endpoint](#operation/ImageGet).
     * @param quiet Suppress progress details during load. (optional, default to false)
     * @param imagesTarball Tar archive containing images (optional)
     * @return void
     */
    open suspend fun imageLoad(
        quiet: Boolean? = false,
        imagesTarball: io.github.xzima.docomagos.docker.infrastructure.OctetByteArray? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = imagesTarball

        val localVariableQuery = mutableMapOf<String, List<String>>()
        quiet?.apply { localVariableQuery["quiet"] = listOf("$quiet") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/images/load",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return jsonRequest(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Delete unused images
     *
     * @param filters Filters to process on the prune list, encoded as JSON (a &#x60;map[string][]string&#x60;). Available filters:  - &#x60;dangling&#x3D;&lt;boolean&gt;&#x60; When set to &#x60;true&#x60; (or &#x60;1&#x60;), prune only    unused *and* untagged images. When set to &#x60;false&#x60;    (or &#x60;0&#x60;), all unused images are pruned. - &#x60;until&#x3D;&lt;string&gt;&#x60; Prune images created before this timestamp. The &#x60;&lt;timestamp&gt;&#x60; can be Unix timestamps, date formatted timestamps, or Go duration strings (e.g. &#x60;10m&#x60;, &#x60;1h30m&#x60;) computed relative to the daemon machine’s time. - &#x60;label&#x60; (&#x60;label&#x3D;&lt;key&gt;&#x60;, &#x60;label&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;, &#x60;label!&#x3D;&lt;key&gt;&#x60;, or &#x60;label!&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;) Prune images with (or without, in case &#x60;label!&#x3D;...&#x60; is used) the specified labels.  (optional)
     * @return ImagePruneResponse
     */
    open suspend fun imagePrune(filters: String? = null): HttpResponse<ImagePruneResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/images/prune",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Push an image
     * Push an image to a registry.  If you wish to push an image on to a private registry, that image must already have a tag which references the registry. For example, &#x60;registry.example.com/myimage:latest&#x60;.  The push is cancelled if the HTTP connection is closed.
     * @param name Name of the image to push. For example, &#x60;registry.example.com/myimage&#x60;. The image must be present in the local image store with the same name.  The name should be provided without tag; if a tag is provided, it is ignored. For example, &#x60;registry.example.com/myimage:latest&#x60; is considered equivalent to &#x60;registry.example.com/myimage&#x60;.  Use the &#x60;tag&#x60; parameter to specify the tag to push.
     * @param xRegistryAuth A base64url-encoded auth configuration.  Refer to the [authentication section](#section/Authentication) for details.
     * @param tag Tag of the image to push. For example, &#x60;latest&#x60;. If no tag is provided, all tags of the given image that are present in the local image store are pushed.  (optional)
     * @param platform JSON-encoded OCI platform to select the platform-variant to push. If not provided, all available variants will attempt to be pushed.  If the daemon provides a multi-platform image store, this selects the platform-variant to push to the registry. If the image is a single-platform image, or if the multi-platform image does not provide a variant matching the given platform, an error is returned.  Example: &#x60;{\&quot;os\&quot;: \&quot;linux\&quot;, \&quot;architecture\&quot;: \&quot;arm\&quot;, \&quot;variant\&quot;: \&quot;v5\&quot;}&#x60;  (optional)
     * @return void
     */
    open suspend fun imagePush(
        name: String,
        xRegistryAuth: String,
        tag: String? = null,
        platform: String? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        tag?.apply { localVariableQuery["tag"] = listOf(tag) }
        platform?.apply { localVariableQuery["platform"] = listOf(platform) }
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/images/{name}/push".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }

    /**
     * Search images
     * Search for an image on Docker Hub.
     * @param term Term to search
     * @param limit Maximum number of results to return (optional)
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the images list. Available filters:  - &#x60;is-official&#x3D;(true|false)&#x60; - &#x60;stars&#x3D;&lt;number&gt;&#x60; Matches images that has at least &#39;number&#39; stars.  (optional)
     * @return kotlin.collections.List<ImageSearchResponseItem>
     */
    open suspend fun imageSearch(
        term: String,
        limit: Int? = null,
        filters: String? = null,
    ): HttpResponse<List<ImageSearchResponseItem>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        term.apply { localVariableQuery["term"] = listOf(term) }
        limit?.apply { localVariableQuery["limit"] = listOf("$limit") }
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/images/search",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ImageSearchResponse>().map { value }
    }

    @Serializable(ImageSearchResponse.Companion::class)
    private class ImageSearchResponse(
        val value: List<ImageSearchResponseItem>,
    ) {
        companion object : KSerializer<ImageSearchResponse> {
            private val serializer: KSerializer<List<ImageSearchResponseItem>> =
                serializer<List<ImageSearchResponseItem>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ImageSearchResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ImageSearchResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Tag an image
     * Tag an image so that it becomes part of a repository.
     * @param name Image name or ID to tag.
     * @param repo The repository to tag in. For example, &#x60;someuser/someimage&#x60;. (optional)
     * @param tag The name of the new tag. (optional)
     * @return void
     */
    open suspend fun imageTag(name: String, repo: String? = null, tag: String? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        repo?.apply { localVariableQuery["repo"] = listOf(repo) }
        tag?.apply { localVariableQuery["tag"] = listOf(tag) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/images/{name}/tag".replace("{" + "name" + "}", name),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap()
    }
}
