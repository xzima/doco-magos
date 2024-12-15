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
