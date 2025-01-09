/**
 * Copyright 2024-2025 Alex Zima(xzima@ro.ru)
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
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.ext.ktor.customEmbeddedServer
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.routes.RouteInjector
import io.github.xzima.docomagos.server.routes.http
import io.github.xzima.docomagos.server.services.JobService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.rsocket.kotlin.ktor.server.RSocketSupport
import kotlinx.coroutines.*

object App {
    private val logger = KotlinLogging.from(App::class)

    fun createServer(
        rsocketProps: RsocketProps,
        jobService: JobService,
        routeInjectors: List<RouteInjector>,
        ktorProps: KtorProps,
    ): CIOApplicationEngine = customEmbeddedServer(ktorProps) {
        install(WebSockets)
        install(RSocketSupport) {
            server {
                val rSocketEnv = rsocketProps
                maxFragmentSize = rSocketEnv.maxFragmentSize
            }
        }

        val pingJob = jobService.createJob(this)

        environment.monitor.subscribe(ApplicationStarted) {
            pingJob.start()
            logger.info { "ApplicationStarted $it" }
        }
        environment.monitor.subscribe(ApplicationStopping) {
            logger.info { "ApplicationStopping" }
        }
        environment.monitor.subscribe(ApplicationStopped) {
            runBlocking { pingJob.join() }
            logger.info { "ApplicationStopped" }
        }
        environment.monitor.subscribe(ApplicationStopPreparing) {
            logger.info { "ApplicationStopPreparing" }
        }

        routing {
            http()
            routeInjectors.forEach { it.injectRoutes(this) }
        }
    }

    fun ApplicationEngine.stopServer(ktorProps: KtorProps) {
        logger.info { "start server stopping" }
        stop(ktorProps.gracePeriodMillis, ktorProps.graceTimeoutMillis)
        logger.info { "server successfully stopped" }
    }
}
