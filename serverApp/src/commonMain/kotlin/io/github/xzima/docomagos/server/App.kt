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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.koin.inject
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.ext.ktor.customEmbeddedServer
import io.github.xzima.docomagos.server.routes.http
import io.github.xzima.docomagos.server.routes.rsocketRoute
import io.github.xzima.docomagos.server.services.StaticUiService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.rsocket.kotlin.ktor.server.RSocketSupport

object App {
    private val logger = KotlinLogging.from(App::class)

    fun createServer(): CIOApplicationEngine = customEmbeddedServer {
        install(WebSockets)
        install(RSocketSupport) {
            server {
                val rSocketEnv = inject<RSocketEnv>()
                maxFragmentSize = rSocketEnv.maxFragmentSize
            }
        }

        environment.monitor.subscribe(ApplicationStarted) {
            logger.info { "ApplicationStarted" }
        }
        environment.monitor.subscribe(ApplicationStopping) {
            logger.info { "ApplicationStopping" }
        }
        environment.monitor.subscribe(ApplicationStopped) {
            logger.info { "ApplicationStopped" }
        }
        environment.monitor.subscribe(ApplicationStopPreparing) {
            logger.info { "ApplicationStopPreparing" }
        }

        routing {
            http()
            route("/") {
                val staticUiService = inject<StaticUiService>()
                staticUiService.configureStaticUi(this)
            }
            rsocketRoute()
        }
    }

    fun ApplicationEngine.stopServer() {
        val ktorEnv = inject<KtorEnv>()
        logger.info { "start server stopping" }
        stop(ktorEnv.gracePeriodMillis, ktorEnv.graceTimeoutMillis)
        logger.info { "server successfully stopped" }
    }
}
