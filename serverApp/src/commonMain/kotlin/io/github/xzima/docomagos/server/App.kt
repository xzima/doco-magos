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
package io.github.xzima.docomagos.server

import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.routes.http
import io.github.xzima.docomagos.server.routes.rSocketRoute
import io.github.xzima.docomagos.server.services.StaticUiService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.rsocket.kotlin.ktor.server.RSocketSupport

fun createServer(): CIOApplicationEngine {
    val ktorEnv = inject<KtorEnv>()

    val configure: CIOApplicationEngine.Configuration.() -> Unit = {
        reuseAddress = ktorEnv.reuseAddress
    }
    return embeddedServer(CIO, port = ktorEnv.port, configure = configure) {
        install(WebSockets)
        install(RSocketSupport) {
            server {
                val rSocketEnv = inject<RSocketEnv>()
                maxFragmentSize = rSocketEnv.maxFragmentSize
            }
        }
        environment.monitor.subscribe(ApplicationStarted) {
            println("ApplicationStarted")
        }
        environment.monitor.subscribe(ApplicationStopping) {
            println("ApplicationStopping")
        }
        environment.monitor.subscribe(ApplicationStopped) {
            println("ApplicationStopped")
        }
        environment.monitor.subscribe(ApplicationStopPreparing) {
            println("ApplicationStopPreparing")
        }

        routing {
            http()
            route("/") {
                val staticUiService = inject<StaticUiService>()
                staticUiService.configureStaticUi(this)
            }
            rSocketRoute()
        }
    }
}

fun ApplicationEngine.stopServer() {
    val ktorEnv = inject<KtorEnv>()
    environment.log.info("start server stopping")
    stop(ktorEnv.gracePeriodMillis, ktorEnv.graceTimeoutMillis)
    environment.log.info("server successfully stopped")
}
