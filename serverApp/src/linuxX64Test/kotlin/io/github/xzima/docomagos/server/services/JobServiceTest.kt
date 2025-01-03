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
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifySuspend
import io.github.xzima.docomagos.server.env.AppEnv
import io.kotest.common.runBlocking
import kotlinx.coroutines.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class JobServiceTest {

    private val appEnv: AppEnv = AppEnv(
        staticUiPath = "any",
        jobPeriodMs = 100,
    )
    private val pingService = mock<PingService>(MockMode.autoUnit)
    private lateinit var service: JobService

    @BeforeTest
    fun beforeTest(): Unit = runBlocking {
        service = JobService(appEnv, pingService)
    }

    @AfterTest
    fun afterTest(): Unit = runBlocking {
        resetCalls(pingService)
        resetAnswers(pingService)
    }

    @Test
    fun testEachCall(): Unit = runBlocking {
        coroutineScope {
            // GIVEN
            val job = service.createJob(this)

            // WHEN
            job.start()
            delay(1.seconds)
            job.cancelAndJoin()

            // THEN
            verifySuspend(mode = VerifyMode.atLeast(9)) { pingService.ping() }
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
