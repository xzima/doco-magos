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
