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
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.logging.toLevel
import io.github.xzima.docomagos.server.App.stopServer
import io.github.xzima.docomagos.server.env.EnvUtils
import io.github.xzima.docomagos.server.services.DockerComposeService
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(::main)

fun main(): Unit = runBlocking {
    configureLogging(EnvUtils.getEnvVar("LOGGING_LEVEL") { it.toLevel() })
    initKoinModule()
    // TODO create check external services task
    inject<DockerComposeService>().listProjects().also {
        logger.debug { "DC: ${it.size}" }
    }
    val server = App.createServer().start()
    initGracefulShutdown().join()
    server.stopServer()
}
