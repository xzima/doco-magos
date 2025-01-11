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
package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.client.engine.HttpClientEngineConfig

class SocketCIOEngineConfig : HttpClientEngineConfig() {
    /**
     * Provides access to [io.github.xzima.docomagos.server.ext.socket.SocketEndpoint] settings.
     */
    val endpoint: EndpointConfig = EndpointConfig()

    /**
     * Specifies the maximum number of connections used to make [requests](https://ktor.io/docs/request.html).
     */
    var maxConnectionsCount: Int = 1000

    /**
     * Specifies a request timeout in milliseconds.
     * The request timeout is the time period required to process an HTTP call:
     * from sending a request to receiving a response.
     *
     * To disable this timeout, set its value to `0`.
     */
    var requestTimeout: Long = 15000

    var unixSocketPath: String = ""
}
