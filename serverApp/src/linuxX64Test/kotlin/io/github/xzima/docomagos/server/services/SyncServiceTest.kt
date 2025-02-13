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
import dev.mokkery.answering.calls
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.every
import dev.mokkery.everySuspend
import dev.mokkery.matcher.any
import dev.mokkery.matcher.logical.and
import dev.mokkery.matcher.matching
import dev.mokkery.mock
import dev.mokkery.resetAnswers
import dev.mokkery.resetCalls
import dev.mokkery.verify
import dev.mokkery.verify.VerifyMode
import dev.mokkery.verifyNoMoreCalls
import dev.mokkery.verifySuspend
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.impl.SyncServiceImpl
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.RepoInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

class SyncServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val gitProps = mock<GitProps>()
    private val appProps = mock<AppProps>()
    private val repoService = mock<RepoService>()
    private val syncProjectService = mock<SyncProjectService>()
    private val dockerComposeService = mock<DockerComposeService>()
    private lateinit var syncService: SyncService

    @BeforeTest
    fun setup() {
        syncService = SyncServiceImpl(
            gitProps = gitProps,
            appProps = appProps,
            repoService = repoService,
            syncProjectService = syncProjectService,
            dockerComposeService = dockerComposeService,
        )
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(repoService, syncProjectService, dockerComposeService)
        resetCalls(repoService, syncProjectService, dockerComposeService)
        resetAnswers(repoService, syncProjectService, dockerComposeService)
    }

    @Test
    fun testIsUpdateRequiredDockerComposeRequestFailed(): Unit = runBlocking {
        // GIVEN
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        everySuspend { dockerComposeService.listProjects() } calls {
            delay(300.milliseconds)
            throw RuntimeException("any exception")
        }
        everySuspend { repoService.getInfo(any()) } returns TestCreator.repoInfo()

        // WHEN
        val actual = shouldThrow<RuntimeException> { syncService.isMainRepoStacksUpdateRequired() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.atMost(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.atMost(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
    }

    @Test
    fun testIsUpdateRequiredRepoRequestFailed(): Unit = runBlocking {
        // GIVEN
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        everySuspend { dockerComposeService.listProjects() } returns listOf()
        everySuspend { repoService.getInfo(any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { syncService.isMainRepoStacksUpdateRequired() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.atMost(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.atMost(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
    }

    @Test
    fun testIsCreateSyncPlanDockerComposeRequestFailed(): Unit = runBlocking {
        // GIVEN
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        everySuspend { dockerComposeService.listProjects() } calls {
            delay(300.milliseconds)
            throw RuntimeException("any exception")
        }
        everySuspend { repoService.getInfo(any()) } returns TestCreator.repoInfo()

        // WHEN
        val actual = shouldThrow<RuntimeException> { syncService.createSyncPlanForMainRepo() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.atMost(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.atMost(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
    }

    @Test
    fun testIsCreateSyncPlanRepoRequestFailed(): Unit = runBlocking {
        // GIVEN
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        everySuspend { dockerComposeService.listProjects() } returns listOf()
        everySuspend { repoService.getInfo(any()) } throws RuntimeException("any exception")

        // WHEN
        val actual = shouldThrow<RuntimeException> { syncService.createSyncPlanForMainRepo() }

        // THEN
        actual.message shouldBe "any exception"

        verifySuspend(mode = VerifyMode.atMost(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.atMost(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
    }

    @Test
    fun testIsUpdateRequiredTrue(): Unit = runBlocking {
        // GIVEN
        every { appProps.ignoreRepoExternalStacksSync } returns false
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        mockProjects()
        every { syncProjectService.isUpdateRequiredForProject(any(), any(), any()) } calls {
            "p1" == it.arg<ProjectInfo?>(0)?.name
        }

        // WHEN
        val actual = syncService.isMainRepoStacksUpdateRequired()

        // THEN
        actual.shouldBeTrue()

        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.exactly(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                and(matching { "cp1" == it?.name }, matching { Int.MAX_VALUE == it?.order }),
                null,
                false,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                false,
            )
        }
    }

    @Test
    fun testIsUpdateRequiredFalse(): Unit = runBlocking {
        // GIVEN
        every { appProps.ignoreRepoExternalStacksSync } returns true
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        mockProjects()
        every { syncProjectService.isUpdateRequiredForProject(any(), any(), any()) } returns false

        // WHEN
        val actual = syncService.isMainRepoStacksUpdateRequired()

        // THEN
        actual.shouldBeFalse()

        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.exactly(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                and(matching { "cp1" == it?.name }, matching { Int.MAX_VALUE == it?.order }),
                null,
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                null,
                and(matching { "rp1" == it?.name }, matching { 1 == it?.order }),
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.isUpdateRequiredForProject(
                and(matching { "p2" == it?.name }, matching { 3 == it?.order }),
                and(matching { "p2" == it?.name }, matching { 3 == it?.order }),
                true,
            )
        }
    }

    @Test
    fun testCreateSyncPlan(): Unit = runBlocking {
        // GIVEN
        every { appProps.ignoreRepoExternalStacksSync } returns true
        every { gitProps.mainRepoPath } returns "/tmp/main-repo-path"
        mockProjects()
        every { syncProjectService.addSyncPlanForProject(any(), any(), any()) } calls { args ->
            val plan = args.arg<SyncStackPlan>(0)
            args.arg<ProjectInfo?>(1)?.let { plan.ignored.add(it) }
            args.arg<ProjectInfo?>(2)?.let { plan.ignored.add(it) }
        }

        // WHEN
        val actual = syncService.createSyncPlanForMainRepo()

        // THEN
        actual.ignored shouldContainExactlyInAnyOrder listOf(
            ProjectInfo.Actual(
                name = "cp1",
                manifestPath = "/tmp/cp1/compose.yml".toPath(),
                order = Int.MAX_VALUE,
                statuses = mapOf(ContainerState.Status.RUNNING to 3),
            ),
            ProjectInfo.Actual(
                name = "p1",
                manifestPath = "/tmp/p1/compose.yml".toPath(),
                order = 2,
                statuses = mapOf(ContainerState.Status.RUNNING to 3),
            ),
            ProjectInfo.Actual(
                name = "p2",
                manifestPath = "/tmp/p2/another-compose.yml".toPath(),
                order = 3,
                statuses = mapOf(ContainerState.Status.RUNNING to 3),
            ),
            ProjectInfo.Expected(
                name = "rp1",
                manifestPath = "/tmp/rp1/compose.yml".toPath(),
                order = 1,
                stackPath = "/tmp/any".toPath(),
                envPaths = listOf(
                    "/tmp/repo.env".toPath(),
                    "/tmp/repo-secret.env".toPath(),
                    "/tmp/rp1/project.env".toPath(),
                    "/tmp/rp1/project-secret.env".toPath(),
                ),
            ),
            ProjectInfo.Expected(
                name = "p1",
                manifestPath = "/tmp/p1/compose.yml".toPath(),
                order = 2,
                stackPath = "/tmp/any".toPath(),
                envPaths = listOf(
                    "/tmp/repo.env".toPath(),
                    "/tmp/repo-secret.env".toPath(),
                    "/tmp/p1/project.env".toPath(),
                    "/tmp/p1/project-secret.env".toPath(),
                ),
            ),
            ProjectInfo.Expected(
                name = "p2",
                manifestPath = "/tmp/p2/compose.yml".toPath(),
                order = 3,
                stackPath = "/tmp/any".toPath(),
                envPaths = listOf(
                    "/tmp/repo.env".toPath(),
                    "/tmp/repo-secret.env".toPath(),
                    "/tmp/p2/project.env".toPath(),
                    "/tmp/p2/project-secret.env".toPath(),
                ),
            ),
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeService.listProjects() }
        verifySuspend(mode = VerifyMode.exactly(1)) { repoService.getInfo("/tmp/main-repo-path".toPath()) }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.addSyncPlanForProject(
                any(),
                and(matching { "cp1" == it?.name }, matching { Int.MAX_VALUE == it?.order }),
                null,
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.addSyncPlanForProject(
                any(),
                null,
                and(matching { "rp1" == it?.name }, matching { 1 == it?.order }),
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.addSyncPlanForProject(
                any(),
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                and(matching { "p1" == it?.name }, matching { 2 == it?.order }),
                true,
            )
        }
        verify(mode = VerifyMode.exactly(1)) {
            syncProjectService.addSyncPlanForProject(
                any(),
                and(matching { "p2" == it?.name }, matching { 3 == it?.order }),
                and(matching { "p2" == it?.name }, matching { 3 == it?.order }),
                true,
            )
        }
    }

    private fun mockProjects() {
        everySuspend { dockerComposeService.listProjects() } returns listOf(
            TestCreator.composeProjectInfo().copy(name = "cp1", manifestPath = "/tmp/cp1/compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p1", manifestPath = "/tmp/p1/compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p2", manifestPath = "/tmp/p2/another-compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p3", manifestPath = "/tmp/p3/another-compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p3", manifestPath = "/tmp/p3/compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p4", manifestPath = "/tmp/p4/compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p5", manifestPath = "/tmp/p5/another-compose.yml".toPath()),
            TestCreator.composeProjectInfo().copy(name = "p5", manifestPath = "/tmp/p5/compose.yml".toPath()),
        )
        everySuspend { repoService.getInfo(any()) } returns RepoInfo(
            path = "/tmp/main-repo-path".toPath(),
            secretEnvPath = "/tmp/repo-secret.env".toPath(),
            envPath = "/tmp/repo.env".toPath(),
            projects = listOf(
                TestCreator.repoProjectInfo().copy(
                    name = "rp1",
                    order = 1,
                    manifestPath = "/tmp/rp1/compose.yml".toPath(),
                    envPath = "/tmp/rp1/project.env".toPath(),
                    secretEnvPath = "/tmp/rp1/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p1",
                    order = 2,
                    manifestPath = "/tmp/p1/compose.yml".toPath(),
                    envPath = "/tmp/p1/project.env".toPath(),
                    secretEnvPath = "/tmp/p1/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p2",
                    order = 3,
                    manifestPath = "/tmp/p2/compose.yml".toPath(),
                    envPath = "/tmp/p2/project.env".toPath(),
                    secretEnvPath = "/tmp/p2/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p3",
                    order = 4,
                    manifestPath = "/tmp/p3/compose.yml".toPath(),
                    envPath = "/tmp/p3/project.env".toPath(),
                    secretEnvPath = "/tmp/p3/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p4",
                    order = 5,
                    manifestPath = "/tmp/p4/another-compose.yml".toPath(),
                    envPath = "/tmp/p4/project.env".toPath(),
                    secretEnvPath = "/tmp/p4/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p4",
                    order = 6,
                    manifestPath = "/tmp/p4/compose.yml".toPath(),
                    envPath = "/tmp/p4/project.env".toPath(),
                    secretEnvPath = "/tmp/p4/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p5",
                    order = 7,
                    manifestPath = "/tmp/p5/compose.yml".toPath(),
                    envPath = "/tmp/p5/project.env".toPath(),
                    secretEnvPath = "/tmp/p5/project-secret.env".toPath(),
                ),
                TestCreator.repoProjectInfo().copy(
                    name = "p5",
                    order = 8,
                    manifestPath = "/tmp/p5/another-compose.yml".toPath(),
                    envPath = "/tmp/p5/project.env".toPath(),
                    secretEnvPath = "/tmp/p5/project-secret.env".toPath(),
                ),
            ),
        )
    }
}
