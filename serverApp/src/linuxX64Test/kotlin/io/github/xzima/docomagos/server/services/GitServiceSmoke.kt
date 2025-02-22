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

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldStartWith
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class GitServiceSmoke {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val gitEnv = mock<GitProps>()
    private lateinit var gitClient: GitClient
    private lateinit var gitService: GitService

    @BeforeTest
    fun setUp() {
        val pwdPath = FileSystem.SYSTEM.canonicalize("./".toPath())
        val repoRoot = pwdPath.resolve("build/repo", normalize = true)
        FileSystem.SYSTEM.deleteRecursively(repoRoot, false)

        every { gitEnv.mainRepoUrl } returns "https://github.com/xzima/home-composes.git"
        every { gitEnv.mainRepoRemote } returns "origin"
        every { gitEnv.mainRepoBranch } returns "master"
        every { gitEnv.mainRepoPath } returns repoRoot.toString()
        every { gitEnv.gitAskPass } returns pwdPath.resolve("../GIT_ASKPASS", normalize = true).toString()
        every { gitEnv.gitTokenFile } returns pwdPath.resolve("../.git-token", normalize = true).toString()
        every { gitEnv.gitToken } returns null
        every { gitEnv.gitCryptKeyFile } returns null

        gitClient = GitClientImpl(gitEnv.gitAskPass)
        gitService = GitServiceImpl(gitEnv, gitClient)
    }

    @AfterTest
    fun afterTest() {
        resetCalls(gitEnv)
        resetAnswers(gitEnv)
    }

    @Test
    fun testActualizeMainRepoWhenPathNotExists() {
        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldBe "${gitEnv.mainRepoPath} is file or not exists"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsFile() {
        // GIVEN
        FileSystem.SYSTEM.write(gitEnv.mainRepoPath.toPath(), true) {
            writeUtf8("Hello from tests")
        }

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldBe "${gitEnv.mainRepoPath} is file or not exists"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsDirEmpty() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)

        // WHEN
        gitService.actualizeMainRepo()

        // THEN
        FileSystem.SYSTEM.metadata(repoPath.resolve(".git")).isDirectory.shouldBeTrue()
    }

    @Test
    fun testActualizeMainRepoWhenPathIsDirEmptyButWithoutCredentials() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        every { gitEnv.gitTokenFile } returns null

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldStartWith "command failed\n" +
            "status: 128\n" +
            "message: Cloning into '${gitEnv.mainRepoPath}'...\n" +
            "fatal: could not read Username"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsDirEmptyButRemoteNotExists() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        every { gitEnv.mainRepoRemote } returns "remote-not-exists"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        val revision = "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}"
        actual.message shouldStartWith "command failed\n" +
            "status: 128\n" +
            "message: fatal: ambiguous argument '$revision': unknown revision or path not in the working tree."
    }

    @Test
    fun testActualizeMainRepoWhenPathIsDirEmptyButBranchNotExists() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        every { gitEnv.mainRepoBranch } returns "branch-not-exists"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        val revision = "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}"
        actual.message shouldStartWith "command failed\n" +
            "status: 128\n" +
            "message: fatal: ambiguous argument '$revision': unknown revision or path not in the working tree."
    }

    @Test
    fun testActualizeMainRepoWhenPathIsDirWithFiles() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        FileSystem.SYSTEM.write(repoPath.resolve(".file"), true) {
            writeUtf8("Hello from tests")
        }

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldStartWith "unexpected git repo path. expected: ${gitEnv.mainRepoPath} actual:"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsGitDirAndRemoteNotExists() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        gitClient.cloneRepo(gitEnv.mainRepoUrl, gitEnv.mainRepoPath, gitTokenFile = gitEnv.gitTokenFile)
        every { gitEnv.mainRepoRemote } returns "remote-not-exists"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldBe "command failed\n" +
            "status: 2\n" +
            "message: error: No such remote 'remote-not-exists'\n"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsGitDirButWithoutCredentials() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        gitClient.cloneRepo(gitEnv.mainRepoUrl, gitEnv.mainRepoPath, gitTokenFile = gitEnv.gitTokenFile)
        every { gitEnv.gitTokenFile } returns null

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldStartWith "command failed\n" +
            "status: 128\n" +
            "message: fatal: could not read Username"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsGitDirAndBranchNotExists() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        gitClient.cloneRepo(gitEnv.mainRepoUrl, gitEnv.mainRepoPath, gitTokenFile = gitEnv.gitTokenFile)
        every { gitEnv.mainRepoBranch } returns "branch-not-exists"

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitService.actualizeMainRepo() }

        // THEN
        actual.message shouldStartWith "command failed\n" +
            "status: 128\n" +
            "message: fatal: ambiguous argument 'origin/branch-not-exists': unknown revision or path not in the working tree.\n"
    }

    @Test
    fun testActualizeMainRepoWhenPathIsGitDir() {
        // GIVEN
        val repoPath = gitEnv.mainRepoPath.toPath()
        FileSystem.SYSTEM.createDirectory(repoPath, true)
        gitClient.cloneRepo(gitEnv.mainRepoUrl, gitEnv.mainRepoPath, gitTokenFile = gitEnv.gitTokenFile)
        val expectedCommit = gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
        gitClient.hardResetHeadToRef(gitEnv.mainRepoPath, "${GitClient.HEAD_REF_NAME}~10")

        // WHEN
        gitService.actualizeMainRepo()

        // THEN
        val actualCommit = gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
        actualCommit shouldBe expectedCommit
    }
}
