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
package io.github.xzima.docomagos.server.services

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.services.impl.JobServiceImpl
import io.kotest.common.runBlocking
import kotlinx.coroutines.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class JobServiceTest {

    private val appEnv = object : AppProps {
        override val hostname: String = "any"
        override val staticUiPath: String = "any"
        override val jobPeriodMs: Int = 100
    }

    private val pingService = mock<PingService>(MockMode.autoUnit)
    private val gitService = mock<GitService>(MockMode.autoUnit)
    private val dockerService = mock<DockerService>(MockMode.autoUnit)
    private val syncService = mock<SyncService>(MockMode.autoUnit)
    private lateinit var service: JobServiceImpl

    @BeforeTest
    fun beforeTest(): Unit = runBlocking {
        KotlinLogging.configureLogging(Level.DEBUG)
        service = JobServiceImpl(
            appEnv = appEnv,
            pingService = pingService,
            gitService = gitService,
            dockerService = dockerService,
            syncService = syncService,
        )
    }

    @AfterTest
    fun afterTest(): Unit = runBlocking {
        verifyNoMoreCalls(pingService, gitService, dockerService, syncService)
        resetCalls(pingService, gitService, dockerService, syncService)
        resetAnswers(pingService, gitService, dockerService, syncService)
    }

    @Test
    fun testPositiveExecution(): Unit = runBlocking {
        coroutineScope {
            // GIVEN
            everySuspend { gitService.isActualRepoHead() } returns true
            everySuspend { syncService.isMainRepoStacksUpdateRequired() } returns false
            val job = service.createJob(this)

            // WHEN
            job.start()
            delay(1.seconds)
            job.cancelAndJoin()

            // THEN
            verifySuspend(mode = VerifyMode.atLeast(9)) { pingService.ping() }
            verifySuspend(mode = VerifyMode.atLeast(9)) { gitService.isActualRepoHead() }
            verifySuspend(mode = VerifyMode.atLeast(9)) { syncService.isMainRepoStacksUpdateRequired() }
        }
    }

    @Test
    fun testRepoNotActual(): Unit = runBlocking {
        coroutineScope {
            // GIVEN
            everySuspend { gitService.isActualRepoHead() } returns false
            val job = service.createJob(this)

            // WHEN
            job.start()
            delay(200.milliseconds)
            job.cancelAndJoin()

            // THEN
            verifySuspend(mode = VerifyMode.atLeast(1)) { pingService.ping() }
            verifySuspend(mode = VerifyMode.atLeast(1)) { gitService.isActualRepoHead() }
            verifySuspend(mode = VerifyMode.atLeast(1)) { dockerService.tryStartSyncJobService() }
        }
    }

    @Test
    fun testRepoStacksUpdateRequired(): Unit = runBlocking {
        coroutineScope {
            // GIVEN
            everySuspend { gitService.isActualRepoHead() } returns true
            everySuspend { syncService.isMainRepoStacksUpdateRequired() } returns true
            val job = service.createJob(this)

            // WHEN
            job.start()
            delay(200.milliseconds)
            job.cancelAndJoin()

            // THEN
            verifySuspend(mode = VerifyMode.atLeast(1)) { pingService.ping() }
            verifySuspend(mode = VerifyMode.atLeast(1)) { gitService.isActualRepoHead() }
            verifySuspend(mode = VerifyMode.atLeast(1)) { syncService.isMainRepoStacksUpdateRequired() }
            verifySuspend(mode = VerifyMode.atLeast(1)) { dockerService.tryStartSyncJobService() }
        }
    }

    @Test
    fun testFailedEachCall(): Unit = runBlocking {
        coroutineScope {
            // GIVEN
            everySuspend { pingService.ping() } throws Exception("Any error")
            val job = service.createJob(this)

            // WHEN
            job.start()
            delay(200.milliseconds)
            job.cancelAndJoin()

            // THEN
            verifySuspend(mode = VerifyMode.atLeast(1)) { pingService.ping() }
        }
    }
}
