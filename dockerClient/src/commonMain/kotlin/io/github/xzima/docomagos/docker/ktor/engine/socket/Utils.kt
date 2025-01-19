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

import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.util.date.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import io.ktor.utils.io.CancellationException as KtorCancellationException

@OptIn(InternalAPI::class)
fun getRequestTimeout(request: HttpRequestData, engineConfig: SocketCIOEngineConfig): Long {
    /**
     * The request timeout is handled by the plugin and disabled for the WebSockets.
     */
    val isWebSocket = request.url.protocol.isWebsocket()
    if (request.getCapabilityOrNull(HttpTimeout) != null || isWebSocket || request.isUpgradeRequest()) {
        return HttpTimeout.INFINITE_TIMEOUT_MS
    }

    return engineConfig.requestTimeout
}

@OptIn(DelicateCoroutinesApi::class)
fun setupTimeout(callContext: CoroutineContext, request: HttpRequestData, timeout: Long) {
    if (timeout == HttpTimeout.INFINITE_TIMEOUT_MS || timeout == 0L) return

    val timeoutJob = GlobalScope.launch {
        delay(timeout)
        callContext.job.cancel("Request is timed out", HttpRequestTimeoutException(request))
    }

    callContext.job.invokeOnCompletion {
        timeoutJob.cancel()
    }
}

suspend fun writeRequest(
    request: HttpRequestData,
    output: ByteWriteChannel,
    callContext: CoroutineContext,
//    overProxy: Boolean,
    closeChannel: Boolean = true,
) = withContext(callContext) {
    writeHeaders(request, output, closeChannel)
    writeBody(request, output, callContext)
}

@OptIn(InternalAPI::class)
suspend fun writeHeaders(request: HttpRequestData, output: ByteWriteChannel, closeChannel: Boolean = true) {
    val builder = RequestResponseBuilder()

    val method = request.method
    val url = request.url
    val headers = request.headers
    val body = request.body

    val contentLength = headers[HttpHeaders.ContentLength] ?: body.contentLength?.toString()
    val contentEncoding = headers[HttpHeaders.TransferEncoding]
    val responseEncoding = body.headers[HttpHeaders.TransferEncoding]
    val chunked = isChunked(contentLength, responseEncoding, contentEncoding)
    val expected = headers[HttpHeaders.Expect]

    try {
        val normalizedUrl = if (url.pathSegments.isEmpty()) URLBuilder(url).apply { encodedPath = "/" }.build() else url
        val urlString = normalizedUrl.fullPath

        builder.requestLine(method, urlString, HttpProtocolVersion.HTTP_1_1.toString())
        // this will only add the port to the host header if the port is non-standard for the protocol
        if (!headers.contains(HttpHeaders.Host)) {
            val host = if (url.protocol.defaultPort == url.port) {
                url.host
            } else {
                url.hostWithPort
            }
            builder.headerLine(HttpHeaders.Host, host)
        }

        if (contentLength != null) {
            if ((method != HttpMethod.Get && method != HttpMethod.Head) || body !is OutgoingContent.NoContent) {
                builder.headerLine(HttpHeaders.ContentLength, contentLength)
            }
        }

        mergeHeaders(headers, body) { key, value ->
            if (key == HttpHeaders.ContentLength || key == HttpHeaders.Expect) return@mergeHeaders

            builder.headerLine(key, value)
        }

        if (chunked && contentEncoding == null && responseEncoding == null && body !is OutgoingContent.NoContent) {
            builder.headerLine(HttpHeaders.TransferEncoding, "chunked")
        }

        if (expectContinue(expected, body)) {
            builder.headerLine(HttpHeaders.Expect, expected!!)
        }

        builder.emptyLine()
        output.writePacket(builder.build())
        output.flush()
    } catch (cause: Throwable) {
        if (closeChannel) {
            output.close()
        }
        throw cause
    } finally {
        builder.release()
    }
}

suspend fun writeBody(
    request: HttpRequestData,
    output: ByteWriteChannel,
    callContext: CoroutineContext,
    closeChannel: Boolean = true,
) {
    if (request.body is OutgoingContent.NoContent) {
        if (closeChannel) output.close()
        return
    }

    val contentLength = request.headers[HttpHeaders.ContentLength] ?: request.body.contentLength?.toString()
    val contentEncoding = request.headers[HttpHeaders.TransferEncoding]
    val responseEncoding = request.body.headers[HttpHeaders.TransferEncoding]
    val chunked = isChunked(contentLength, responseEncoding, contentEncoding)

    val chunkedJob = if (chunked) encodeChunked(output, callContext) else null
    val channel = chunkedJob?.channel ?: output

    val scope = CoroutineScope(callContext + CoroutineName("Request body writer"))
    scope.launch {
        try {
            when (val body = request.body) {
                is OutgoingContent.NoContent -> return@launch
                is OutgoingContent.ByteArrayContent -> channel.writeFully(body.bytes())
                is OutgoingContent.ReadChannelContent -> body.readFrom().copyAndClose(channel)
                is OutgoingContent.WriteChannelContent -> body.writeTo(channel)
                is OutgoingContent.ProtocolUpgrade -> throw UnsupportedContentTypeException(body)
            }
        } catch (cause: Throwable) {
            channel.close(cause)
            throw cause
        } finally {
            channel.flush()
            chunkedJob?.channel?.close()
            chunkedJob?.join()

            output.closedCause?.unwrapCancellationException()?.takeIf { it !is KtorCancellationException }
                ?.let {
                    throw it
                }
            if (closeChannel) {
                output.close()
            }
        }
    }
}

suspend fun readResponse(
    requestTime: GMTDate,
    request: HttpRequestData,
    input: ByteReadChannel,
    output: ByteWriteChannel,
    callContext: CoroutineContext,
): HttpResponseData = withContext(callContext) {
    val rawResponse = parseResponse(input)
        ?: throw EOFException("Failed to parse HTTP response: unexpected EOF")

    rawResponse.use {
        val status = HttpStatusCode(rawResponse.status, rawResponse.statusText.toString())
        val contentLength = rawResponse.headers[HttpHeaders.ContentLength]?.toString()?.toLong() ?: -1L
        val transferEncoding = rawResponse.headers[HttpHeaders.TransferEncoding]?.toString()
        val connectionType = ConnectionOptions.parse(rawResponse.headers[HttpHeaders.Connection])

        val rawHeaders = rawResponse.headers
        val headers = HeadersImpl(rawHeaders.toMap())
        val version = HttpProtocolVersion.parse(rawResponse.version)

        if (status == HttpStatusCode.SwitchingProtocols) {
            val session = RawWebSocket(input, output, masking = true, coroutineContext = callContext)
            return@withContext HttpResponseData(status, requestTime, headers, version, session, callContext)
        }

        val body = when {
            request.method == HttpMethod.Head ||
                status in listOf(HttpStatusCode.NotModified, HttpStatusCode.NoContent) ||
                status.isInformational() -> {
                ByteReadChannel.Empty
            }

            else -> {
                val coroutineScope = CoroutineScope(callContext + CoroutineName("Response"))
                val httpBodyParser = coroutineScope.writer(autoFlush = true) {
                    parseHttpBody(version, contentLength, transferEncoding, connectionType, input, channel)
                }
                httpBodyParser.channel
            }
        }

        return@withContext HttpResponseData(status, requestTime, headers, version, body, callContext)
    }
}

fun HttpHeadersMap.toMap(): Map<String, List<String>> {
    val result = mutableMapOf<String, MutableList<String>>()

    for (index in 0 until size) {
        val key = nameAt(index).toString()
        val value = valueAt(index).toString()

        if (result[key]?.add(value) == null) {
            result[key] = mutableListOf(value)
        }
    }

    return result
}

fun HttpStatusCode.isInformational(): Boolean = (value / 100) == 1

fun isChunked(contentLength: String?, responseEncoding: String?, contentEncoding: String?) =
    contentLength == null || responseEncoding == "chunked" || contentEncoding == "chunked"

fun expectContinue(expectHeader: String?, body: OutgoingContent) =
    expectHeader != null && body !is OutgoingContent.NoContent

@OptIn(DelicateCoroutinesApi::class)
fun ByteWriteChannel.withoutClosePropagation(coroutineContext: CoroutineContext): ByteWriteChannel {
    coroutineContext[Job]!!.invokeOnCompletion { close(it) }

    return GlobalScope.reader(coroutineContext, autoFlush = true) {
        channel.copyTo(this@withoutClosePropagation, Long.MAX_VALUE)
        this@withoutClosePropagation.flush()
    }.channel
}
