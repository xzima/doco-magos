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
package io.github.xzima.docomagos.server.services

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.env.AppEnv
import kotlinx.coroutines.*
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private val logger = KotlinLogging.from(JobService::class)

class JobService(
    private val appEnv: AppEnv,
    private val pingService: PingService,
    private val gitService: GitService,
) {

    fun createJob(scope: CoroutineScope): Job = scope.launch(start = CoroutineStart.LAZY) {
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

    suspend fun onStart() = try {
        logger.debug { "on start phase: started" }
    } catch (e: Exception) {
        logger.error(e) { "on start phase: failed" }
    } finally {
        logger.debug { "on start phase: finished" }
    }

    suspend fun onEach() = try {
        logger.debug { "on each phase: started" }
        pingService.ping()
        val isActualRepoHead = gitService.isActualRepoHead()
        logger.info { "isActualRepoHead: $isActualRepoHead" }
    } catch (e: Exception) {
        logger.error(e) { "on each phase: failed" }
    } finally {
        logger.debug { "on each phase: finished" }
    }

    suspend fun onStop() = try {
        logger.debug { "on stop phase: started" }
    } catch (e: Exception) {
        logger.error(e) { "on stop phase: failed" }
    } finally {
        logger.debug { "on stop phase: finished" }
    }
}
