package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import kotlinx.coroutines.CompletableDeferred
import kotlin.coroutines.CoroutineContext

data class RequestTask(
    val request: HttpRequestData,
    val response: CompletableDeferred<HttpResponseData>,
    val context: CoroutineContext,
)
