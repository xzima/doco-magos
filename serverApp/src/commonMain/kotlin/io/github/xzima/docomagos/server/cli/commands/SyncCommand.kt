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
package io.github.xzima.docomagos.server.cli.commands

import com.github.ajalt.clikt.core.CliktCommand
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.koin.inject
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.SyncService
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(SyncCommand::class)

class SyncCommand : CliktCommand() {
    private val gitService by lazy { inject<GitService>() }
    private val syncService by lazy { inject<SyncService>() }
    private val dockerComposeService by lazy { inject<DockerComposeService>() }

    override fun run(): Unit = runBlocking {
        logger.info { "Start stack synchronization with options: TODO" }

        // TODO: clone or update head of git repo
        gitService.actualizeMainRepo()
        logger.debug { "Main repo actualized" }

        val syncPlan = syncService.createSyncPlanForMainRepo()
        logger.debug { "Created sync plan: $syncPlan" }

        dockerComposeService.executeSyncPlan(syncPlan)
        logger.info { "Stack synchronization completed" }
    }
}
