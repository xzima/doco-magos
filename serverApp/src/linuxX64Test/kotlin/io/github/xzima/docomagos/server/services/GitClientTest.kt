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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldNotBeBlank
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class GitClientTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private lateinit var repoRoot: Path
    private lateinit var gitTokenFile: Path
    private lateinit var gitClient: GitClient

    @BeforeTest
    fun setup() {
        val pwdPath = FileSystem.SYSTEM.canonicalize("./".toPath())

        gitTokenFile = pwdPath.resolve("../.git-token", normalize = true)

        repoRoot = pwdPath.resolve("build/repo", normalize = true)
        FileSystem.SYSTEM.deleteRecursively(repoRoot, false)

        gitClient = GitClientImpl(pwdPath.resolve("../GIT_ASKPASS", normalize = true).toString())
    }

    @Test
    fun testVersion() {
        // WHEN
        val actual = gitClient.version()

        // THEN
        actual.shouldNotBeNull().version shouldBe "2.48.1"
    }

    @Test
    fun testCloneWithoutCredentials() {
        // WHEN
        val actual = gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
        )

        // THEN
        actual shouldBe false
    }

    @Test
    fun testClonePositive() {
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
    fun testCloneAlreadyCloned() {
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
    fun testCloneEmptyDirExist() {
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
    fun testCloneDirWithFilesExist() {
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
    fun testGetRepoPathPositive() {
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
    fun testGetRepoPathFromParent() {
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
    fun testGetRepoPathFromChild() {
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
    fun testGetRepoPathFromNotGitDirectory() {
        // WHEN
        val actual = shouldThrow<RuntimeException> { gitClient.getRepoPathBy("/tmp") }

        // THEN
        actual.shouldNotBeNull().message shouldBe
            "command failed. status: 128 message: fatal: not a git repository (or any parent up to mount point /)\n" +
            "Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).\n"
    }

    @Test
    fun testGetRepoUrlPositive() {
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
    fun testGetRepoUrlWithUnexpectedRemote() {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitClient.getRepoUrlBy(repoRoot.toString(), "strange-remote") }

        // THEN
        actual.message shouldBe
            "command failed. status: 2 message: error: No such remote 'strange-remote'\n"
    }

    @Test
    fun testGetRepoFromNotGitDirectory() {
        // WHEN
        val actual = shouldThrow<RuntimeException> { gitClient.getRepoUrlBy("/tmp", "origin") }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: fatal: not a git repository (or any parent up to mount point /)\n" +
            "Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).\n"
    }

    @Test
    fun testFetchPositive() {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        gitClient.fetchRemote(repoRoot.toString(), "origin", gitTokenFile = gitTokenFile.toString())
    }

    @Test
    fun testFetchWithoutCredentials() {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = shouldThrow<RuntimeException> { gitClient.fetchRemote(repoRoot.toString(), "origin") }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: " +
            "fatal: could not read Username for 'https://github.com': terminal prompts disabled\n"
    }

    @Test
    fun testFetchWithUnexpectedRemote() {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = shouldThrow<RuntimeException> {
            gitClient.fetchRemote(repoRoot.toString(), "strange-remote", gitTokenFile = gitTokenFile.toString())
        }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: fatal: 'strange-remote' does not appear to be a git repository\n" +
            "fatal: Could not read from remote repository.\n" +
            "\n" +
            "Please make sure you have the correct access rights\n" +
            "and the repository exists.\n"
    }

    @Test
    fun testFetchFromNotGitDirectory() {
        // WHEN
        val actual = shouldThrow<RuntimeException> {
            gitClient.fetchRemote("/tmp", "origin", gitTokenFile = gitTokenFile.toString())
        }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: fatal: not a git repository (or any parent up to mount point /)\n" +
            "Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).\n"
    }

    @Test
    fun testHardResetHeadPositive() {
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
    fun testHardResetHeadWithUnexpectedRef() {
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
    fun testHardResetHeadFromNotGitDirectory() {
        // WHEN
        val actual = gitClient.hardResetHeadToRef("/tmp", "origin/master")

        // THEN
        actual shouldBe false
    }

    @Test
    fun testGetLastCommitWithRemoteRef() {
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
    fun testGetLastCommitWithHeadRef() {
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
    fun testGetLastCommitWithUnexpectedRef() {
        // GIVEN
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )

        // WHEN
        val actual = shouldThrow<RuntimeException> {
            gitClient.getLastCommitByRef(repoRoot.toString(), "origin/strange-branch")
        }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: fatal: ambiguous argument 'origin/strange-branch': " +
            "unknown revision or path not in the working tree.\n" +
            "Use '--' to separate paths from revisions, like this:\n" +
            "'git <command> [<revision>...] -- [<file>...]'\n"
    }

    @Test
    fun testGetLastCommitFromNotGitDirectory() {
        // WHEN
        val actual = shouldThrow<RuntimeException> { gitClient.getLastCommitByRef("/tmp", "origin/master") }

        // THEN
        actual.message shouldBe
            "command failed. status: 128 message: fatal: not a git repository (or any parent up to mount point /)\n" +
            "Stopping at filesystem boundary (GIT_DISCOVERY_ACROSS_FILESYSTEM not set).\n"
    }
}
