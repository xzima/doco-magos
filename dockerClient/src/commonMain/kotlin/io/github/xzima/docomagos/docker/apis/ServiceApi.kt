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
import io.github.xzima.docomagos.docker.models.Service
import io.github.xzima.docomagos.docker.models.ServiceCreateRequest
import io.github.xzima.docomagos.docker.models.ServiceCreateResponse
import io.github.xzima.docomagos.docker.models.ServiceUpdateRequest
import io.github.xzima.docomagos.docker.models.ServiceUpdateResponse
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class ServiceApi : ApiClient {

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
     * Create a service
     *
     * @param body
     * @param xRegistryAuth A base64url-encoded auth configuration for pulling from private registries.  Refer to the [authentication section](#section/Authentication) for details.  (optional)
     * @return ServiceCreateResponse
     */
    open suspend fun serviceCreate(
        body: ServiceCreateRequest,
        xRegistryAuth: String? = null,
    ): HttpResponse<ServiceCreateResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth?.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/services/create",
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
     * Delete a service
     *
     * @param id ID or name of service.
     * @return void
     */
    open suspend fun serviceDelete(id: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/services/{id}".replace("{" + "id" + "}", id),
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
     * Inspect a service
     *
     * @param id ID or name of service.
     * @param insertDefaults Fill empty fields with default values. (optional, default to false)
     * @return Service
     */
    open suspend fun serviceInspect(id: String, insertDefaults: Boolean? = false): HttpResponse<Service> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        insertDefaults?.apply { localVariableQuery["insertDefaults"] = listOf("$insertDefaults") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/services/{id}".replace("{" + "id" + "}", id),
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
     * List services
     *
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the services list.  Available filters:  - &#x60;id&#x3D;&lt;service id&gt;&#x60; - &#x60;label&#x3D;&lt;service label&gt;&#x60; - &#x60;mode&#x3D;[\&quot;replicated\&quot;|\&quot;global\&quot;]&#x60; - &#x60;name&#x3D;&lt;service name&gt;&#x60;  (optional)
     * @param status Include service status, with count of running and desired tasks.  (optional)
     * @return kotlin.collections.List<Service>
     */
    open suspend fun serviceList(filters: String? = null, status: Boolean? = null): HttpResponse<List<Service>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        status?.apply { localVariableQuery["status"] = listOf("$status") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/services",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<ServiceListResponse>().map { value }
    }

    @Serializable(ServiceListResponse.Companion::class)
    private class ServiceListResponse(
        val value: List<Service>,
    ) {
        companion object : KSerializer<ServiceListResponse> {
            private val serializer: KSerializer<List<Service>> = serializer<List<Service>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: ServiceListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = ServiceListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Get service logs
     * Get &#x60;stdout&#x60; and &#x60;stderr&#x60; logs from a service. See also [&#x60;/containers/{id}/logs&#x60;](#operation/ContainerLogs).  **Note**: This endpoint works only for services with the &#x60;local&#x60;, &#x60;json-file&#x60; or &#x60;journald&#x60; logging drivers.
     * @param id ID or name of the service
     * @param details Show service context and extra details provided to logs. (optional, default to false)
     * @param follow Keep connection after returning logs. (optional, default to false)
     * @param stdout Return logs from &#x60;stdout&#x60; (optional, default to false)
     * @param stderr Return logs from &#x60;stderr&#x60; (optional, default to false)
     * @param since Only return logs since this time, as a UNIX timestamp (optional, default to 0)
     * @param timestamps Add timestamps to every log line (optional, default to false)
     * @param tail Only return this number of log lines from the end of the logs. Specify as an integer or &#x60;all&#x60; to output all log lines.  (optional, default to "all")
     * @return io.github.xzima.docomagos.docker.infrastructure.OctetByteArray
     */
    open suspend fun serviceLogs(
        id: String,
        details: Boolean? = false,
        follow: Boolean? = false,
        stdout: Boolean? = false,
        stderr: Boolean? = false,
        since: Int? = 0,
        timestamps: Boolean? = false,
        tail: String? = "all",
    ): HttpResponse<io.github.xzima.docomagos.docker.infrastructure.OctetByteArray> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        details?.apply { localVariableQuery["details"] = listOf("$details") }
        follow?.apply { localVariableQuery["follow"] = listOf("$follow") }
        stdout?.apply { localVariableQuery["stdout"] = listOf("$stdout") }
        stderr?.apply { localVariableQuery["stderr"] = listOf("$stderr") }
        since?.apply { localVariableQuery["since"] = listOf("$since") }
        timestamps?.apply { localVariableQuery["timestamps"] = listOf("$timestamps") }
        tail?.apply { localVariableQuery["tail"] = listOf(tail) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/services/{id}/logs".replace("{" + "id" + "}", id),
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
     * enum for parameter registryAuthFrom
     */
    @Serializable
    enum class RegistryAuthFromServiceUpdate(
        val value: String,
    ) {

        @SerialName(value = "spec")
        SPEC("spec"),

        @SerialName(value = "previous-spec")
        PREVIOUS_MINUS_SPEC("previous-spec"),
    }

    /**
     * Update a service
     *
     * @param id ID or name of service.
     * @param version The version number of the service object being updated. This is required to avoid conflicting writes. This version number should be the value as currently set on the service *before* the update. You can find the current version by calling &#x60;GET /services/{id}&#x60;
     * @param body
     * @param registryAuthFrom If the &#x60;X-Registry-Auth&#x60; header is not specified, this parameter indicates where to find registry authorization credentials.  (optional, default to spec)
     * @param rollback Set to this parameter to &#x60;previous&#x60; to cause a server-side rollback to the previous service spec. The supplied spec will be ignored in this case.  (optional)
     * @param xRegistryAuth A base64url-encoded auth configuration for pulling from private registries.  Refer to the [authentication section](#section/Authentication) for details.  (optional)
     * @return ServiceUpdateResponse
     */
    open suspend fun serviceUpdate(
        id: String,
        version: Int,
        body: ServiceUpdateRequest,
        registryAuthFrom: RegistryAuthFromServiceUpdate? = RegistryAuthFromServiceUpdate.SPEC,
        rollback: String? = null,
        xRegistryAuth: String? = null,
    ): HttpResponse<ServiceUpdateResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = body

        val localVariableQuery = mutableMapOf<String, List<String>>()
        version.apply { localVariableQuery["version"] = listOf("$version") }
        registryAuthFrom?.apply { localVariableQuery["registryAuthFrom"] = listOf(registryAuthFrom.value) }
        rollback?.apply { localVariableQuery["rollback"] = listOf(rollback) }
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth?.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/services/{id}/update".replace("{" + "id" + "}", id),
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
