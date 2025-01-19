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
import io.github.xzima.docomagos.docker.infrastructure.toMultiValue
import io.github.xzima.docomagos.docker.infrastructure.wrap
import io.github.xzima.docomagos.docker.models.AuthConfig
import io.github.xzima.docomagos.docker.models.EventMessage
import io.github.xzima.docomagos.docker.models.SystemAuthResponse
import io.github.xzima.docomagos.docker.models.SystemDataUsageResponse
import io.github.xzima.docomagos.docker.models.SystemInfo
import io.github.xzima.docomagos.docker.models.SystemVersion
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*

open class SystemApi : ApiClient {

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
     * Check auth configuration
     * Validate credentials for a registry and, if available, get an identity token for accessing the registry without password.
     * @param authConfig Authentication to check (optional)
     * @return SystemAuthResponse
     */
    open suspend fun systemAuth(authConfig: AuthConfig? = null): HttpResponse<SystemAuthResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = authConfig

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/auth",
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
     * enum for parameter type
     */
    @Serializable
    enum class TypeSystemDataUsage(
        val value: String,
    ) {

        @SerialName(value = "container")
        CONTAINER("container"),

        @SerialName(value = "image")
        IMAGE("image"),

        @SerialName(value = "volume")
        VOLUME("volume"),

        @SerialName(value = "build-cache")
        BUILD_MINUS_CACHE("build-cache"),
    }

    /**
     * Get data usage information
     *
     * @param type Object types, for which to compute and return data.  (optional)
     * @return SystemDataUsageResponse
     */
    open suspend fun systemDataUsage(type: List<TypeSystemDataUsage>? = null): HttpResponse<SystemDataUsageResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        type?.apply { localVariableQuery["type"] = toMultiValue(this, "multi") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/system/df",
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
     * Monitor events
     * Stream real-time events from the server.  Various objects within Docker report events when something happens to them.  Containers report these events: &#x60;attach&#x60;, &#x60;commit&#x60;, &#x60;copy&#x60;, &#x60;create&#x60;, &#x60;destroy&#x60;, &#x60;detach&#x60;, &#x60;die&#x60;, &#x60;exec_create&#x60;, &#x60;exec_detach&#x60;, &#x60;exec_start&#x60;, &#x60;exec_die&#x60;, &#x60;export&#x60;, &#x60;health_status&#x60;, &#x60;kill&#x60;, &#x60;oom&#x60;, &#x60;pause&#x60;, &#x60;rename&#x60;, &#x60;resize&#x60;, &#x60;restart&#x60;, &#x60;start&#x60;, &#x60;stop&#x60;, &#x60;top&#x60;, &#x60;unpause&#x60;, &#x60;update&#x60;, and &#x60;prune&#x60;  Images report these events: &#x60;create&#x60;, &#x60;delete&#x60;, &#x60;import&#x60;, &#x60;load&#x60;, &#x60;pull&#x60;, &#x60;push&#x60;, &#x60;save&#x60;, &#x60;tag&#x60;, &#x60;untag&#x60;, and &#x60;prune&#x60;  Volumes report these events: &#x60;create&#x60;, &#x60;mount&#x60;, &#x60;unmount&#x60;, &#x60;destroy&#x60;, and &#x60;prune&#x60;  Networks report these events: &#x60;create&#x60;, &#x60;connect&#x60;, &#x60;disconnect&#x60;, &#x60;destroy&#x60;, &#x60;update&#x60;, &#x60;remove&#x60;, and &#x60;prune&#x60;  The Docker daemon reports these events: &#x60;reload&#x60;  Services report these events: &#x60;create&#x60;, &#x60;update&#x60;, and &#x60;remove&#x60;  Nodes report these events: &#x60;create&#x60;, &#x60;update&#x60;, and &#x60;remove&#x60;  Secrets report these events: &#x60;create&#x60;, &#x60;update&#x60;, and &#x60;remove&#x60;  Configs report these events: &#x60;create&#x60;, &#x60;update&#x60;, and &#x60;remove&#x60;  The Builder reports &#x60;prune&#x60; events
     * @param since Show events created since this timestamp then stream new events. (optional)
     * @param until Show events created until this timestamp then stop streaming. (optional)
     * @param filters A JSON encoded value of filters (a &#x60;map[string][]string&#x60;) to process on the event list. Available filters:  - &#x60;config&#x3D;&lt;string&gt;&#x60; config name or ID - &#x60;container&#x3D;&lt;string&gt;&#x60; container name or ID - &#x60;daemon&#x3D;&lt;string&gt;&#x60; daemon name or ID - &#x60;event&#x3D;&lt;string&gt;&#x60; event type - &#x60;image&#x3D;&lt;string&gt;&#x60; image name or ID - &#x60;label&#x3D;&lt;string&gt;&#x60; image or container label - &#x60;network&#x3D;&lt;string&gt;&#x60; network name or ID - &#x60;node&#x3D;&lt;string&gt;&#x60; node ID - &#x60;plugin&#x60;&#x3D;&lt;string&gt; plugin name or ID - &#x60;scope&#x60;&#x3D;&lt;string&gt; local or swarm - &#x60;secret&#x3D;&lt;string&gt;&#x60; secret name or ID - &#x60;service&#x3D;&lt;string&gt;&#x60; service name or ID - &#x60;type&#x3D;&lt;string&gt;&#x60; object to filter by, one of &#x60;container&#x60;, &#x60;image&#x60;, &#x60;volume&#x60;, &#x60;network&#x60;, &#x60;daemon&#x60;, &#x60;plugin&#x60;, &#x60;node&#x60;, &#x60;service&#x60;, &#x60;secret&#x60; or &#x60;config&#x60; - &#x60;volume&#x3D;&lt;string&gt;&#x60; volume name  (optional)
     * @return EventMessage
     */
    open suspend fun systemEvents(
        since: String? = null,
        until: String? = null,
        filters: String? = null,
    ): HttpResponse<EventMessage> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        since?.apply { localVariableQuery["since"] = listOf(since) }
        until?.apply { localVariableQuery["until"] = listOf(until) }
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/events",
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
     * Get system information
     *
     * @return SystemInfo
     */
    open suspend fun systemInfo(): HttpResponse<SystemInfo> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/info",
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
     * Ping
     * This is a dummy endpoint you can use to test if the server is accessible.
     * @return kotlin.String
     */
    open suspend fun systemPing(): HttpResponse<String> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/_ping",
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
     * Ping
     * This is a dummy endpoint you can use to test if the server is accessible.
     * @return kotlin.String
     */
    open suspend fun systemPingHead(): HttpResponse<String> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.HEAD,
            "/_ping",
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
     * Get version
     * Returns the version of Docker that is running and various information about the system that Docker is running on.
     * @return SystemVersion
     */
    open suspend fun systemVersion(): HttpResponse<SystemVersion> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/version",
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
