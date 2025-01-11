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
import io.github.xzima.docomagos.docker.models.Task
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class TaskApi : ApiClient {

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
     * Inspect a task
     *
     * @param id ID of the task
     * @return Task
     */
    open suspend fun taskInspect(id: String): HttpResponse<Task> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/tasks/{id}".replace("{" + "id" + "}", id),
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
     * List tasks
     *
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the tasks list.  Available filters:  - &#x60;desired-state&#x3D;(running | shutdown | accepted)&#x60; - &#x60;id&#x3D;&lt;task id&gt;&#x60; - &#x60;label&#x3D;key&#x60; or &#x60;label&#x3D;\&quot;key&#x3D;value\&quot;&#x60; - &#x60;name&#x3D;&lt;task name&gt;&#x60; - &#x60;node&#x3D;&lt;node id or name&gt;&#x60; - &#x60;service&#x3D;&lt;service name&gt;&#x60;  (optional)
     * @return kotlin.collections.List<Task>
     */
    open suspend fun taskList(filters: String? = null): HttpResponse<List<Task>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/tasks",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<TaskListResponse>().map { value }
    }

    @Serializable(TaskListResponse.Companion::class)
    private class TaskListResponse(
        val value: List<Task>,
    ) {
        companion object : KSerializer<TaskListResponse> {
            private val serializer: KSerializer<List<Task>> = serializer<List<Task>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: TaskListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = TaskListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Get task logs
     * Get &#x60;stdout&#x60; and &#x60;stderr&#x60; logs from a task. See also [&#x60;/containers/{id}/logs&#x60;](#operation/ContainerLogs).  **Note**: This endpoint works only for services with the &#x60;local&#x60;, &#x60;json-file&#x60; or &#x60;journald&#x60; logging drivers.
     * @param id ID of the task
     * @param details Show task context and extra details provided to logs. (optional, default to false)
     * @param follow Keep connection after returning logs. (optional, default to false)
     * @param stdout Return logs from &#x60;stdout&#x60; (optional, default to false)
     * @param stderr Return logs from &#x60;stderr&#x60; (optional, default to false)
     * @param since Only return logs since this time, as a UNIX timestamp (optional, default to 0)
     * @param timestamps Add timestamps to every log line (optional, default to false)
     * @param tail Only return this number of log lines from the end of the logs. Specify as an integer or &#x60;all&#x60; to output all log lines.  (optional, default to "all")
     * @return io.github.xzima.docomagos.docker.infrastructure.OctetByteArray
     */
    open suspend fun taskLogs(
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
            "/tasks/{id}/logs".replace("{" + "id" + "}", id),
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
