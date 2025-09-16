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
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
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
import io.github.xzima.docomagos.server.services.AppServer
import io.github.xzima.docomagos.server.services.GitService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ServeCommandTest {

    private val gitService = mock<GitService>(MockMode.autoUnit)
    private val appServer = mock<AppServer>(MockMode.autoUnit)
    private lateinit var command: ServeCommand

    @BeforeTest
    fun before() {
        KotlinLogging.configureLogging(Level.DEBUG)
        configureKoin {
            single<GitService> { gitService }
            single<AppServer> { appServer }
        }
        command = ServeCommand()
    }

    @AfterTest
    fun after() {
        verifyNoMoreCalls(gitService, appServer)
        resetCalls(gitService, appServer)
        resetAnswers(gitService, appServer)
        stopKoin()
    }

    @Test
    fun testPositive(): Unit = runBlocking {
        // WHEN
        val actual = command.test()

        // THEN
        actual.statusCode shouldBe 0

        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoPath() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoUrl() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoHead() }
        verifySuspend(mode = VerifyMode.exactly(1)) { appServer.start() }
    }

    @Test
    fun testFailRepoPathCheck() {
        // GIVEN
        every { gitService.checkMainRepoPath() } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoPath() }
    }

    @Test
    fun testFailRepoUrlCheck() {
        // GIVEN
        every { gitService.checkMainRepoUrl() } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoPath() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoUrl() }
    }

    @Test
    fun testFailRepoHeadCheck() {
        // GIVEN
        every { gitService.checkMainRepoHead() } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoPath() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoUrl() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoHead() }
    }

    @Test
    fun testFailServeServer(): Unit = runBlocking {
        // GIVEN
        everySuspend { appServer.start() } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { command.test() }

        // THEN
        actual.message shouldBe "any exception"

        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoPath() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoUrl() }
        verify(mode = VerifyMode.exactly(1)) { gitService.checkMainRepoHead() }
        verifySuspend(mode = VerifyMode.exactly(1)) { appServer.start() }
    }
}
