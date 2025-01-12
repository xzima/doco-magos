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
package io.github.xzima.docomagos.docker.infrastructure

import io.ktor.http.*
import io.ktor.util.reflect.*

open class HttpResponse<T : Any>(
    val response: io.ktor.client.statement.HttpResponse,
    val provider: BodyProvider<T>,
) {
    val from = response.call.request.url
    val status: HttpStatusCode = response.status
    val headers: Map<String, List<String>> = response.headers.mapEntries()

    suspend fun body(): T = provider.body(response)

    suspend inline fun <reified V : Any> typedBody(): V = provider.typedBody(response, typeInfo<V>())

    companion object {
        private fun Headers.mapEntries(): Map<String, List<String>> {
            val result = mutableMapOf<String, List<String>>()
            entries().forEach { result[it.key] = it.value }
            return result
        }
    }
}

interface BodyProvider<T : Any> {
    suspend fun body(response: io.ktor.client.statement.HttpResponse): T

    suspend fun <V : Any> typedBody(response: io.ktor.client.statement.HttpResponse, type: TypeInfo): V
}

@Suppress("UNCHECKED_CAST")
class TypedBodyProvider<T : Any>(
    private val type: TypeInfo,
) : BodyProvider<T> {

    override suspend fun body(response: io.ktor.client.statement.HttpResponse): T = response.call.body(type) as T

    override suspend fun <V : Any> typedBody(response: io.ktor.client.statement.HttpResponse, type: TypeInfo): V =
        response.call.body(type) as V
}

class MappedBodyProvider<S : Any, T : Any>(
    private val provider: BodyProvider<S>,
    private val block: S.() -> T,
) : BodyProvider<T> {
    override suspend fun body(response: io.ktor.client.statement.HttpResponse): T = block(provider.body(response))

    override suspend fun <V : Any> typedBody(response: io.ktor.client.statement.HttpResponse, type: TypeInfo): V =
        provider.typedBody(response, type)
}

inline fun <reified T : Any> io.ktor.client.statement.HttpResponse.wrap(): HttpResponse<T> =
    HttpResponse(this, TypedBodyProvider(typeInfo<T>()))

fun <T : Any, V : Any> HttpResponse<T>.map(block: T.() -> V): HttpResponse<V> =
    HttpResponse(response, MappedBodyProvider(provider, block))
