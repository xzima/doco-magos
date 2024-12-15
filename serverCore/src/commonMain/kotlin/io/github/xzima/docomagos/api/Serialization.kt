/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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
package io.github.xzima.docomagos.api

import io.ktor.utils.io.core.*
import io.rsocket.kotlin.ExperimentalMetadataApi
import io.rsocket.kotlin.RSocket
import io.rsocket.kotlin.metadata.RoutingMetadata
import io.rsocket.kotlin.metadata.metadata
import io.rsocket.kotlin.metadata.read
import io.rsocket.kotlin.payload.Payload
import io.rsocket.kotlin.payload.buildPayload
import io.rsocket.kotlin.payload.data
import kotlinx.serialization.*
import kotlinx.serialization.protobuf.*

// just stub
@ExperimentalSerializationApi
val ConfiguredProtoBuf = ProtoBuf

@ExperimentalSerializationApi
inline fun <reified T> ProtoBuf.decodeFromPayload(payload: Payload): T = decodeFromByteArray(payload.data.readBytes())

@ExperimentalSerializationApi
fun ProtoBuf.decodeFromRequest(payload: Payload): Req<Any> {
    return decodeFromByteArray(Req.polymorphicSerializer, payload.data.readBytes())
}

@ExperimentalSerializationApi
inline fun <reified T> ProtoBuf.encodeToPayload(value: T): Payload = buildPayload {
    data(encodeToByteArray(value))
}

@OptIn(ExperimentalSerializationApi::class)
suspend inline fun <reified RS> RSocket.reqResp(req: Req<RS>): RS {
    val resp = requestResponse(ConfiguredProtoBuf.encodeToPayload(req))
    return ConfiguredProtoBuf.decodeFromPayload(resp)
}

@ExperimentalSerializationApi
@OptIn(ExperimentalMetadataApi::class)
inline fun <reified T> ProtoBuf.encodeToPayload(route: String, value: T): Payload = buildPayload {
    data(encodeToByteArray(value))
    metadata(RoutingMetadata(route))
}

@ExperimentalSerializationApi
inline fun <reified I, reified O> ProtoBuf.decoding(payload: Payload, block: (I) -> O): Payload =
    encodeToPayload(decodeFromPayload<I>(payload).let(block))

@ExperimentalSerializationApi
inline fun <reified I> ProtoBuf.decodingToEmpty(payload: Payload, block: (I) -> Unit): Payload {
    decodeFromPayload<I>(payload).let(block)
    return Payload.Empty
}

@OptIn(ExperimentalMetadataApi::class)
fun Payload(route: String, packet: ByteReadPacket = ByteReadPacket.Empty): Payload = buildPayload {
    data(packet)
    metadata(RoutingMetadata(route))
}

@OptIn(ExperimentalMetadataApi::class)
fun Payload.route(): String = metadata?.read(RoutingMetadata)?.tags?.first() ?: error("No route provided")
