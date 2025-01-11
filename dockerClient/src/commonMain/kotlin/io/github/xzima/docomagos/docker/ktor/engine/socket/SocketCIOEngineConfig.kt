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
