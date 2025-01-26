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
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.ext.dotenv.okioFileReader
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitCryptClientImpl
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldNotContain
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

private val logger = KotlinLogging.from(GitCryptClientTest::class)

class GitCryptClientTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private lateinit var repoRoot: Path
    private lateinit var gitCryptKey: Path
    private val gitCryptClient: GitCryptClient = GitCryptClientImpl()

    @BeforeTest
    fun setup(): Unit = runBlocking {
        val pwdPath = FileSystem.SYSTEM.canonicalize("./".toPath())

        val gitTokenFile = pwdPath.resolve("../.git-token", normalize = true)
        gitCryptKey = pwdPath.resolve("../.git-crypt-key", normalize = true)

        repoRoot = pwdPath.resolve("build/repo", normalize = true)
        FileSystem.SYSTEM.deleteRecursively(repoRoot, false)

        val gitClient = GitClientImpl(pwdPath.resolve("../GIT_ASKPASS", normalize = true).toString())
        gitClient.cloneRepo(
            "https://github.com/xzima/home-composes.git",
            repoRoot.toString(),
            gitTokenFile = gitTokenFile.toString(),
        )
    }

    @Test
    fun testVersion(): Unit = runBlocking {
        // WHEN
        val actual = gitCryptClient.version()

        // THEN
        actual.shouldNotBeNull() shouldBe "0.7.0"
    }

    @Test
    fun testListEncryptedFiles(): Unit = runBlocking {
        // WHEN
        val actual = gitCryptClient.getEncryptedFiles(repoRoot.toString())

        // THEN
        actual.shouldNotBeNull() shouldContainAll listOf(
            "global.secret.env",
            "tor/bridges.secret.txt",
            "tor/pwd.secret.php",
            "tor/torrc.secret.txt",
            "traefik/duck_dns_token.secret.txt",
            "traefik/user.secret.txt",
        )
    }

    @Test
    fun testLockUnlockRepo(): Unit = runBlocking {
        // THEN-0
        var content = okioFileReader(repoRoot.resolve("global.secret.env")).joinToString(separator = "\n")
        content shouldContain "GITCRYPT"

        // WHEN-1
        gitCryptClient.unlockRepo(repoRoot.toString(), gitCryptKey.toString())
        // THEN-1
        content = okioFileReader(repoRoot.resolve("global.secret.env")).joinToString(separator = "\n")
        content shouldNotContain "GITCRYPT"

        // WHEN-2
        gitCryptClient.lockRepo(repoRoot.toString())
        // THEN-2
        content = okioFileReader(repoRoot.resolve("global.secret.env")).joinToString(separator = "\n")
        content shouldContain "GITCRYPT"
    }
}
