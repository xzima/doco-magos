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

import TestUtils
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.impl.FileReadServiceImpl
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.RepoInfo
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class FileReadServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val gitCryptClient = mock<GitCryptClient>()
    private lateinit var fileReadService: FileReadService

    @BeforeTest
    fun setup() {
        fileReadService = FileReadServiceImpl(
            gitCryptClient = gitCryptClient,
        )
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(gitCryptClient)
        resetCalls(gitCryptClient)
        resetAnswers(gitCryptClient)
    }

    @Test
    fun testCheckRepoEncryptionWithoutKeyPositive(): Unit = runBlocking {
        // GIVEN
        val repoPath = "/tmp/test-repo"
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns emptySet()

        // WHEN
        fileReadService.checkRepoEncryption(repoPath, null)

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath) }
    }

    @Test
    fun testCheckRepoEncryptionWithoutKeyNegative(): Unit = runBlocking {
        // GIVEN
        val repoPath = "/tmp/test-repo"
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf("any-file")

        // WHEN
        val actual = shouldThrow<RuntimeException> { fileReadService.checkRepoEncryption(repoPath, null) }

        // THEN
        actual.message shouldBe "For repo(/tmp/test-repo) has encrypted files, but key file not specified: [any-file]"
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath) }
    }

    @Test
    fun testCheckRepoEncryptionWithoutKeyFailed(): Unit = runBlocking {
        // GIVEN
        val repoPath = "/tmp/test-repo"
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { fileReadService.checkRepoEncryption(repoPath, null) }

        // THEN
        actual.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath) }
    }

    @Test
    fun testCheckRepoEncryptionPositive(): Unit = runBlocking {
        // GIVEN
        val repoPath = "/tmp/test-repo"
        val repoKeyPath = "/tmp/test-repo-key"
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } returns Unit

        // WHEN
        fileReadService.checkRepoEncryption(repoPath, repoKeyPath)

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.unlockRepo(repoPath, repoKeyPath) }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath) }
    }

    @Test
    fun testCheckRepoEncryptionFailed(): Unit = runBlocking {
        // GIVEN
        val repoPath = "/tmp/test-repo"
        val repoKeyPath = "/tmp/test-repo-key"
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { fileReadService.checkRepoEncryption(repoPath, repoKeyPath) }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.unlockRepo(repoPath, repoKeyPath) }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath) }
    }

    @Test
    fun testReadAndMergeRepoEnvsWithoutDecryption(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = null,
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("global.env"),
        )

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "***",
            "OVERRIDE_VAL1" to "***",
            "OVERRIDE_VAL2" to "***",
            "OVERRIDE_VAL3" to "***",
            "OVERRIDE_VAL4" to "global.env",
        )
    }

    @Test
    fun testReadAndMergeRepoEnvsWithoutEncryptedFiles(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("global.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns emptySet()

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "***",
            "OVERRIDE_VAL1" to "***",
            "OVERRIDE_VAL2" to "***",
            "OVERRIDE_VAL3" to "***",
            "OVERRIDE_VAL4" to "global.env",
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
    }

    @Test
    fun testReadAndMergeRepoEnvsWithoutEncryptedEnvs(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("global.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "another-file",
        )

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "***",
            "OVERRIDE_VAL1" to "***",
            "OVERRIDE_VAL2" to "***",
            "OVERRIDE_VAL3" to "***",
            "OVERRIDE_VAL4" to "global.env",
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
    }

    @Test
    fun testReadAndMergeRepoEnvsPositive(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("global.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "global.secret.env",
        )
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } returns Unit

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "***",
            "OVERRIDE_VAL1" to "***",
            "OVERRIDE_VAL2" to "***",
            "OVERRIDE_VAL3" to "***",
            "OVERRIDE_VAL4" to "global.env",
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            gitCryptClient.unlockRepo(repoPath.toString(), "/tmp/test-secret-key")
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath.toString()) }
    }

    @Test
    fun testReadAndMergeProjectEnvsPositive(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = ProjectInfo.Expected(
            name = "simple-order",
            manifestPath = repoPath.resolve("1_simple-order/compose.yaml"),
            order = 1,
            stackPath = repoPath.resolve("1_simple-order"),
            repoPath = repoPath,
            repoEncryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            repoEnvPath = repoPath.resolve("global.env"),
            repoSecretEnvPath = repoPath.resolve("global.secret.env"),
            projectEnvPath = repoPath.resolve("1_simple-order/.env"),
            projectSecretEnvPath = repoPath.resolve("1_simple-order/.secret.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "global.secret.env",
        )
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } returns Unit

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo, maskSecrets = false)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "secret-val",
            "PROJECT_KEY" to "project-val",
            "PROJECT_SECRET_KEY" to "project-secret-val",
            "OVERRIDE_VAL1" to "project-secret.env",
            "OVERRIDE_VAL2" to "project.env",
            "OVERRIDE_VAL3" to "global-secret.env",
            "OVERRIDE_VAL4" to "global.env",
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            gitCryptClient.unlockRepo(repoPath.toString(), "/tmp/test-secret-key")
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath.toString()) }
    }

    @Test
    fun testReadAndMergeRepoEnvsFailedUnlock(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = ProjectInfo.Expected(
            name = "simple-order",
            manifestPath = repoPath.resolve("1_simple-order/compose.yaml"),
            order = 1,
            stackPath = repoPath.resolve("1_simple-order"),
            repoPath = repoPath,
            repoEncryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            repoEnvPath = repoPath.resolve("global.env"),
            repoSecretEnvPath = repoPath.resolve("global.secret.env"),
            projectEnvPath = repoPath.resolve("1_simple-order/.env"),
            projectSecretEnvPath = repoPath.resolve("1_simple-order/.secret.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "global.secret.env",
        )
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { fileReadService.readAndMergeEnvs(repoInfo, maskSecrets = false) }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            gitCryptClient.unlockRepo(repoPath.toString(), "/tmp/test-secret-key")
        }
    }

    @Test
    fun testReadAndMergeRepoEnvsFailedLock(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("global.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "global.secret.env",
        )
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = fileReadService.readAndMergeEnvs(repoInfo)

        // THEN
        actual shouldBe mapOf(
            "KEY" to "val",
            "SECRET_KEY" to "***",
            "OVERRIDE_VAL1" to "***",
            "OVERRIDE_VAL2" to "***",
            "OVERRIDE_VAL3" to "***",
            "OVERRIDE_VAL4" to "global.env",
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            gitCryptClient.unlockRepo(repoPath.toString(), "/tmp/test-secret-key")
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath.toString()) }
    }

    @Test
    fun testReadAndMergeRepoEnvsFailedReadEnvs(): Unit = runBlocking {
        // GIVEN
        val repoPath = TestUtils.testResourcesDir.resolve("test-repo-structure/full")
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoPath,
            encryptionKeyFilePath = "/tmp/test-secret-key".toPath(),
            secretEnvPath = repoPath.resolve("global.secret.env"),
            envPath = repoPath.resolve("404.env"),
        )
        everySuspend { gitCryptClient.getEncryptedFiles(any()) } returns setOf(
            "global.secret.env",
        )
        everySuspend { gitCryptClient.unlockRepo(any(), any()) } returns Unit
        everySuspend { gitCryptClient.lockRepo(any()) } returns Unit

        // WHEN
        val actual = shouldThrow<FileNotFoundException> { fileReadService.readAndMergeEnvs(repoInfo) }

        // THEN
        actual.message shouldBe "No such file or directory"

        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.getEncryptedFiles(repoPath.toString()) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            gitCryptClient.unlockRepo(repoPath.toString(), "/tmp/test-secret-key")
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { gitCryptClient.lockRepo(repoPath.toString()) }
    }
}
