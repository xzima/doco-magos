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

import dev.mokkery.MockMode
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.impl.GitServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GitServiceTest {

    private val gitClient = mock<GitClient>(MockMode.autoUnit)
    private val gitEnv = object : GitProps {
        override val mainRepoPath: String = "/tmp/repo"
        override val mainRepoUrl: String = "https://github.com/xzima/home-composes.git"
        override val mainRepoRemote: String = "origin"
        override val mainRepoBranch: String = "master"
        override val gitAskPass: String = "/tmp/GIT_ASKPASS"
        override val gitToken: String? = "any-token"
        override val gitTokenFile: String? = "/tmp/.git-token"
        override val gitCryptKeyFile: String? = "/tmp/.git-crypt-key"
    }
    private lateinit var gitService: GitService

    @BeforeTest
    fun setUp() {
        gitService = GitServiceImpl(gitEnv, gitClient)
    }

    @AfterTest
    fun afterTest() {
        verifyNoMoreCalls(gitClient)
        resetCalls(gitClient)
        resetAnswers(gitClient)
    }

    @Test
    fun testCheckMainRepoPathPositive() {
        // GIVEN
        every { gitClient.getRepoPathBy(gitEnv.mainRepoPath) } returns gitEnv.mainRepoPath

        // WHEN
        gitService.checkMainRepoPath()

        // THEN
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoPathBy(gitEnv.mainRepoPath) }
    }

    @Test
    fun testCheckMainRepoPathNegativeGetPath() {
        // GIVEN
        every { gitClient.getRepoPathBy(gitEnv.mainRepoPath) } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.checkMainRepoPath() }

        // THEN
        actual.message shouldContain "any exception"
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoPathBy(gitEnv.mainRepoPath) }
    }

    @Test
    fun testCheckMainRepoPathNegativeUnexpectedPath() {
        // GIVEN
        every { gitClient.getRepoPathBy(gitEnv.mainRepoPath) } returns "strange-path"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.checkMainRepoPath() }

        // THEN
        actual.message shouldContain "unexpected git repo path. expected: ${gitEnv.mainRepoPath} actual: strange-path"
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoPathBy(gitEnv.mainRepoPath) }
    }

    @Test
    fun testCheckMainRepoUrlPositive() {
        // GIVEN
        every { gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote) } returns gitEnv.mainRepoUrl

        // WHEN
        gitService.checkMainRepoUrl()
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote) }
    }

    @Test
    fun testCheckMainRepoUrlNegativeGetUrl() {
        // GIVEN
        every {
            gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote)
        } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.checkMainRepoUrl() }

        // THEN
        actual.message shouldContain "any exception"
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote) }
    }

    @Test
    fun testCheckMainRepoUrlNegativeUnexpectedUrl() {
        // GIVEN
        every { gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote) } returns "strange-url"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.checkMainRepoUrl() }

        // THEN
        actual.message shouldContain "unexpected git repo url. expected: ${gitEnv.mainRepoUrl} actual: strange-url"
        verify(mode = VerifyMode.exactly(1)) { gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote) }
    }

    @Test
    fun testCheckMainRepoHeadPositive() {
        // GIVEN
        every {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
        } returns Unit
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        } returns null

        // WHEN
        gitService.checkMainRepoHead()
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        }
    }

    @Test
    fun testCheckMainRepoHeadNegativeFetch() {
        // GIVEN
        every {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
        } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.checkMainRepoHead() }

        // THEN
        actual.message shouldContain "any exception"
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
        }
    }

    @Test
    fun testCheckMainRepoHeadNegativeRef() {
        // GIVEN
        every {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
        } returns Unit
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.checkMainRepoHead() }

        // THEN
        actual.message shouldContain "any exception"
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.fetchRemote(gitEnv.mainRepoPath, gitEnv.mainRepoRemote, gitEnv.gitToken, gitEnv.gitTokenFile)
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        }
    }

    @Test
    fun testIsActualRepoHeadPositiveActual() {
        // GIVEN
        every { gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME) } returns "commit-1"
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        } returns "commit-1"

        // WHEN
        val actual = gitService.isActualRepoHead()

        // THEN
        actual shouldBe true
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        }
    }

    @Test
    fun testIsActualRepoHeadPositiveNotActual() {
        // GIVEN
        every { gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME) } returns "commit-1"
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        } returns "commit-2"

        // WHEN
        val actual = gitService.isActualRepoHead()

        // THEN
        actual shouldBe false
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        }
    }

    @Test
    fun testIsActualRepoHeadNegativeHead() {
        // GIVEN
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
        } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.isActualRepoHead() }

        // THEN
        actual.message shouldBe "any exception"
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
        }
    }

    @Test
    fun testIsActualRepoHeadNegativeRemote() {
        // GIVEN
        every { gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME) } returns "commit-1"
        every {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        } throws Exception("any exception")

        // WHEN
        val actual = shouldThrow<Exception> { gitService.isActualRepoHead() }

        // THEN
        actual.message shouldBe "any exception"
        verify(mode = VerifyMode.exactly(1)) {
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
            gitClient.getLastCommitByRef(gitEnv.mainRepoPath, "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}")
        }
    }
}
