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

import TestCreator
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.impl.SyncProjectServiceImpl
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

private val logger = KotlinLogging.from(SyncProjectServiceTest::class)

class SyncProjectServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val repoRoot = "/tmp/main-repo-path".toPath()
    private val gitProps = mock<GitProps> {
        every { mainRepoPath } returns repoRoot.toString()
    }
    private lateinit var syncProjectService: SyncProjectService

    @BeforeTest
    fun setup() {
        syncProjectService = SyncProjectServiceImpl(
            gitProps = gitProps,
        )
    }

    @Test
    fun testIsUpdateRequiredForProjectWithIgnoreExternal() {
        // GIVEN
        val allCases = generateCases()

        @Suppress("ktlint:standard:argument-list-wrapping")
        val updateNotRequiredCases = setOf(
            1, 3, 5, 7, 9, // external
            11, 12, // update not required
            21, 22, 23, 24, 25, // conflict with external by name
        )

        allCases.shouldForAll { (index, compose, repo) ->
            logger.info { "$index: $compose, $repo" }

            // WHEN
            val actual = if (null != compose && null != repo && !compose.name.equals(repo.name, ignoreCase = true)) {
                val actualForCompose = syncProjectService.isUpdateRequiredForProject(
                    compose,
                    null,
                    ignoreExternal = true,
                )
                val actualForRepo = syncProjectService.isUpdateRequiredForProject(null, repo, ignoreExternal = true)
                actualForCompose || actualForRepo
            } else {
                syncProjectService.isUpdateRequiredForProject(compose, repo, ignoreExternal = true)
            }
            // THEN
            if (index in updateNotRequiredCases) {
                actual.shouldBeFalse()
            } else {
                actual.shouldBeTrue()
            }
        }
    }

    @Test
    fun testIsUpdateRequiredForProjectWithoutIgnoreExternal() {
        // GIVEN
        val allCases = generateCases()

        @Suppress("ktlint:standard:argument-list-wrapping")
        val updateNotRequiredCases = setOf(
            11, 12, // update not required
        )

        allCases.shouldForAll { (index, compose, repo) ->
            logger.info { "$index: $compose, $repo" }

            // WHEN
            val actual = if (null != compose && null != repo && !compose.name.equals(repo.name, ignoreCase = true)) {
                val actualForCompose = syncProjectService.isUpdateRequiredForProject(
                    compose,
                    null,
                    ignoreExternal = false,
                )
                val actualForRepo = syncProjectService.isUpdateRequiredForProject(null, repo, ignoreExternal = false)
                actualForCompose || actualForRepo
            } else {
                syncProjectService.isUpdateRequiredForProject(compose, repo, ignoreExternal = false)
            }
            // THEN
            if (index in updateNotRequiredCases) {
                actual.shouldBeFalse()
            } else {
                actual.shouldBeTrue()
            }
        }
    }

    @Test
    fun testAddSyncPlanForProjectWithIgnoreExternal() {
        // GIVEN
        val allCases = generateCases()

        @Suppress("ktlint:standard:argument-list-wrapping")
        val resultCases = listOf(
            setOf(0, 2, 4, 6, 8) to ("toDown" to null), // only actual stacks declared in repo dir
            setOf(1, 3, 5, 7, 9) to ("ignored" to null), // only actual stacks declared outside of repo dir
            setOf(10) to (null to "toUp"), // only expected stacks
            setOf(11, 12, 13, 14, 15) to (null to "toUp"), // stacks with equal name and compose path
            setOf(16, 17, 18, 19, 20) to ("toDown" to "toUp"), // stacks with equal name; actual declared in repo dir
            setOf(21, 22, 23, 24, 25) to ("ignored" to "ignored"), // stacks with equal name; actual declared outside
            setOf(26, 27, 28, 29, 30) to ("toDown" to "toUp"), // stacks with equal compose path
        )

        allCases.shouldForAll { (index, compose, repo) ->
            logger.info { "$index: $compose, $repo" }
            val actual = SyncStackPlan()

            // WHEN
            if (null != compose && null != repo && !compose.name.equals(repo.name, ignoreCase = true)) {
                syncProjectService.addSyncPlanForProject(actual, compose, null, ignoreExternal = true)
                syncProjectService.addSyncPlanForProject(actual, null, repo, ignoreExternal = true)
            } else {
                syncProjectService.addSyncPlanForProject(actual, compose, repo, ignoreExternal = true)
            }

            // THEN
            val (_, result) = resultCases.first { (key, _) -> index in key }
            val expected = SyncStackPlan()
            when (result.first) {
                "toDown" -> expected.toDown.add(compose!!)
                "ignored" -> expected.ignored.add(compose!!)
                null -> Unit
                else -> throw UnsupportedOperationException()
            }
            when (result.second) {
                "toUp" -> expected.toUp.add(repo!!)
                "toDown" -> expected.toDown.add(repo!!)
                "ignored" -> expected.ignored.add(repo!!)
                null -> Unit
                else -> throw UnsupportedOperationException()
            }

            actual.toUp shouldContainExactlyInAnyOrder expected.toUp
            actual.toDown shouldContainExactlyInAnyOrder expected.toDown
            actual.ignored shouldContainExactlyInAnyOrder expected.ignored
        }
    }

    @Test
    fun testAddSyncPlanForProjectWithoutIgnoreExternal() {
        // GIVEN
        val allCases = generateCases()

        @Suppress("ktlint:standard:argument-list-wrapping")
        val resultCases = listOf(
            setOf(0, 2, 4, 6, 8) to ("toDown" to null), // only actual stacks declared in repo dir
            setOf(1, 3, 5, 7, 9) to ("toDown" to null), // only actual stacks declared outside of repo dir
            setOf(10) to (null to "toUp"), // only expected stacks
            setOf(11, 12, 13, 14, 15) to (null to "toUp"), // stacks with equal name and compose path
            setOf(16, 17, 18, 19, 20) to ("toDown" to "toUp"), // stacks with equal name; actual declared in repo dir
            setOf(21, 22, 23, 24, 25) to ("toDown" to "toUp"), // stacks with equal name; actual declared outside
            setOf(26, 27, 28, 29, 30) to ("toDown" to "toUp"), // stacks with equal compose path
        )

        allCases.shouldForAll { (index, compose, repo) ->
            logger.info { "$index: $compose, $repo" }
            val actual = SyncStackPlan()

            // WHEN
            if (null != compose && null != repo && !compose.name.equals(repo.name, ignoreCase = true)) {
                syncProjectService.addSyncPlanForProject(actual, compose, null, ignoreExternal = false)
                syncProjectService.addSyncPlanForProject(actual, null, repo, ignoreExternal = false)
            } else {
                syncProjectService.addSyncPlanForProject(actual, compose, repo, ignoreExternal = false)
            }

            // THEN
            val (_, result) = resultCases.first { (key, _) -> index in key }
            val expected = SyncStackPlan()
            when (result.first) {
                "toDown" -> expected.toDown.add(compose!!)
                "ignored" -> expected.ignored.add(compose!!)
                null -> Unit
                else -> throw UnsupportedOperationException()
            }
            when (result.second) {
                "toUp" -> expected.toUp.add(repo!!)
                "toDown" -> expected.toDown.add(repo!!)
                "ignored" -> expected.ignored.add(repo!!)
                null -> Unit
                else -> throw UnsupportedOperationException()
            }

            actual.toUp shouldContainExactlyInAnyOrder expected.toUp
            actual.toDown shouldContainExactlyInAnyOrder expected.toDown
            actual.ignored shouldContainExactlyInAnyOrder expected.ignored
        }
    }

    private fun generateCases(): Sequence<Triple<Int, ProjectInfo.Actual?, ProjectInfo.Expected?>> {
        val statusCases = sequenceOf(
            mapOf(ContainerState.Status.RUNNING to 5), // all running
            mapOf(
                ContainerState.Status.RUNNING to 2,
                ContainerState.Status.RESTARTING to 2,
                ContainerState.Status.REMOVING to 2,
            ), // all up
            mapOf(ContainerState.Status.DEAD to 5), // all dead
            mapOf(
                ContainerState.Status.CREATED to 2,
                ContainerState.Status.PAUSED to 2,
                ContainerState.Status.EXITED to 2,
                ContainerState.Status.DEAD to 2,
            ), // all down
            mapOf(
                ContainerState.Status.RUNNING to 1,
                ContainerState.Status.RESTARTING to 1,
                ContainerState.Status.REMOVING to 1,
                ContainerState.Status.CREATED to 1,
                ContainerState.Status.PAUSED to 1,
                ContainerState.Status.EXITED to 1,
                ContainerState.Status.DEAD to 1,
            ), // mixed
        )
        val booleanSeq = sequenceOf(true, false)

        val composePresentOnlyCases: Sequence<Pair<ProjectInfo.Actual?, ProjectInfo.Expected?>> = statusCases
            .flatMap { status -> booleanSeq.map { status to it } }
            .map { (composeStatus, isPathInRepo) ->
                TestCreator.actualProjectInfo().copy(
                    name = "project-name",
                    statuses = composeStatus,
                    manifestPath = generateComposePath(isPathInRepo, "project-name"),
                )
            }
            .map { it to null }
        val repoPresentOnlyCases: Sequence<Pair<ProjectInfo.Actual?, ProjectInfo.Expected?>> = sequenceOf(
            null to TestCreator.expectedProjectInfo().copy(
                name = "project-name",
                manifestPath = repoRoot.resolve("project-name/compose.yml"),
            ),
        )
        val allPresentCases: Sequence<Pair<ProjectInfo.Actual?, ProjectInfo.Expected?>> = booleanSeq
            .flatMap { isNameEq -> booleanSeq.map { isNameEq to it } }
            .filter { (isNameEq, isPathEq) -> isNameEq || isPathEq }
            .flatMap { (isNameEq, isPathEq) ->
                if (isPathEq) {
                    sequenceOf(Triple(isNameEq, isPathEq, false))
                } else {
                    booleanSeq.map { Triple(isNameEq, isPathEq, it) }
                }
            }
            .flatMap { (isNameEq, isPathEq, isRepoComposePath) ->
                statusCases.map { composeStatus ->
                    val (composeName, repoName) = if (isNameEq) {
                        "project-name" to "project-name"
                    } else {
                        "compose-name" to "repo-name"
                    }
                    val (composePath, repoPath) = if (isPathEq) {
                        generateComposePath(true, "project-name") to generateComposePath(true, "project-name")
                    } else {
                        generateComposePath(isRepoComposePath, "compose-name") to generateComposePath(true, "repo-name")
                    }
                    val compose = TestCreator.actualProjectInfo().copy(
                        name = composeName,
                        statuses = composeStatus,
                        manifestPath = composePath,
                    )
                    val repo = TestCreator.expectedProjectInfo().copy(
                        name = repoName,
                        manifestPath = repoPath,
                    )
                    compose to repo
                }
            }

        return (composePresentOnlyCases + repoPresentOnlyCases + allPresentCases)
            .mapIndexed { index, (compose, repo) -> Triple(index, compose, repo) }
    }

    private fun generateComposePath(isPathInRepo: Boolean, name: String): Path = if (isPathInRepo) {
        repoRoot.resolve("$name/compose.yml")
    } else {
        "/tmp/$name/compose.yml".toPath()
    }
}
