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
import io.github.xzima.docomagos.docker.models.Plugin
import io.github.xzima.docomagos.docker.models.PluginPrivilege
import io.ktor.client.*
import io.ktor.client.engine.*
import kotlinx.serialization.*
import kotlinx.serialization.encoding.*
import kotlinx.serialization.json.*

open class PluginApi : ApiClient {

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
     * Get plugin privileges
     *
     * @param remote The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @return kotlin.collections.List<PluginPrivilege>
     */
    open suspend fun getPluginPrivileges(remote: String): HttpResponse<List<PluginPrivilege>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        remote.apply { localVariableQuery["remote"] = listOf(remote) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/plugins/privileges",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<GetPluginPrivilegesResponse>().map { value }
    }

    @Serializable(GetPluginPrivilegesResponse.Companion::class)
    private class GetPluginPrivilegesResponse(
        val value: List<PluginPrivilege>,
    ) {
        companion object : KSerializer<GetPluginPrivilegesResponse> {
            private val serializer: KSerializer<List<PluginPrivilege>> = serializer<List<PluginPrivilege>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: GetPluginPrivilegesResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = GetPluginPrivilegesResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Create a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param tarContext Path to tar containing plugin rootfs and manifest (optional)
     * @return void
     */
    open suspend fun pluginCreate(
        name: String,
        tarContext: io.github.xzima.docomagos.docker.infrastructure.OctetByteArray? = null,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = tarContext

        val localVariableQuery = mutableMapOf<String, List<String>>()
        name.apply { localVariableQuery["name"] = listOf(name) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/create",
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
     * Remove a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param force Disable the plugin before removing. This may result in issues if the plugin is in use by a container.  (optional, default to false)
     * @return Plugin
     */
    open suspend fun pluginDelete(name: String, force: Boolean? = false): HttpResponse<Plugin> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        force?.apply { localVariableQuery["force"] = listOf("$force") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.DELETE,
            "/plugins/{name}".replace("{" + "name" + "}", name),
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
     * Disable a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param force Force disable a plugin even if still in use.  (optional)
     * @return void
     */
    open suspend fun pluginDisable(name: String, force: Boolean? = null): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        force?.apply { localVariableQuery["force"] = listOf("$force") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/{name}/disable".replace("{" + "name" + "}", name),
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
     * Enable a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param timeout Set the HTTP client timeout (in seconds) (optional, default to 0)
     * @return void
     */
    open suspend fun pluginEnable(name: String, timeout: Int? = 0): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        timeout?.apply { localVariableQuery["timeout"] = listOf("$timeout") }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/{name}/enable".replace("{" + "name" + "}", name),
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
     * Inspect a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @return Plugin
     */
    open suspend fun pluginInspect(name: String): HttpResponse<Plugin> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/plugins/{name}/json".replace("{" + "name" + "}", name),
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
     * List plugins
     * Returns information about installed plugins.
     * @param filters A JSON encoded value of the filters (a &#x60;map[string][]string&#x60;) to process on the plugin list.  Available filters:  - &#x60;capability&#x3D;&lt;capability name&gt;&#x60; - &#x60;enable&#x3D;&lt;true&gt;|&lt;false&gt;&#x60;  (optional)
     * @return kotlin.collections.List<Plugin>
     */
    open suspend fun pluginList(filters: String? = null): HttpResponse<List<Plugin>> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        filters?.apply { localVariableQuery["filters"] = listOf(filters) }
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.GET,
            "/plugins",
            query = localVariableQuery,
            headers = localVariableHeaders,
            requiresAuthentication = false,
        )

        return request(
            localVariableConfig,
            localVariableBody,
            localVariableAuthNames,
        ).wrap<PluginListResponse>().map { value }
    }

    @Serializable(PluginListResponse.Companion::class)
    private class PluginListResponse(
        val value: List<Plugin>,
    ) {
        companion object : KSerializer<PluginListResponse> {
            private val serializer: KSerializer<List<Plugin>> = serializer<List<Plugin>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: PluginListResponse) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = PluginListResponse(serializer.deserialize(decoder))
        }
    }

    /**
     * Install a plugin
     * Pulls and installs a plugin. After the plugin is installed, it can be enabled using the [&#x60;POST /plugins/{name}/enable&#x60; endpoint](#operation/PostPluginsEnable).
     * @param remote Remote reference for plugin to install.  The &#x60;:latest&#x60; tag is optional, and is used as the default if omitted.
     * @param name Local name for the pulled plugin.  The &#x60;:latest&#x60; tag is optional, and is used as the default if omitted.  (optional)
     * @param xRegistryAuth A base64url-encoded auth configuration to use when pulling a plugin from a registry.  Refer to the [authentication section](#section/Authentication) for details.  (optional)
     * @param body  (optional)
     * @return void
     */
    open suspend fun pluginPull(
        remote: String,
        name: String? = null,
        xRegistryAuth: String? = null,
        body: List<PluginPrivilege>,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = PluginPullRequest(body)

        val localVariableQuery = mutableMapOf<String, List<String>>()
        remote.apply { localVariableQuery["remote"] = listOf(remote) }
        name?.apply { localVariableQuery["name"] = listOf(name) }
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth?.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/pull",
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

    @Serializable(PluginPullRequest.Companion::class)
    private class PluginPullRequest(
        val value: List<PluginPrivilege>,
    ) {
        companion object : KSerializer<PluginPullRequest> {
            private val serializer: KSerializer<List<PluginPrivilege>> = serializer<List<PluginPrivilege>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: PluginPullRequest) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = PluginPullRequest(serializer.deserialize(decoder))
        }
    }

    /**
     * Push a plugin
     * Push a plugin to the registry.
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @return void
     */
    open suspend fun pluginPush(name: String): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody =
            io.ktor.client.utils.EmptyContent

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/{name}/push".replace("{" + "name" + "}", name),
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
     * Configure a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param body  (optional)
     * @return void
     */
    open suspend fun pluginSet(name: String, body: List<String>): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = PluginSetRequest(body)

        val localVariableQuery = mutableMapOf<String, List<String>>()
        val localVariableHeaders = mutableMapOf<String, String>()

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/{name}/set".replace("{" + "name" + "}", name),
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

    @Serializable(PluginSetRequest.Companion::class)
    private class PluginSetRequest(
        val value: List<String>,
    ) {
        companion object : KSerializer<PluginSetRequest> {
            private val serializer: KSerializer<List<String>> = serializer<List<String>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: PluginSetRequest) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = PluginSetRequest(serializer.deserialize(decoder))
        }
    }

    /**
     * Upgrade a plugin
     *
     * @param name The name of the plugin. The &#x60;:latest&#x60; tag is optional, and is the default if omitted.
     * @param remote Remote reference to upgrade to.  The &#x60;:latest&#x60; tag is optional, and is used as the default if omitted.
     * @param xRegistryAuth A base64url-encoded auth configuration to use when pulling a plugin from a registry.  Refer to the [authentication section](#section/Authentication) for details.  (optional)
     * @param body  (optional)
     * @return void
     */
    open suspend fun pluginUpgrade(
        name: String,
        remote: String,
        xRegistryAuth: String? = null,
        body: List<PluginPrivilege>,
    ): HttpResponse<Unit> {
        val localVariableAuthNames = listOf<String>()

        val localVariableBody = PluginUpgradeRequest(body)

        val localVariableQuery = mutableMapOf<String, List<String>>()
        remote.apply { localVariableQuery["remote"] = listOf(remote) }
        val localVariableHeaders = mutableMapOf<String, String>()
        xRegistryAuth?.apply { localVariableHeaders["X-Registry-Auth"] = this.toString() }

        val localVariableConfig = RequestConfig<Any?>(
            RequestMethod.POST,
            "/plugins/{name}/upgrade".replace("{" + "name" + "}", name),
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

    @Serializable(PluginUpgradeRequest.Companion::class)
    private class PluginUpgradeRequest(
        val value: List<PluginPrivilege>,
    ) {
        companion object : KSerializer<PluginUpgradeRequest> {
            private val serializer: KSerializer<List<PluginPrivilege>> = serializer<List<PluginPrivilege>>()
            override val descriptor = serializer.descriptor

            override fun serialize(encoder: Encoder, value: PluginUpgradeRequest) =
                serializer.serialize(encoder, value.value)

            override fun deserialize(decoder: Decoder) = PluginUpgradeRequest(serializer.deserialize(decoder))
        }
    }
}
