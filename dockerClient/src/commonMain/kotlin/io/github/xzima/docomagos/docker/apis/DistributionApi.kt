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
import io.github.xzima.docomagos.docker.models.DistributionInspect
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.*

open class DistributionApi : ApiClient {

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
     * Get image information from the registry
     * Return image digest and platform information by contacting the registry.
     * @param name Image name or id
     * @return DistributionInspect
     */
    open suspend fun distributionInspect(name: String): HttpResponse<DistributionInspect> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/distribution/{name}/json".replace("{" + "name" + "}", name),
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
