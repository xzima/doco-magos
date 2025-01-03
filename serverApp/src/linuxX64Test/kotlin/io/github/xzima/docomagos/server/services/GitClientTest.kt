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

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldNotBeBlank
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.BeforeTest
import kotlin.test.Test

class GitClientTest {

    private lateinit var repoRoot: Path
    private lateinit var gitTokenFile: Path
    private lateinit var gitClient: GitClient

    @BeforeTest
    fun setup() {
        configureLogging(Level.TRACE)
        val pwdPath = Command("pwd")
            .stdout(Stdio.Pipe).stderr(Stdio.Pipe)
            .spawn().waitWithOutput()
            .stdout!!
            .trim().toPath()

        gitTokenFile = pwdPath.resolve("../.git-token", normalize = true)

        repoRoot = pwdPath.resolve("build/repo", normalize = true)
        FileSystem.SYSTEM.deleteRecursively(repoRoot, false)

        gitClient = GitClient(pwdPath.resolve("../GIT_ASKPASS", normalize = true).toString())
    }

    @Test
    fun testVersion(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.version()

        // THEN
        actual.shouldNotBeNull().version shouldBe "2.47.1"
    }

    @Test
    fun testCloneWithoutCredentials(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
        )

        // THEN
        actual shouldBe false
    }

    @Test
    fun testClonePositive(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // THEN
        actual shouldBe true
    }

    @Test
    fun testCloneAlreadyCloned(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // THEN
        actual shouldBe false
    }

    @Test
    fun testCloneEmptyDirExist(): Unit = runBlocking {
        // GIVEN
        FileSystem.SYSTEM.createDirectory(repoRoot)

        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // THEN
        actual shouldBe true
    }

    @Test
    fun testCloneDirWithFilesExist(): Unit = runBlocking {
        // GIVEN
        FileSystem.SYSTEM.createDirectory(repoRoot)
        FileSystem.SYSTEM.write(repoRoot.resolve("any-file.txt")) {
            writeUtf8("any-file.txt")
        }

        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // THEN
        actual shouldBe false
    }

    @Test
    fun testGetRepoPathPositive(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getRepoPathBy(repoRoot.toString())

        // THEN
        actual.shouldNotBeNull() shouldBe repoRoot.toString()
    }

    @Test
    fun testGetRepoPathFromParent(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getRepoPathBy(repoRoot.parent.toString())

        // THEN
        actual.shouldNotBeNull() shouldEndWith "doco-magos" // because find root project repo
    }

    @Test
    fun testGetRepoPathFromChild(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getRepoPathBy(repoRoot.resolve("homepage").toString())

        // THEN
        actual.shouldNotBeNull() shouldEndWith repoRoot.toString()
    }

    @Test
    fun testGetRepoPathFromNotGitDirectory(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.getRepoPathBy("/tmp")

        // THEN
        actual.shouldBeNull()
    }

    @Test
    fun testGetRepoUrlPositive(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getRepoUrlBy(repoRoot.toString(), "origin")

        // THEN
        actual.shouldNotBeNull() shouldBe "https://github.com/xzima/home-composes.git"
    }

    @Test
    fun testGetRepoUrlWithUnexpectedRemote(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getRepoUrlBy(repoRoot.toString(), "strange-remote")

        // THEN
        actual.shouldBeNull()
    }

    @Test
    fun testGetRepoFromNotGitDirectory(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.getRepoUrlBy("/tmp", "origin")

        // THEN
        actual.shouldBeNull()
    }

    @Test
    fun testFetchPositive(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.fetchRemote(repoRoot.toString(), "origin", gitTokenFile = gitTokenFile.toString())

        // THEN
        actual shouldBe true
    }

    @Test
    fun testFetchWithoutCredentials(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.fetchRemote(repoRoot.toString(), "origin")

        // THEN
        actual shouldBe false
    }

    @Test
    fun testFetchWithUnexpectedRemote(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.fetchRemote(
            repoRoot.toString(),
            "strange-remote",
            gitTokenFile = gitTokenFile.toString(),
        )

        // THEN
        actual shouldBe false
    }

    @Test
    fun testFetchFromNotGitDirectory(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.fetchRemote("/tmp", "origin", gitTokenFile = gitTokenFile.toString())

        // THEN
        actual shouldBe false
    }

    @Test
    fun testHardResetHeadPositive(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.hardResetHeadToRef(repoRoot.toString(), "origin/master")

        // THEN
        actual shouldBe true
    }

    @Test
    fun testHardResetHeadWithUnexpectedRef(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.hardResetHeadToRef(repoRoot.toString(), "origin/strange-branch")

        // THEN
        actual shouldBe false
    }

    @Test
    fun testHardResetHeadFromNotGitDirectory(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.hardResetHeadToRef("/tmp", "origin/master")

        // THEN
        actual shouldBe false
    }

    @Test
    fun testGetLastCommitWithRemoteRef(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getLastCommitByRef(repoRoot.toString(), "origin/master")

        // THEN
        actual.shouldNotBeBlank()
    }

    @Test
    fun testGetLastCommitWithHeadRef(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getLastCommitByRef(repoRoot.toString(), GitClient.HEAD_REF_NAME)

        // THEN
        actual.shouldNotBeBlank()
    }

    @Test
    fun testGetLastCommitWithUnexpectedRef(): Unit = runBlocking {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = gitClient.getLastCommitByRef(repoRoot.toString(), "origin/strange-branch")

        // THEN
        actual.shouldBeNull()
    }

    @Test
    fun testGetLastCommitFromNotGitDirectory(): Unit = runBlocking {
        // WHEN
        val actual = gitClient.getLastCommitByRef("/tmp", "origin/master")

        // THEN
        actual.shouldBeNull()
    }
}
