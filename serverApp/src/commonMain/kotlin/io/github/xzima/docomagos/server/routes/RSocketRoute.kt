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
@file:OptIn(ExperimentalSerializationApi::class)

package io.github.xzima.docomagos.server.routes

import io.github.xzima.docomagos.Constants
import io.github.xzima.docomagos.api.ConfiguredProtoBuf
import io.github.xzima.docomagos.api.Req
import io.github.xzima.docomagos.api.decodeFromRequest
import io.github.xzima.docomagos.api.encodeToPayload
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.inject
import io.ktor.server.routing.*
import io.rsocket.kotlin.RSocketRequestHandler
import io.rsocket.kotlin.ktor.server.rSocket
import kotlinx.serialization.*

fun Routing.rSocketRoute() {
    rSocket(Constants.RSOCKET_API_PATH) {
        println(config.setupPayload.data.readText()) // print setup payload data

        RSocketRequestHandler {
            // handler for request/response
            requestResponse { requestPayload ->
                val request = ConfiguredProtoBuf.decodeFromRequest(requestPayload)
                println(request)

                val response = when (request) {
                    is Req.ListProjects -> inject<ListProjectsHandler>().handle(request)
                }

                ConfiguredProtoBuf.encodeToPayload(response)
            }
        }
    }
}
