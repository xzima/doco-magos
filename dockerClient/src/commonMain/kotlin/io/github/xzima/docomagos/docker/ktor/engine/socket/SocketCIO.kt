package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.engines
import io.ktor.util.InternalAPI

@OptIn(InternalAPI::class)
object SocketCIO : HttpClientEngineFactory<SocketCIOEngineConfig> {
    init {
        engines.append(SocketCIO)
    }

    override fun create(block: SocketCIOEngineConfig.() -> Unit): HttpClientEngine =
        SocketCIOEngine(SocketCIOEngineConfig().apply(block))

    override fun toString(): String = "SocketCIO"
}
