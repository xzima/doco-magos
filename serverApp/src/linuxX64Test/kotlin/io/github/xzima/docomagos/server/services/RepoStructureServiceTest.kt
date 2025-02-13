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
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.props.RepoStructureProps
import io.github.xzima.docomagos.server.services.impl.RepoStructureServiceImpl
import io.github.xzima.docomagos.server.services.models.RepoInfo
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import okio.*
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class RepoStructureServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)

        private val repoStructureProps = object : RepoStructureProps {
            override val envPattern = Regex("""\.env$""")
            override val secretEnvPattern = Regex("""\.secret\.env$""")
            override val globalEnvPattern = Regex("""^global(\.secret)?\.env$""")
            override val projectNamePattern = Regex("""^((?<order>\d*)_)?(?<name>[-_\w]*)$""")
            override val composeNamePattern = Regex("""^(docker-)?compose.ya?ml$""")
        }
    }

    private lateinit var repoStructureService: RepoStructureService

    @BeforeTest
    fun setup() {
        repoStructureService = RepoStructureServiceImpl(
            repoProps = repoStructureProps,
        )
    }

    @Test
    fun testBaseInfo(): Unit = runBlocking {
        // GIVEN
        val repoRoot = FileSystem.SYSTEM.canonicalize(TestUtils.testResourcesDir.resolve("test-repo-structure/full"))
        // WHEN
        val actual = repoStructureService.getBaseInfo(repoRoot)

        // THEN
        actual shouldBe RepoInfo.BaseRepoInfo(
            path = repoRoot,
            encryptionKeyFilePath = null,
            secretEnvPath = repoRoot.resolve("global.secret.env"),
            envPath = repoRoot.resolve("global.env"),
        )
    }

    @Test
    fun testBaseInfoWithEncryptionKey(): Unit = runBlocking {
        // GIVEN
        val repoRoot = FileSystem.SYSTEM.canonicalize(TestUtils.testResourcesDir.resolve("test-repo-structure/empty"))
        // WHEN
        val actual = repoStructureService.getBaseInfo(repoRoot, repoRoot.resolve("secret-key"))

        // THEN
        actual shouldBe RepoInfo.BaseRepoInfo(
            path = repoRoot,
            encryptionKeyFilePath = repoRoot.resolve("secret-key"),
            secretEnvPath = null,
            envPath = null,
        )
    }

    @Test
    fun testFullInfo(): Unit = runBlocking {
        // GIVEN
        val repoRoot = FileSystem.SYSTEM.canonicalize(TestUtils.testResourcesDir.resolve("test-repo-structure/full"))
        // WHEN
        val actual = repoStructureService.getFullInfo(repoRoot)

        // THEN
        actual shouldBe RepoInfo.FullRepoInfo(
            path = repoRoot,
            encryptionKeyFilePath = null,
            secretEnvPath = repoRoot.resolve("global.secret.env"),
            envPath = repoRoot.resolve("global.env"),
            projects = listOf(
                RepoInfo.FullRepoInfo.RepoProjectInfo(
                    name = "simple_order_with_zero",
                    path = repoRoot.resolve("002_simple_order_with_zero"),
                    order = 2,
                    manifestPath = repoRoot.resolve("002_simple_order_with_zero/compose.yml"),
                    secretEnvPath = null,
                    envPath = repoRoot.resolve("002_simple_order_with_zero/any.env"),
                ),
                RepoInfo.FullRepoInfo.RepoProjectInfo(
                    name = "another-simple_order",
                    path = repoRoot.resolve("101_another-simple_order"),
                    order = 101,
                    manifestPath = repoRoot.resolve("101_another-simple_order/docker-compose.yml"),
                    secretEnvPath = repoRoot.resolve("101_another-simple_order/any.secret.env"),
                    envPath = null,
                ),
                RepoInfo.FullRepoInfo.RepoProjectInfo(
                    name = "simple-order",
                    path = repoRoot.resolve("1_simple-order"),
                    order = 1,
                    manifestPath = repoRoot.resolve("1_simple-order/compose.yaml"),
                    secretEnvPath = repoRoot.resolve("1_simple-order/.secret.env"),
                    envPath = repoRoot.resolve("1_simple-order/.env"),
                ),
                RepoInfo.FullRepoInfo.RepoProjectInfo(
                    name = "simple",
                    path = repoRoot.resolve("simple"),
                    order = Int.MAX_VALUE,
                    manifestPath = repoRoot.resolve("simple/docker-compose.yaml"),
                    secretEnvPath = null,
                    envPath = null,
                ),
            ),
        )
    }

    @Test
    fun testFullInfoWithEncryptionKey(): Unit = runBlocking {
        // GIVEN
        val repoRoot = FileSystem.SYSTEM.canonicalize(TestUtils.testResourcesDir.resolve("test-repo-structure/empty"))
        // WHEN
        val actual = repoStructureService.getFullInfo(repoRoot, repoRoot.resolve("secret-key"))

        // THEN
        actual shouldBe RepoInfo.FullRepoInfo(
            path = repoRoot,
            encryptionKeyFilePath = repoRoot.resolve("secret-key"),
            secretEnvPath = null,
            envPath = null,
            projects = emptyList(),
        )
    }
}
