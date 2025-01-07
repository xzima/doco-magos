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
package io.github.xzima.docomagos.server.cli

import com.github.ajalt.clikt.core.CliktCommand
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.koin.inject
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.services.GitService
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(AbstractServeCommand::class)

abstract class AbstractServeCommand : CliktCommand(name = "serve") {
    private val gitService by lazy { inject<GitService>() }

    override fun run(): Unit = runBlocking {
        checkMainRepo()
        serveServer()
    }

    abstract suspend fun serveServer()

    private suspend fun checkMainRepo() {
        logger.debug { "main repo: start check" }
        gitService.checkMainRepoPath()
        gitService.checkMainRepoUrl()
        gitService.checkMainRepoHead()
        logger.debug { "main repo: check successfully completed" }
    }
}
