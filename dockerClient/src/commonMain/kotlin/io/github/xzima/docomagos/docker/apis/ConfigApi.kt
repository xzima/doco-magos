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
import io.github.xzima.docomagos.docker.models.Config
import io.github.xzima.docomagos.docker.models.ConfigCreateRequest
import io.github.xzima.docomagos.docker.models.ConfigSpec
import io.github.xzima.docomagos.docker.models.IdResponse
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class ConfigApi : ApiClient {

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
     * Create a config
     *
     * @param body  (optional)
     * @return IdResponse
     */
    open suspend fun configCreate(body: ConfigCreateRequest? = null): HttpResponse<IdResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/configs/create",
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
     * Delete a config
     *
     * @param id ID of the config
     * @return void
     */
    open suspend fun configDelete(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/configs/{id}".replace("{" + "id" + "}", id),
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
     * Inspect a config
     *
     * @param id ID of the config
     * @return Config
     */
    open suspend fun configInspect(id: String): HttpResponse<Config> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/configs/{id}".replace("{" + "id" + "}", id),
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
     * List configs
     *
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the configs list.  Available filters:  - &#x60;id&#x3D;&lt;config id&gt;&#x60; - &#x60;label&#x3D;&lt;key&gt; or label&#x3D;&lt;key&gt;&#x3D;value&#x60; - &#x60;name&#x3D;&lt;config name&gt;&#x60; - &#x60;names&#x3D;&lt;config name&gt;&#x60;  (optional)
     * @return kotlin.collections.List<Config>
     */
    open suspend fun configList(filters: String? = null): HttpResponse<List<Config>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/configs",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ConfigListResponse>().map { value }
    }

    @Serializable(ConfigListResponse.Companion::class)
    private class ConfigListResponse(
        val value: List<Config>,
    ) {
        companion object : KSerializer<ConfigListResponse> {
            private val serializer: KSerializer<List<Config>> = serializer<List<Config>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ConfigListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ConfigListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Update a Config
     *
     * @param id The ID or name of the config
     * @param version The version number of the config object being updated. This is required to avoid conflicting writes.
     * @param body The spec of the config to update. Currently, only the Labels field can be updated. All other fields must remain unchanged from the [ConfigInspect endpoint](#operation/ConfigInspect) response values.  (optional)
     * @return void
     */
    open suspend fun configUpdate(id: String, version: Long, body: ConfigSpec? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        version.apply { localVariableQuery["version"] = listOf("$version") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/configs/{id}/update".replace("{" + "id" + "}", id),
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
