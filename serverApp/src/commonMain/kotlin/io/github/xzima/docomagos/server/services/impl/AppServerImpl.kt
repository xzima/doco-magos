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
package io.github.xzima.docomagos.server.services.impl

import io.github.xzima.docomagos.server.doOnSigterm
import io.github.xzima.docomagos.server.ext.ktor.ktorLogger
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.routes.RouteInjector
import io.github.xzima.docomagos.server.routes.http
import io.github.xzima.docomagos.server.services.AppServer
import io.github.xzima.docomagos.server.services.JobService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.rsocket.kotlin.ktor.server.RSocketSupport
import kotlinx.coroutines.*

private typealias CIOServer = EmbeddedServer<CIOApplicationEngine, CIOApplicationEngine.Configuration>

class AppServerImpl(
    private val rsocketProps: RsocketProps,
    private val jobService: JobService,
    private val routeInjectors: List<RouteInjector>,
    private val ktorProps: KtorProps,
) : AppServer {

    override suspend fun start(wait: Boolean) {
        server.startSuspend(wait)
    }

    override suspend fun stop() {
        server.stopSuspend()
    }

    private val server: CIOServer by lazy {
        initServer().apply {
            monitor.subscribe(ApplicationStarting) {
                doOnSigterm(this::stop)
            }
        }
    }

    private fun initServer() = serverDsl {
        install(WebSockets)
        install(RSocketSupport) {
            server {
                maxFragmentSize = rsocketProps.maxFragmentSize
            }
        }

        val pingJob = jobService.createJob(this)

        monitor.subscribe(ApplicationStarted) {
            pingJob.start()
            log.info("ApplicationStarted $it")
        }
        monitor.subscribe(ApplicationStopping) {
            log.info("ApplicationStopping")
        }
        monitor.subscribe(ApplicationStopped) {
            runBlocking { pingJob.join() }
            log.info("ApplicationStopped")
        }
        monitor.subscribe(ApplicationStopPreparing) {
            log.info("ApplicationStopPreparing")
        }

        routing {
            http()
            routeInjectors.forEach { it.injectRoutes(this) }
        }
    }

    private fun serverDsl(module: suspend Application.() -> Unit): CIOServer {
        val configure: CIOApplicationEngine.Configuration.() -> Unit = {
            reuseAddress = ktorProps.reuseAddress
            connector {
                port = ktorProps.port
            }
            shutdownGracePeriod = ktorProps.gracePeriodMillis
            shutdownTimeout = ktorProps.graceTimeoutMillis
        }
        val environment = applicationEnvironment {
            log = ktorLogger("ktor.application")
        }
        return embeddedServer(CIO, environment = environment, configure = configure, module = module)
    }
}
