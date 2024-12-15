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
