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
import io.github.xzima.docomagos.docker.infrastructure.wrap
import io.github.xzima.docomagos.docker.models.ContainerCreateRequest
import io.github.xzima.docomagos.docker.models.ContainerCreateResponse
import io.github.xzima.docomagos.docker.models.ContainerInspectResponse
import io.github.xzima.docomagos.docker.models.ContainerPruneResponse
import io.github.xzima.docomagos.docker.models.ContainerSummary
import io.github.xzima.docomagos.docker.models.ContainerTopResponse
import io.github.xzima.docomagos.docker.models.ContainerUpdateRequest
import io.github.xzima.docomagos.docker.models.ContainerUpdateResponse
import io.github.xzima.docomagos.docker.models.ContainerWaitResponse
import io.github.xzima.docomagos.docker.models.FilesystemChange
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class ContainerApi : ApiClient {

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
     * Get an archive of a filesystem resource in a container
     * Get a tar archive of a resource in the filesystem of container id.
     * @param id ID or name of the container
     * @param path Resource in the container’s filesystem to archive.
     * @return void
     */
    open suspend fun containerArchive(id: String, path: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        path.apply { localVariableQuery["path"] = listOf(path) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/archive".replace("{" + "id" + "}", id),
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
     * Get information about files in a container
     * A response header &#x60;X-Docker-Container-Path-Stat&#x60; is returned, containing a base64 - encoded JSON object with some filesystem header information about the path.
     * @param id ID or name of the container
     * @param path Resource in the container’s filesystem to archive.
     * @return void
     */
    open suspend fun containerArchiveInfo(id: String, path: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        path.apply { localVariableQuery["path"] = listOf(path) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.HEAD,
            "/containers/{id}/archive".replace("{" + "id" + "}", id),
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
     * Attach to a container
     * Attach to a container to read its output or send it input. You can attach to the same container multiple times, and you can reattach to containers that have been detached.  Either the &#x60;stream&#x60; or &#x60;logs&#x60; parameter must be &#x60;true&#x60; for this endpoint to do anything.  See the [documentation for the &#x60;docker attach&#x60; command](https://docs.docker.com/engine/reference/commandline/attach/) for more details.  ### Hijacking  This endpoint hijacks the HTTP connection to transport &#x60;stdin&#x60;, &#x60;stdout&#x60;, and &#x60;stderr&#x60; on the same socket.  This is the response from the daemon for an attach request:  &#x60;&#x60;&#x60; HTTP/1.1 200 OK Content-Type: application/vnd.docker.raw-stream  STREAM &#x60;&#x60;&#x60;  After the headers and two new lines, the TCP connection can now be used for raw, bidirectional communication between the client and server.  To hint potential proxies about connection hijacking, the Docker client can also optionally send connection upgrade headers.  For example, the client sends this request to upgrade the connection:  &#x60;&#x60;&#x60; POST /containers/16253994b7c4/attach?stream&#x3D;1&amp;stdout&#x3D;1 HTTP/1.1 Upgrade: tcp Connection: Upgrade &#x60;&#x60;&#x60;  The Docker daemon will respond with a &#x60;101 UPGRADED&#x60; response, and will similarly follow with the raw stream:  &#x60;&#x60;&#x60; HTTP/1.1 101 UPGRADED Content-Type: application/vnd.docker.raw-stream Connection: Upgrade Upgrade: tcp  STREAM &#x60;&#x60;&#x60;  ### Stream format  When the TTY setting is disabled in [&#x60;POST /containers/create&#x60;](#operation/ContainerCreate), the HTTP Content-Type header is set to application/vnd.docker.multiplexed-stream and the stream over the hijacked connected is multiplexed to separate out &#x60;stdout&#x60; and &#x60;stderr&#x60;. The stream consists of a series of frames, each containing a header and a payload.  The header contains the information which the stream writes (&#x60;stdout&#x60; or &#x60;stderr&#x60;). It also contains the size of the associated frame encoded in the last four bytes (&#x60;uint32&#x60;).  It is encoded on the first eight bytes like this:  &#x60;&#x60;&#x60;go header :&#x3D; [8]byte{STREAM_TYPE, 0, 0, 0, SIZE1, SIZE2, SIZE3, SIZE4} &#x60;&#x60;&#x60;  &#x60;STREAM_TYPE&#x60; can be:  - 0: &#x60;stdin&#x60; (is written on &#x60;stdout&#x60;) - 1: &#x60;stdout&#x60; - 2: &#x60;stderr&#x60;  &#x60;SIZE1, SIZE2, SIZE3, SIZE4&#x60; are the four bytes of the &#x60;uint32&#x60; size encoded as big endian.  Following the header is the payload, which is the specified number of bytes of &#x60;STREAM_TYPE&#x60;.  The simplest way to implement this protocol is the following:  1. Read 8 bytes. 2. Choose &#x60;stdout&#x60; or &#x60;stderr&#x60; depending on the first byte. 3. Extract the frame size from the last four bytes. 4. Read the extracted size and output it on the correct output. 5. Goto 1.  ### Stream format when using a TTY  When the TTY setting is enabled in [&#x60;POST /containers/create&#x60;](#operation/ContainerCreate), the stream is not multiplexed. The data exchanged over the hijacked connection is simply the raw data from the process PTY and client&#39;s &#x60;stdin&#x60;.
     * @param id ID or name of the container
     * @param detachKeys Override the key sequence for detaching a container.Format is a single character &#x60;[a-Z]&#x60; or &#x60;ctrl-&lt;value&gt;&#x60; where &#x60;&lt;value&gt;&#x60; is one of: &#x60;a-z&#x60;, &#x60;@&#x60;, &#x60;^&#x60;, &#x60;[&#x60;, &#x60;,&#x60; or &#x60;_&#x60;.  (optional)
     * @param logs Replay previous logs from the container.  This is useful for attaching to a container that has started and you want to output everything since the container started.  If &#x60;stream&#x60; is also enabled, once all the previous output has been returned, it will seamlessly transition into streaming current output.  (optional, default to false)
     * @param stream Stream attached streams from the time the request was made onwards.  (optional, default to false)
     * @param stdin Attach to &#x60;stdin&#x60; (optional, default to false)
     * @param stdout Attach to &#x60;stdout&#x60; (optional, default to false)
     * @param stderr Attach to &#x60;stderr&#x60; (optional, default to false)
     * @return void
     */
    open suspend fun containerAttach(
        id: String,
        detachKeys: String? = null,
        logs: Boolean? = false,
        stream: Boolean? = false,
        stdin: Boolean? = false,
        stdout: Boolean? = false,
        stderr: Boolean? = false,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        detachKeys?.apply { localVariableQuery["detachKeys"] = listOf(detachKeys) }
        logs?.apply { localVariableQuery["logs"] = listOf("$logs") }
        stream?.apply { localVariableQuery["stream"] = listOf("$stream") }
        stdin?.apply { localVariableQuery["stdin"] = listOf("$stdin") }
        stdout?.apply { localVariableQuery["stdout"] = listOf("$stdout") }
        stderr?.apply { localVariableQuery["stderr"] = listOf("$stderr") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/attach".replace("{" + "id" + "}", id),
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
     * Attach to a container via a websocket
     *
     * @param id ID or name of the container
     * @param detachKeys Override the key sequence for detaching a container.Format is a single character &#x60;[a-Z]&#x60; or &#x60;ctrl-&lt;value&gt;&#x60; where &#x60;&lt;value&gt;&#x60; is one of: &#x60;a-z&#x60;, &#x60;@&#x60;, &#x60;^&#x60;, &#x60;[&#x60;, &#x60;,&#x60;, or &#x60;_&#x60;.  (optional)
     * @param logs Return logs (optional, default to false)
     * @param stream Return stream (optional, default to false)
     * @param stdin Attach to &#x60;stdin&#x60; (optional, default to false)
     * @param stdout Attach to &#x60;stdout&#x60; (optional, default to false)
     * @param stderr Attach to &#x60;stderr&#x60; (optional, default to false)
     * @return void
     */
    open suspend fun containerAttachWebsocket(
        id: String,
        detachKeys: String? = null,
        logs: Boolean? = false,
        stream: Boolean? = false,
        stdin: Boolean? = false,
        stdout: Boolean? = false,
        stderr: Boolean? = false,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        detachKeys?.apply { localVariableQuery["detachKeys"] = listOf(detachKeys) }
        logs?.apply { localVariableQuery["logs"] = listOf("$logs") }
        stream?.apply { localVariableQuery["stream"] = listOf("$stream") }
        stdin?.apply { localVariableQuery["stdin"] = listOf("$stdin") }
        stdout?.apply { localVariableQuery["stdout"] = listOf("$stdout") }
        stderr?.apply { localVariableQuery["stderr"] = listOf("$stderr") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/attach/ws".replace("{" + "id" + "}", id),
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
     * Get changes on a container’s filesystem
     * Returns which files in a container&#39;s filesystem have been added, deleted, or modified. The &#x60;Kind&#x60; of modification can be one of:  - &#x60;0&#x60;: Modified (\&quot;C\&quot;) - &#x60;1&#x60;: Added (\&quot;A\&quot;) - &#x60;2&#x60;: Deleted (\&quot;D\&quot;)
     * @param id ID or name of the container
     * @return kotlin.collections.List<FilesystemChange>
     */
    open suspend fun containerChanges(id: String): HttpResponse<List<FilesystemChange>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/changes".replace("{" + "id" + "}", id),
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ContainerChangesResponse>().map { value }
    }

    @Serializable(ContainerChangesResponse.Companion::class)
    private class ContainerChangesResponse(
        val value: List<FilesystemChange>,
    ) {
        companion object : KSerializer<ContainerChangesResponse> {
            private val serializer: KSerializer<List<FilesystemChange>> = serializer<List<FilesystemChange>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ContainerChangesResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ContainerChangesResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Create a container
     *
     * @param body Container to create
     * @param name Assign the specified name to the container. Must match &#x60;/?[a-zA-Z0-9][a-zA-Z0-9_.-]+&#x60;.  (optional)
     * @param platform Platform in the format &#x60;os[/arch[/variant]]&#x60; used for image lookup.  When specified, the daemon checks if the requested image is present in the local image cache with the given OS and Architecture, and otherwise returns a &#x60;404&#x60; status.  If the option is not set, the host&#39;s native OS and Architecture are used to look up the image in the image cache. However, if no platform is passed and the given image does exist in the local image cache, but its OS or architecture does not match, the container is created with the available image, and a warning is added to the &#x60;Warnings&#x60; field in the response, for example;      WARNING: The requested image&#39;s platform (linux/arm64/v8) does not              match the detected host platform (linux/amd64) and no              specific platform was requested  (optional)
     * @return ContainerCreateResponse
     */
    open suspend fun containerCreate(
        body: ContainerCreateRequest,
        name: String? = null,
        platform: String? = null,
    ): HttpResponse<ContainerCreateResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        name?.apply { localVariableQuery["name"] = listOf(name) }
        platform?.apply { localVariableQuery["platform"] = listOf(platform) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/create",
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
     * Remove a container
     *
     * @param id ID or name of the container
     * @param v Remove anonymous volumes associated with the container. (optional, default to false)
     * @param force If the container is running, kill it before removing it. (optional, default to false)
     * @param link Remove the specified link associated with the container. (optional, default to false)
     * @return void
     */
    open suspend fun containerDelete(
        id: String,
        v: Boolean? = false,
        force: Boolean? = false,
        link: Boolean? = false,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        v?.apply { localVariableQuery["v"] = listOf("$v") }
        force?.apply { localVariableQuery["force"] = listOf("$force") }
        link?.apply { localVariableQuery["link"] = listOf("$link") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/containers/{id}".replace("{" + "id" + "}", id),
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
     * Export a container
     * Export the contents of a container as a tarball.
     * @param id ID or name of the container
     * @return void
     */
    open suspend fun containerExport(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/export".replace("{" + "id" + "}", id),
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
     * Inspect a container
     * Return low-level information about a container.
     * @param id ID or name of the container
     * @param size Return the size of container as fields &#x60;SizeRw&#x60; and &#x60;SizeRootFs&#x60; (optional, default to false)
     * @return ContainerInspectResponse
     */
    open suspend fun containerInspect(id: String, size: Boolean? = false): HttpResponse<ContainerInspectResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        size?.apply { localVariableQuery["size"] = listOf("$size") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/json".replace("{" + "id" + "}", id),
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
     * Kill a container
     * Send a POSIX signal to a container, defaulting to killing to the container.
     * @param id ID or name of the container
     * @param signal Signal to send to the container as an integer or string (e.g. &#x60;SIGINT&#x60;).  (optional, default to "SIGKILL")
     * @return void
     */
    open suspend fun containerKill(id: String, signal: String? = "SIGKILL"): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        signal?.apply { localVariableQuery["signal"] = listOf(signal) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/kill".replace("{" + "id" + "}", id),
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
     * List containers
     * Returns a list of containers. For details on the format, see the [inspect endpoint](#operation/ContainerInspect).  Note that it uses a different, smaller representation of a container than inspecting a single container. For example, the list of linked containers is not propagated .
     * @param all Return all containers. By default, only running containers are shown.  (optional, default to false)
     * @param limit Return this number of most recently created containers, including non-running ones.  (optional)
     * @param size Return the size of container as fields &#x60;SizeRw&#x60; and &#x60;SizeRootFs&#x60;.  (optional, default to false)
     * @param filters Filters to process on the container list, encoded as JSON (a &#x60;map[string][]string&#x60;). For example, &#x60;{\&quot;status\&quot;: [\&quot;paused\&quot;]}&#x60; will only return paused containers.  Available filters:  - &#x60;ancestor&#x60;&#x3D;(&#x60;&lt;image-name&gt;[:&lt;tag&gt;]&#x60;, &#x60;&lt;image id&gt;&#x60;, or &#x60;&lt;image@digest&gt;&#x60;) - &#x60;before&#x60;&#x3D;(&#x60;&lt;container id&gt;&#x60; or &#x60;&lt;container name&gt;&#x60;) - &#x60;expose&#x60;&#x3D;(&#x60;&lt;port&gt;[/&lt;proto&gt;]&#x60;|&#x60;&lt;startport-endport&gt;/[&lt;proto&gt;]&#x60;) - &#x60;exited&#x3D;&lt;int&gt;&#x60; containers with exit code of &#x60;&lt;int&gt;&#x60; - &#x60;health&#x60;&#x3D;(&#x60;starting&#x60;|&#x60;healthy&#x60;|&#x60;unhealthy&#x60;|&#x60;none&#x60;) - &#x60;id&#x3D;&lt;ID&gt;&#x60; a container&#39;s ID - &#x60;isolation&#x3D;&#x60;(&#x60;default&#x60;|&#x60;process&#x60;|&#x60;hyperv&#x60;) (Windows daemon only) - &#x60;is-task&#x3D;&#x60;(&#x60;true&#x60;|&#x60;false&#x60;) - &#x60;label&#x3D;key&#x60; or &#x60;label&#x3D;\&quot;key&#x3D;value\&quot;&#x60; of a container label - &#x60;name&#x3D;&lt;name&gt;&#x60; a container&#39;s name - &#x60;network&#x60;&#x3D;(&#x60;&lt;network id&gt;&#x60; or &#x60;&lt;network name&gt;&#x60;) - &#x60;publish&#x60;&#x3D;(&#x60;&lt;port&gt;[/&lt;proto&gt;]&#x60;|&#x60;&lt;startport-endport&gt;/[&lt;proto&gt;]&#x60;) - &#x60;since&#x60;&#x3D;(&#x60;&lt;container id&gt;&#x60; or &#x60;&lt;container name&gt;&#x60;) - &#x60;status&#x3D;&#x60;(&#x60;created&#x60;|&#x60;restarting&#x60;|&#x60;running&#x60;|&#x60;removing&#x60;|&#x60;paused&#x60;|&#x60;exited&#x60;|&#x60;dead&#x60;) - &#x60;volume&#x60;&#x3D;(&#x60;&lt;volume name&gt;&#x60; or &#x60;&lt;mount point destination&gt;&#x60;)  (optional)
     * @return kotlin.collections.List<ContainerSummary>
     */
    open suspend fun containerList(
        all: Boolean? = false,
        limit: Int? = null,
        size: Boolean? = false,
        filters: String? = null,
    ): HttpResponse<List<ContainerSummary>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        all?.apply { localVariableQuery["all"] = listOf("$all") }
        limit?.apply { localVariableQuery["limit"] = listOf("$limit") }
        size?.apply { localVariableQuery["size"] = listOf("$size") }
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/json",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ContainerListResponse>().map { value }
    }

    @Serializable(ContainerListResponse.Companion::class)
    private class ContainerListResponse(
        val value: List<ContainerSummary>,
    ) {
        companion object : KSerializer<ContainerListResponse> {
            private val serializer: KSerializer<List<ContainerSummary>> = serializer<List<ContainerSummary>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ContainerListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ContainerListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Get container logs
     * Get &#x60;stdout&#x60; and &#x60;stderr&#x60; logs from a container.  Note: This endpoint works only for containers with the &#x60;json-file&#x60; or &#x60;journald&#x60; logging driver.
     * @param id ID or name of the container
     * @param follow Keep connection after returning logs. (optional, default to false)
     * @param stdout Return logs from &#x60;stdout&#x60; (optional, default to false)
     * @param stderr Return logs from &#x60;stderr&#x60; (optional, default to false)
     * @param since Only return logs since this time, as a UNIX timestamp (optional, default to 0)
     * @param until Only return logs before this time, as a UNIX timestamp (optional, default to 0)
     * @param timestamps Add timestamps to every log line (optional, default to false)
     * @param tail Only return this number of log lines from the end of the logs. Specify as an integer or &#x60;all&#x60; to output all log lines.  (optional, default to "all")
     * @return io.github.xzima.docomagos.docker.infrastructure.OctetByteArray
     */
    open suspend fun containerLogs(
        id: String,
        follow: Boolean? = false,
        stdout: Boolean? = false,
        stderr: Boolean? = false,
        since: Int? = 0,
        until: Int? = 0,
        timestamps: Boolean? = false,
        tail: String? = "all",
    ): HttpResponse<io.github.xzima.docomagos.docker.infrastructure.OctetByteArray> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        follow?.apply { localVariableQuery["follow"] = listOf("$follow") }
        stdout?.apply { localVariableQuery["stdout"] = listOf("$stdout") }
        stderr?.apply { localVariableQuery["stderr"] = listOf("$stderr") }
        since?.apply { localVariableQuery["since"] = listOf("$since") }
        until?.apply { localVariableQuery["until"] = listOf("$until") }
        timestamps?.apply { localVariableQuery["timestamps"] = listOf("$timestamps") }
        tail?.apply { localVariableQuery["tail"] = listOf(tail) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/logs".replace("{" + "id" + "}", id),
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
     * Pause a container
     * Use the freezer cgroup to suspend all processes in a container.  Traditionally, when suspending a process the &#x60;SIGSTOP&#x60; signal is used, which is observable by the process being suspended. With the freezer cgroup the process is unaware, and unable to capture, that it is being suspended, and subsequently resumed.
     * @param id ID or name of the container
     * @return void
     */
    open suspend fun containerPause(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/pause".replace("{" + "id" + "}", id),
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
     * Delete stopped containers
     *
     * @param filters Filters to process on the prune list, encoded as JSON (a &#x60;map[string][]string&#x60;).  Available filters: - &#x60;until&#x3D;&lt;timestamp&gt;&#x60; Prune containers created before this timestamp. The &#x60;&lt;timestamp&gt;&#x60; can be Unix timestamps, date formatted timestamps, or Go duration strings (e.g. &#x60;10m&#x60;, &#x60;1h30m&#x60;) computed relative to the daemon machine’s time. - &#x60;label&#x60; (&#x60;label&#x3D;&lt;key&gt;&#x60;, &#x60;label&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;, &#x60;label!&#x3D;&lt;key&gt;&#x60;, or &#x60;label!&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;) Prune containers with (or without, in case &#x60;label!&#x3D;...&#x60; is used) the specified labels.  (optional)
     * @return ContainerPruneResponse
     */
    open suspend fun containerPrune(filters: String? = null): HttpResponse<ContainerPruneResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/prune",
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
     * Rename a container
     *
     * @param id ID or name of the container
     * @param name New name for the container
     * @return void
     */
    open suspend fun containerRename(id: String, name: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        name.apply { localVariableQuery["name"] = listOf(name) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/rename".replace("{" + "id" + "}", id),
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
     * Resize a container TTY
     * Resize the TTY for a container.
     * @param id ID or name of the container
     * @param h Height of the TTY session in characters
     * @param w Width of the TTY session in characters
     * @return void
     */
    open suspend fun containerResize(id: String, h: Int, w: Int): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        h.apply { localVariableQuery["h"] = listOf("$h") }
        w.apply { localVariableQuery["w"] = listOf("$w") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/resize".replace("{" + "id" + "}", id),
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
     * Restart a container
     *
     * @param id ID or name of the container
     * @param signal Signal to send to the container as an integer or string (e.g. &#x60;SIGINT&#x60;).  (optional)
     * @param t Number of seconds to wait before killing the container (optional)
     * @return void
     */
    open suspend fun containerRestart(id: String, signal: String? = null, t: Int? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        signal?.apply { localVariableQuery["signal"] = listOf(signal) }
        t?.apply { localVariableQuery["t"] = listOf("$t") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/restart".replace("{" + "id" + "}", id),
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
     * Start a container
     *
     * @param id ID or name of the container
     * @param detachKeys Override the key sequence for detaching a container. Format is a single character &#x60;[a-Z]&#x60; or &#x60;ctrl-&lt;value&gt;&#x60; where &#x60;&lt;value&gt;&#x60; is one of: &#x60;a-z&#x60;, &#x60;@&#x60;, &#x60;^&#x60;, &#x60;[&#x60;, &#x60;,&#x60; or &#x60;_&#x60;.  (optional)
     * @return void
     */
    open suspend fun containerStart(id: String, detachKeys: String? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        detachKeys?.apply { localVariableQuery["detachKeys"] = listOf(detachKeys) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/start".replace("{" + "id" + "}", id),
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
     * Get container stats based on resource usage
     * This endpoint returns a live stream of a container’s resource usage statistics.  The &#x60;precpu_stats&#x60; is the CPU statistic of the *previous* read, and is used to calculate the CPU usage percentage. It is not an exact copy of the &#x60;cpu_stats&#x60; field.  If either &#x60;precpu_stats.online_cpus&#x60; or &#x60;cpu_stats.online_cpus&#x60; is nil then for compatibility with older daemons the length of the corresponding &#x60;cpu_usage.percpu_usage&#x60; array should be used.  On a cgroup v2 host, the following fields are not set * &#x60;blkio_stats&#x60;: all fields other than &#x60;io_service_bytes_recursive&#x60; * &#x60;cpu_stats&#x60;: &#x60;cpu_usage.percpu_usage&#x60; * &#x60;memory_stats&#x60;: &#x60;max_usage&#x60; and &#x60;failcnt&#x60; Also, &#x60;memory_stats.stats&#x60; fields are incompatible with cgroup v1.  To calculate the values shown by the &#x60;stats&#x60; command of the docker cli tool the following formulas can be used: * used_memory &#x3D; &#x60;memory_stats.usage - memory_stats.stats.cache&#x60; * available_memory &#x3D; &#x60;memory_stats.limit&#x60; * Memory usage % &#x3D; &#x60;(used_memory / available_memory) * 100.0&#x60; * cpu_delta &#x3D; &#x60;cpu_stats.cpu_usage.total_usage - precpu_stats.cpu_usage.total_usage&#x60; * system_cpu_delta &#x3D; &#x60;cpu_stats.system_cpu_usage - precpu_stats.system_cpu_usage&#x60; * number_cpus &#x3D; &#x60;length(cpu_stats.cpu_usage.percpu_usage)&#x60; or &#x60;cpu_stats.online_cpus&#x60; * CPU usage % &#x3D; &#x60;(cpu_delta / system_cpu_delta) * number_cpus * 100.0&#x60;
     * @param id ID or name of the container
     * @param stream Stream the output. If false, the stats will be output once and then it will disconnect.  (optional, default to true)
     * @param oneShot Only get a single stat instead of waiting for 2 cycles. Must be used with &#x60;stream&#x3D;false&#x60;.  (optional, default to false)
     * @return kotlin.String
     */
    open suspend fun containerStats(
        id: String,
        stream: Boolean? = true,
        oneShot: Boolean? = false,
    ): HttpResponse<String> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        stream?.apply { localVariableQuery["stream"] = listOf("$stream") }
        oneShot?.apply { localVariableQuery["one-shot"] = listOf("$oneShot") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/stats".replace("{" + "id" + "}", id),
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
     * Stop a container
     *
     * @param id ID or name of the container
     * @param signal Signal to send to the container as an integer or string (e.g. &#x60;SIGINT&#x60;).  (optional)
     * @param t Number of seconds to wait before killing the container (optional)
     * @return void
     */
    open suspend fun containerStop(id: String, signal: String? = null, t: Int? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        signal?.apply { localVariableQuery["signal"] = listOf(signal) }
        t?.apply { localVariableQuery["t"] = listOf("$t") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/stop".replace("{" + "id" + "}", id),
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
     * List processes running inside a container
     * On Unix systems, this is done by running the &#x60;ps&#x60; command. This endpoint is not supported on Windows.
     * @param id ID or name of the container
     * @param psArgs The arguments to pass to &#x60;ps&#x60;. For example, &#x60;aux&#x60; (optional, default to "-ef")
     * @return ContainerTopResponse
     */
    open suspend fun containerTop(id: String, psArgs: String? = "-ef"): HttpResponse<ContainerTopResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        psArgs?.apply { localVariableQuery["ps_args"] = listOf(psArgs) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/containers/{id}/top".replace("{" + "id" + "}", id),
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
     * Unpause a container
     * Resume a container which has been paused.
     * @param id ID or name of the container
     * @return void
     */
    open suspend fun containerUnpause(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/unpause".replace("{" + "id" + "}", id),
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
     * Update a container
     * Change various configuration options of a container without having to recreate it.
     * @param id ID or name of the container
     * @param update
     * @return ContainerUpdateResponse
     */
    open suspend fun containerUpdate(
        id: String,
        update: ContainerUpdateRequest,
    ): HttpResponse<ContainerUpdateResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = update

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/update".replace("{" + "id" + "}", id),
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
     * enum for parameter condition
     */
    @Serializable
    enum class ConditionContainerWait(
        val value: String,
    ) {

        @SerialName(value = "not-running")
        NOT_MINUS_RUNNING("not-running"),

        @SerialName(value = "next-exit")
        NEXT_MINUS_EXIT("next-exit"),

        @SerialName(value = "removed")
        REMOVED("removed"),
    }

    /**
     * Wait for a container
     * Block until a container stops, then returns the exit code.
     * @param id ID or name of the container
     * @param condition Wait until a container state reaches the given condition.  Defaults to &#x60;not-running&#x60; if omitted or empty.  (optional, default to not-running)
     * @return ContainerWaitResponse
     */
    open suspend fun containerWait(
        id: String,
        condition: ConditionContainerWait? = ConditionContainerWait.NOT_MINUS_RUNNING,
    ): HttpResponse<ContainerWaitResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        condition?.apply { localVariableQuery["condition"] = listOf(condition.value) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/wait".replace("{" + "id" + "}", id),
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
     * Extract an archive of files or folders to a directory in a container
     * Upload a tar archive to be extracted to a path in the filesystem of container id. &#x60;path&#x60; parameter is asserted to be a directory. If it exists as a file, 400 error will be returned with message \&quot;not a directory\&quot;.
     * @param id ID or name of the container
     * @param path Path to a directory in the container to extract the archive’s contents into.
     * @param inputStream The input stream must be a tar archive compressed with one of the following algorithms: &#x60;identity&#x60; (no compression), &#x60;gzip&#x60;, &#x60;bzip2&#x60;, or &#x60;xz&#x60;.
     * @param noOverwriteDirNonDir If &#x60;1&#x60;, &#x60;true&#x60;, or &#x60;True&#x60; then it will be an error if unpacking the given content would cause an existing directory to be replaced with a non-directory and vice versa.  (optional)
     * @param copyUIDGID If &#x60;1&#x60;, &#x60;true&#x60;, then it will copy UID/GID maps to the dest file or dir  (optional)
     * @return void
     */
    open suspend fun putContainerArchive(
        id: String,
        path: String,
        inputStream: io.github.xzima.docomagos.docker.infrastructure.OctetByteArray,
        noOverwriteDirNonDir: String? = null,
        copyUIDGID: String? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = inputStream

        val localVariableQuery = mutableMapOf<String, List<String>>()
        path.apply { localVariableQuery["path"] = listOf(path) }
        noOverwriteDirNonDir?.apply { localVariableQuery["noOverwriteDirNonDir"] = listOf(noOverwriteDirNonDir) }
        copyUIDGID?.apply { localVariableQuery["copyUIDGID"] = listOf(copyUIDGID) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.PUT,
            "/containers/{id}/archive".replace("{" + "id" + "}", id),
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
}
