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
import io.github.xzima.docomagos.docker.infrastructure.wrap
import io.github.xzima.docomagos.docker.models.Swarm
import io.github.xzima.docomagos.docker.models.SwarmInitRequest
import io.github.xzima.docomagos.docker.models.SwarmJoinRequest
import io.github.xzima.docomagos.docker.models.SwarmSpec
import io.github.xzima.docomagos.docker.models.SwarmUnlockRequest
import io.github.xzima.docomagos.docker.models.UnlockKeyResponse
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.*

open class SwarmApi : ApiClient {

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
     * Initialize a new swarm
     *
     * @param body
     * @return kotlin.String
     */
    open suspend fun swarmInit(body: SwarmInitRequest): HttpResponse<String> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/swarm/init",
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
     * Inspect swarm
     *
     * @return Swarm
     */
    open suspend fun swarmInspect(): HttpResponse<Swarm> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/swarm",
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
     * Join an existing swarm
     *
     * @param body
     * @return void
     */
    open suspend fun swarmJoin(body: SwarmJoinRequest): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/swarm/join",
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
     * Leave a swarm
     *
     * @param force Force leave swarm, even if this is the last manager or that it will break the cluster.  (optional, default to false)
     * @return void
     */
    open suspend fun swarmLeave(force: Boolean? = false): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        force?.apply { localVariableQuery["force"] = listOf("$force") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/swarm/leave",
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
     * Unlock a locked manager
     *
     * @param body
     * @return void
     */
    open suspend fun swarmUnlock(body: SwarmUnlockRequest): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/swarm/unlock",
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
     * Get the unlock key
     *
     * @return UnlockKeyResponse
     */
    open suspend fun swarmUnlockkey(): HttpResponse<UnlockKeyResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/swarm/unlockkey",
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
     * Update a swarm
     *
     * @param version The version number of the swarm object being updated. This is required to avoid conflicting writes.
     * @param body
     * @param rotateWorkerToken Rotate the worker join token. (optional, default to false)
     * @param rotateManagerToken Rotate the manager join token. (optional, default to false)
     * @param rotateManagerUnlockKey Rotate the manager unlock key. (optional, default to false)
     * @return void
     */
    open suspend fun swarmUpdate(
        version: Long,
        body: SwarmSpec,
        rotateWorkerToken: Boolean? = false,
        rotateManagerToken: Boolean? = false,
        rotateManagerUnlockKey: Boolean? = false,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        version.apply { localVariableQuery["version"] = listOf("$version") }
        rotateWorkerToken?.apply { localVariableQuery["rotateWorkerToken"] = listOf("$rotateWorkerToken") }
        rotateManagerToken?.apply { localVariableQuery["rotateManagerToken"] = listOf("$rotateManagerToken") }
        rotateManagerUnlockKey?.apply {
            localVariableQuery["rotateManagerUnlockKey"] = listOf("$rotateManagerUnlockKey")
        }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/swarm/update",
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
