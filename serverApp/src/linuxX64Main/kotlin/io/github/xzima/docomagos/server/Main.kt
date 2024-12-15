package io.github.xzima.docomagos.server

import io.github.xzima.docomagos.server.services.DockerComposeService
import kotlinx.coroutines.*

fun main() = runBlocking {
    initKoinModule()
    // TODO create check external services task
    println("DC: " + inject<DockerComposeService>().listProjects().size)
    val server = createServer().start()
    initGracefulShutdown().join()
    server.stopServer()
}
