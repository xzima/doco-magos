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

import com.github.ajalt.clikt.testing.test
import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.koin.configureKoin
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.SyncService
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class SyncCommandTest {
    companion object {
        @BeforeClass
        fun setUp() {
            KotlinLogging.configureLogging(Level.TRACE)
        }
    }

    private val gitService = mock<GitService>(MockMode.autoUnit)
    private val syncService = mock<SyncService>(MockMode.autoUnit)
    private val dockerComposeService = mock<DockerComposeService>(MockMode.autoUnit)
    private lateinit var command: SyncCommand

    @BeforeTest
    fun before() {
        configureKoin {
            single<GitService> { gitService }
            single<SyncService> { syncService }
            single<DockerComposeService> { dockerComposeService }
        }
        command = SyncCommand()
    }

    @AfterTest
    fun after() {
        verifyNoMoreCalls(gitService, syncService, dockerComposeService)
        resetCalls(gitService, syncService, dockerComposeService)
        resetAnswers(gitService, syncService, dockerComposeService)
        stopKoin()
    }

    @Test
    fun testPositive(): Unit = runBlocking {
        // GIVEN
        everySuspend { gitService.actualizeMainRepo() } returns Unit
        val stackPlanMock = SyncStackPlan()
        every { syncService.createSyncPlanForMainRepo() } returns stackPlanMock
        everySuspend { dockerComposeService.executeSyncPlan(stackPlanMock) } returns Unit

        // WHEN
        val actual = command.test()

        // THEN
        actual.statusCode shouldBe 0

        verifySuspend(mode = VerifyMode.exactly(1)) { gitService.actualizeMainRepo() }
        verify(mode = VerifyMode.exactly(1)) { syncService.createSyncPlanForMainRepo() }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeService.executeSyncPlan(any()) }
    }

    @Test
    fun testActualizeMainRepoFailed(): Unit = runBlocking {
        // GIVEN
        everySuspend { gitService.actualizeMainRepo() } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitService.actualizeMainRepo() }
    }

    @Test
    fun testCreateSyncPlanFailed(): Unit = runBlocking {
        // GIVEN
        everySuspend { gitService.actualizeMainRepo() } returns Unit
        every { syncService.createSyncPlanForMainRepo() } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitService.actualizeMainRepo() }
        verify(mode = VerifyMode.exactly(1)) { syncService.createSyncPlanForMainRepo() }
    }

    @Test
    fun testExecuteSyncFailed(): Unit = runBlocking {
        // GIVEN
        everySuspend { gitService.actualizeMainRepo() } returns Unit
        val stackPlanMock = SyncStackPlan()
        every { syncService.createSyncPlanForMainRepo() } returns stackPlanMock
        everySuspend { dockerComposeService.executeSyncPlan(stackPlanMock) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitService.actualizeMainRepo() }
        verify(mode = VerifyMode.exactly(1)) { syncService.createSyncPlanForMainRepo() }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeService.executeSyncPlan(any()) }
    }
}
