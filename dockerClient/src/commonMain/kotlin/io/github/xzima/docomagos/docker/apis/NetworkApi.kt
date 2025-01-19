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
import io.github.xzima.docomagos.docker.models.Network
import io.github.xzima.docomagos.docker.models.NetworkConnectRequest
import io.github.xzima.docomagos.docker.models.NetworkCreateRequest
import io.github.xzima.docomagos.docker.models.NetworkCreateResponse
import io.github.xzima.docomagos.docker.models.NetworkDisconnectRequest
import io.github.xzima.docomagos.docker.models.NetworkPruneResponse
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class NetworkApi : ApiClient {

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
     * Connect a container to a network
     * The network must be either a local-scoped network or a swarm-scoped network with the &#x60;attachable&#x60; option set. A network cannot be re-attached to a running container
     * @param id Network ID or name
     * @param container
     * @return void
     */
    open suspend fun networkConnect(id: String, container: NetworkConnectRequest): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = container

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/networks/{id}/connect".replace("{" + "id" + "}", id),
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
     * Create a network
     *
     * @param networkConfig Network configuration
     * @return NetworkCreateResponse
     */
    open suspend fun networkCreate(networkConfig: NetworkCreateRequest): HttpResponse<NetworkCreateResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = networkConfig

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/networks/create",
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
     * Remove a network
     *
     * @param id Network ID or name
     * @return void
     */
    open suspend fun networkDelete(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/networks/{id}".replace("{" + "id" + "}", id),
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
     * Disconnect a container from a network
     *
     * @param id Network ID or name
     * @param container
     * @return void
     */
    open suspend fun networkDisconnect(id: String, container: NetworkDisconnectRequest): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = container

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/networks/{id}/disconnect".replace("{" + "id" + "}", id),
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
     * Inspect a network
     *
     * @param id Network ID or name
     * @param verbose Detailed inspect output for troubleshooting (optional, default to false)
     * @param scope Filter the network by scope (swarm, global, or local) (optional)
     * @return Network
     */
    open suspend fun networkInspect(
        id: String,
        verbose: Boolean? = false,
        scope: String? = null,
    ): HttpResponse<Network> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        verbose?.apply { localVariableQuery["verbose"] = listOf("$verbose") }
        scope?.apply { localVariableQuery["scope"] = listOf(scope) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/networks/{id}".replace("{" + "id" + "}", id),
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
     * List networks
     * Returns a list of networks. For details on the format, see the [network inspect endpoint](#operation/NetworkInspect).  Note that it uses a different, smaller representation of a network than inspecting a single network. For example, the list of containers attached to the network is not propagated in API versions 1.28 and up.
     * @param filters JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the networks list.  Available filters:  - &#x60;dangling&#x3D;&lt;boolean&gt;&#x60; When set to &#x60;true&#x60; (or &#x60;1&#x60;), returns all    networks that are not in use by a container. When set to &#x60;false&#x60;    (or &#x60;0&#x60;), only networks that are in use by one or more    containers are returned. - &#x60;driver&#x3D;&lt;driver-name&gt;&#x60; Matches a network&#39;s driver. - &#x60;id&#x3D;&lt;network-id&gt;&#x60; Matches all or part of a network ID. - &#x60;label&#x3D;&lt;key&gt;&#x60; or &#x60;label&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60; of a network label. - &#x60;name&#x3D;&lt;network-name&gt;&#x60; Matches all or part of a network name. - &#x60;scope&#x3D;[\&quot;swarm\&quot;|\&quot;global\&quot;|\&quot;local\&quot;]&#x60; Filters networks by scope (&#x60;swarm&#x60;, &#x60;global&#x60;, or &#x60;local&#x60;). - &#x60;type&#x3D;[\&quot;custom\&quot;|\&quot;builtin\&quot;]&#x60; Filters networks by type. The &#x60;custom&#x60; keyword returns all user-defined networks.  (optional)
     * @return kotlin.collections.List<Network>
     */
    open suspend fun networkList(filters: String? = null): HttpResponse<List<Network>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/networks",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<NetworkListResponse>().map { value }
    }

    @Serializable(NetworkListResponse.Companion::class)
    private class NetworkListResponse(
        val value: List<Network>,
    ) {
        companion object : KSerializer<NetworkListResponse> {
            private val serializer: KSerializer<List<Network>> = serializer<List<Network>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: NetworkListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = NetworkListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Delete unused networks
     *
     * @param filters Filters to process on the prune list, encoded as JSON (a &#x60;map[string][]string&#x60;).  Available filters: - &#x60;until&#x3D;&lt;timestamp&gt;&#x60; Prune networks created before this timestamp. The &#x60;&lt;timestamp&gt;&#x60; can be Unix timestamps, date formatted timestamps, or Go duration strings (e.g. &#x60;10m&#x60;, &#x60;1h30m&#x60;) computed relative to the daemon machine’s time. - &#x60;label&#x60; (&#x60;label&#x3D;&lt;key&gt;&#x60;, &#x60;label&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;, &#x60;label!&#x3D;&lt;key&gt;&#x60;, or &#x60;label!&#x3D;&lt;key&gt;&#x3D;&lt;value&gt;&#x60;) Prune networks with (or without, in case &#x60;label!&#x3D;...&#x60; is used) the specified labels.  (optional)
     * @return NetworkPruneResponse
     */
    open suspend fun networkPrune(filters: String? = null): HttpResponse<NetworkPruneResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/networks/prune",
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
