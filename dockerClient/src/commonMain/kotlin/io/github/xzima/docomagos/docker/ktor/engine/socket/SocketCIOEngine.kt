package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.client.engine.ClientEngineClosedException
import io.ktor.client.engine.HttpClientEngineBase
import io.ktor.client.engine.callContext
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.websocket.WebSocketCapability
import io.ktor.client.plugins.websocket.WebSocketExtensionsCapability
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.network.selector.SelectorManager
import io.ktor.util.InternalAPI
import io.ktor.util.SilentSupervisor
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@OptIn(DelicateCoroutinesApi::class)
class SocketCIOEngine(
    override val config: SocketCIOEngineConfig,
) : HttpClientEngineBase("socket-ktor-cio") {

    override val supportedCapabilities = setOf(HttpTimeout.Plugin, WebSocketCapability, WebSocketExtensionsCapability)

    private val endpoints = ConcurrentMap<String, SocketEndpoint>()

    private val selectorManager = SelectorManager(dispatcher)

    private val connectionFactory = ConnectionFactory(
        selectorManager,
        config.maxConnectionsCount,
        config.endpoint.maxConnectionsPerRoute,
    )

    private val requestsJob: CoroutineContext

    override val coroutineContext: CoroutineContext

    init {
        val parentContext = super.coroutineContext
        val parent = parentContext[Job.Key]!!

        requestsJob = SilentSupervisor(parent)

        val requestField = requestsJob
        coroutineContext = parentContext + requestField

        val requestJob = requestField[Job.Key]!!
        val selector = selectorManager

        @OptIn(ExperimentalCoroutinesApi::class)
        GlobalScope.launch(parentContext, start = CoroutineStart.ATOMIC) {
            try {
                requestJob.join()
            } finally {
                selector.close()
                selector.coroutineContext[Job.Key]!!.join()
            }
        }
    }

    @InternalAPI
    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        val callContext = callContext()

        while (coroutineContext.isActive) {
            val endpoint = selectEndpoint()

            try {
                return endpoint.execute(data, callContext)
            } catch (_: ClosedSendChannelException) {
                continue
            } finally {
                if (!coroutineContext.isActive) {
                    endpoint.close()
                }
            }
        }

        throw ClientEngineClosedException()
    }

    override fun close() {
        super.close()

        endpoints.forEach { (_, endpoint) ->
            endpoint.close()
        }

        (requestsJob[Job.Key] as CompletableJob).complete()
    }

    private fun selectEndpoint(): SocketEndpoint {
        val endpointId = config.unixSocketPath

        return endpoints.computeIfAbsent(endpointId) {
            SocketEndpoint(
                config,
                connectionFactory,
                coroutineContext,
                onDone = { endpoints.remove(endpointId) },
            )
        }
    }
}
