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
import io.github.xzima.docomagos.docker.models.ExecConfig
import io.github.xzima.docomagos.docker.models.ExecInspectResponse
import io.github.xzima.docomagos.docker.models.ExecStartConfig
import io.github.xzima.docomagos.docker.models.IdResponse
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.json.*

open class ExecApi : ApiClient {

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
     * Create an exec instance
     * Run a command inside a running container.
     * @param id ID or name of container
     * @param execConfig Exec configuration
     * @return IdResponse
     */
    open suspend fun containerExec(id: String, execConfig: ExecConfig): HttpResponse<IdResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = execConfig

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/containers/{id}/exec".replace("{" + "id" + "}", id),
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
     * Inspect an exec instance
     * Return low-level information about an exec instance.
     * @param id Exec instance ID
     * @return ExecInspectResponse
     */
    open suspend fun execInspect(id: String): HttpResponse<ExecInspectResponse> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/exec/{id}/json".replace("{" + "id" + "}", id),
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
     * Resize an exec instance
     * Resize the TTY session used by an exec instance. This endpoint only works if &#x60;tty&#x60; was specified as part of creating and starting the exec instance.
     * @param id Exec instance ID
     * @param h Height of the TTY session in characters
     * @param w Width of the TTY session in characters
     * @return void
     */
    open suspend fun execResize(id: String, h: Int, w: Int): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        h.apply { localVariableQuery["h"] = listOf("$h") }
        w.apply { localVariableQuery["w"] = listOf("$w") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/exec/{id}/resize".replace("{" + "id" + "}", id),
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
     * Start an exec instance
     * Starts a previously set up exec instance. If detach is true, this endpoint returns immediately after starting the command. Otherwise, it sets up an interactive session with the command.
     * @param id Exec instance ID
     * @param execStartConfig  (optional)
     * @return void
     */
    open suspend fun execStart(id: String, execStartConfig: ExecStartConfig? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = execStartConfig

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/exec/{id}/start".replace("{" + "id" + "}", id),
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
