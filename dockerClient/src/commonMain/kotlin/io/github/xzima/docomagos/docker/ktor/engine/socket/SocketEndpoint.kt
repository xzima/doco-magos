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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.ktor.client.network.sockets.mapEngineExceptions
import io.ktor.client.plugins.ConnectTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.takeFrom
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.network.sockets.Connection
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.SocketAddress
import io.ktor.network.sockets.UnixSocketAddress
import io.ktor.network.sockets.connection
import io.ktor.util.InternalAPI
import io.ktor.util.date.GMTDate
import io.ktor.util.date.getTimeMillis
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.close
import io.ktor.utils.io.core.Closeable
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.CoroutineContext

private val logger = KotlinLogging.from(SocketEndpoint::class)

class SocketEndpoint(
    private val config: SocketCIOEngineConfig,
    private val connectionFactory: ConnectionFactory,
    override val coroutineContext: CoroutineContext,
    private val onDone: () -> Unit,
) : CoroutineScope,
    Closeable {
    private val lastActivity = atomic(getTimeMillis())
    private val connections: AtomicInt = atomic(0)
    private val deliveryPoint: Channel<RequestTask> = Channel()
    private val maxEndpointIdleTime: Long = 2 * config.endpoint.connectTimeout

    private val timeout = launch(coroutineContext + CoroutineName("Endpoint timeout(${config.unixSocketPath})")) {
        try {
            while (true) {
                val remaining = (lastActivity.value + maxEndpointIdleTime) - getTimeMillis()
                if (remaining <= 0) {
                    break
                }

                delay(remaining)
            }
        } catch (_: Throwable) {
        } finally {
            deliveryPoint.close()
            onDone()
        }
    }

    suspend fun execute(request: HttpRequestData, callContext: CoroutineContext): HttpResponseData {
        lastActivity.value = getTimeMillis()

        return makeDedicatedRequest(request, callContext)
    }

    @OptIn(InternalAPI::class)
    private suspend fun makeDedicatedRequest(
        request: HttpRequestData,
        callContext: CoroutineContext,
    ): HttpResponseData {
        val (address, connection) = connect(request)
        val input = this.mapEngineExceptions(connection.input, request)
        val originOutput = this.mapEngineExceptions(connection.output, request)

        val output = originOutput.withoutClosePropagation(callContext)

        callContext[Job.Key]!!.invokeOnCompletion { cause ->
            val originCause = cause?.unwrapCancellationException()
            try {
                input.cancel(originCause)
                originOutput.close(originCause)
                connection.socket.close()
            } catch (cause: Throwable) {
                logger.debug(cause) {
                    "An error occurred while closing connection"
                }
            } finally {
                releaseConnection(address)
            }
        }

        val timeout = getRequestTimeout(request, config)
        setupTimeout(callContext, request, timeout)

        val requestTime = GMTDate()
//            val overProxy = proxy != null

        return if (expectContinue(request.headers[HttpHeaders.Expect], request.body)) {
            processExpectContinue(request, input, output, originOutput, callContext, requestTime)
        } else {
            writeRequest(request, output, callContext)
            readResponse(requestTime, request, input, originOutput, callContext)
        }
    }

    private suspend fun processExpectContinue(
        request: HttpRequestData,
        input: ByteReadChannel,
        output: ByteWriteChannel,
        originOutput: ByteWriteChannel,
        callContext: CoroutineContext,
        requestTime: GMTDate,
//        overProxy: Boolean,
    ) = withContext(callContext) {
        writeHeaders(request, output)

        val responseReady = withTimeoutOrNull(CONTINUE_RESPONSE_TIMEOUT_MILLIS) {
            input.awaitContent()
        }

        if (responseReady != null) {
            val response = readResponse(requestTime, request, input, originOutput, callContext)
            when (response.statusCode) {
                HttpStatusCode.Companion.ExpectationFailed -> {
                    val newRequest = HttpRequestBuilder().apply {
                        takeFrom(request)
                        headers.remove(HttpHeaders.Expect)
                    }.build()
                    writeRequest(newRequest, output, callContext)
                }

                HttpStatusCode.Companion.Continue -> {
                    writeBody(request, output, callContext)
                }

                else -> {
                    output.close()
                    return@withContext response
                }
            }
        } else {
            writeBody(request, output, callContext)
        }

        return@withContext readResponse(requestTime, request, input, originOutput, callContext)
    }

    @Suppress("UNUSED_EXPRESSION")
    private suspend fun connect(requestData: HttpRequestData): Pair<SocketAddress, Connection> {
        val connectAttempts = config.endpoint.connectAttempts
        val (connectTimeout, socketTimeout) = retrieveTimeouts(requestData)
        var timeoutFails = 0

        connections.incrementAndGet()

        try {
            repeat(connectAttempts) {
                val address = UnixSocketAddress(config.unixSocketPath)

                val connect: suspend CoroutineScope.() -> Socket = {
                    connectionFactory.connect(address) {
                        this.socketTimeout = socketTimeout
                    }
                }

                val socket = when (connectTimeout) {
                    HttpTimeout.Plugin.INFINITE_TIMEOUT_MS -> connect()
                    else -> {
                        val connection = withTimeoutOrNull(connectTimeout, connect)
                        if (connection == null) {
                            timeoutFails++
                            return@repeat
                        }
                        connection
                    }
                }

                val connection = socket.connection()
                return@connect address to connection
            }
        } catch (cause: Throwable) {
            connections.decrementAndGet()
            throw cause
        }

        connections.decrementAndGet()

        throw getTimeoutException(connectAttempts, timeoutFails, requestData)
    }

    /**
     * Defines the exact type of exception based on [connectAttempts] and [timeoutFails].
     */
    private fun getTimeoutException(connectAttempts: Int, timeoutFails: Int, request: HttpRequestData): Exception =
        when (timeoutFails) {
            connectAttempts -> ConnectTimeoutException(request)
            else -> FailToConnectException()
        }

    /**
     * Takes timeout attributes from [config] and [HttpTimeout.HttpTimeoutCapabilityConfiguration] and returns a pair of
     * connection timeout and socket timeout to be applied.
     */
    private fun retrieveTimeouts(requestData: HttpRequestData): Pair<Long, Long> {
        val default = config.endpoint.connectTimeout to config.endpoint.socketTimeout
        val timeoutAttributes = requestData.getCapabilityOrNull(HttpTimeout.Plugin)
            ?: return default

        val socketTimeout = timeoutAttributes.socketTimeoutMillis ?: config.endpoint.socketTimeout
        val connectTimeout = timeoutAttributes.connectTimeoutMillis ?: config.endpoint.connectTimeout
        return connectTimeout to socketTimeout
    }

    private fun releaseConnection(address: SocketAddress) {
        connectionFactory.release(address)
        connections.decrementAndGet()
    }

    override fun close() {
        timeout.cancel()
    }

    companion object {
        const val CONTINUE_RESPONSE_TIMEOUT_MILLIS = 1000L
    }
}
