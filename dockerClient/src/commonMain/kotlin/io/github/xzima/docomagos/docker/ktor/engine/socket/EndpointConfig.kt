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

import io.ktor.client.plugins.HttpTimeout

class EndpointConfig {
    /**
     * Specifies the maximum number of connections for each host.
     *
     * @see [SocketCIOEngineConfig.maxConnectionsCount]
     */
    var maxConnectionsPerRoute: Int = 100

    /**
     * Specifies a time period (in milliseconds) in which a client should establish a connection with a server.
     */
    var connectTimeout: Long = 5000

    /**
     * Specifies a maximum time (in milliseconds) of inactivity between two data packets when exchanging data with a server.
     */
    var socketTimeout: Long = HttpTimeout.Plugin.INFINITE_TIMEOUT_MS

    /**
     * Specifies a maximum number of connection attempts.
     * Note: this property affects only connection retries, but not request retries.
     */
    var connectAttempts: Int = 1
}
