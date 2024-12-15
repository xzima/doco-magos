package io.github.xzima.docomagos.server.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Routing.http() {
    get("/health") {
        call.response.status(HttpStatusCode.NoContent)
    }
}
