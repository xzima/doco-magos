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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.services.DockerService
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.JobService
import io.github.xzima.docomagos.server.services.PingService
import io.github.xzima.docomagos.server.services.SyncService
import kotlinx.coroutines.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val logger = KotlinLogging.from(JobServiceImpl::class)

class JobServiceImpl(
    private val appEnv: AppProps,
    private val pingService: PingService,
    private val gitService: GitService,
    private val dockerService: DockerService,
    private val syncService: SyncService,
) : JobService {
    companion object {
        @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
        private val dispatcher: CoroutineDispatcher = newSingleThreadContext("JobService-worker")
    }

    override fun createJob(scope: CoroutineScope): Job = scope.launch(dispatcher, start = CoroutineStart.LAZY) {
        try {
            onStart()
            while (true) {
                delay(appEnv.jobPeriodMs.toDuration(DurationUnit.MILLISECONDS))
                onEach()
            }
        } finally {
            onStop()
        }
    }

    private fun onStart() = try {
        logger.debug { "on start phase: started" }
    } catch (e: Exception) {
        logger.error(e) { "on start phase: failed" }
    } finally {
        logger.debug { "on start phase: finished" }
    }

    private suspend fun onEach() {
        try {
            logger.debug { "on each phase: started" }
            pingService.ping()

            val isActualRepoHead = gitService.isActualRepoHead()
            logger.info { "isActualRepoHead: $isActualRepoHead" }
            if (!isActualRepoHead) {
                dockerService.tryStartSyncJobService()
                return
            }

            val isMainRepoStacksUpdateRequired = syncService.isMainRepoStacksUpdateRequired()
            logger.info { "isMainRepoStacksUpdateRequired: $isMainRepoStacksUpdateRequired" }
            if (isMainRepoStacksUpdateRequired) {
                dockerService.tryStartSyncJobService()
                return
            }
        } catch (e: Exception) {
            logger.error(e) { "on each phase: failed" }
        } finally {
            logger.debug { "on each phase: finished" }
        }
    }

    private fun onStop() = try {
        logger.debug { "on stop phase: started" }
    } catch (e: Exception) {
        logger.error(e) { "on stop phase: failed" }
    } finally {
        logger.debug { "on stop phase: finished" }
    }
}
