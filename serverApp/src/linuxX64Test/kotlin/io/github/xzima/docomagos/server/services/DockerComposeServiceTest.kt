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
import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.impl.DockerComposeServiceImpl
import io.github.xzima.docomagos.server.services.models.ComposeProjectInfo
import io.github.xzima.docomagos.server.services.models.DCProjectInfo
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class DockerComposeServiceTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private val dockerComposeClient = mock<DockerComposeClient>()
    private val fileReadService = mock<FileReadService>()
    private lateinit var dockerComposeService: DockerComposeService

    @BeforeTest
    fun setup() {
        dockerComposeService = DockerComposeServiceImpl(
            dockerComposeClient = dockerComposeClient,
            fileReadService = fileReadService,
        )
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(dockerComposeClient, fileReadService)
        resetCalls(dockerComposeClient, fileReadService)
        resetAnswers(dockerComposeClient, fileReadService)
    }

    @Test
    fun testExecuteSyncPlan(): Unit = runBlocking {
        // GIVEN
        everySuspend { dockerComposeClient.down(any()) } calls {
            val manifestPath = it.arg<Path>(0)
            if ("err" in manifestPath.toString()) {
                throw RuntimeException("exception with $manifestPath")
            }
        }
        everySuspend { dockerComposeClient.up(any(), any(), any(), any()) } calls {
            val manifestPath = it.arg<Path>(0)
            if ("err" in manifestPath.toString()) {
                throw RuntimeException("exception with $manifestPath")
            }
        }
        everySuspend { fileReadService.readAndMergeEnvs(any<ProjectInfo.Expected>(), false) } calls {
            val projectInfo = it.arg<ProjectInfo.Expected>(0)
            val envsPath = listOfNotNull(projectInfo.projectEnvPath, projectInfo.projectSecretEnvPath)
            if (envsPath.any { "err" in it.toString() }) {
                throw RuntimeException("exception with envs")
            }
            envsPath.associate { it.name to it.segments.size.toString() }
        }

        val da1 = actualProjectInfo(name = "da1")
        val de1 = expectedProjectInfo(name = "de1", isEnvErr = true)
        val da2 = actualProjectInfo(name = "da2", order = 1)
        val de2 = expectedProjectInfo(name = "de2", isManifestErr = true, order = 1)
        val da3 = actualProjectInfo(name = "da3", order = 10)
        val de3 = expectedProjectInfo(name = "de3", order = 10)
        val da4 = actualProjectInfo(name = "da4", isErr = true, order = 3)
        val de4 = expectedProjectInfo(name = "de4", order = 3)
        val ue1 = expectedProjectInfo(name = "ue1")
        val ue2 = expectedProjectInfo(name = "ue2", order = 1)
        val ue3 = expectedProjectInfo(name = "ue3", isManifestErr = true, order = 10)
        val ue4 = expectedProjectInfo(name = "ue4", isManifestErr = true, isEnvErr = true, order = 300)
        val ue5 = expectedProjectInfo(name = "ue5", order = 3)
        val ue6 = expectedProjectInfo(name = "ue6", isEnvErr = true, order = 110)

        @Suppress("ktlint:standard:argument-list-wrapping")
        val syncPlan = SyncStackPlan(
            toDown = mutableListOf(
                da1, de1, da2, de2, da3, de3, da4, de4,
            ),
            toUp = mutableListOf(
                ue1, ue2, ue3, ue4, ue5, ue6,
            ),
            ignored = mutableListOf(
                actualProjectInfo(name = "ia1", isErr = true, order = 404),
                expectedProjectInfo(name = "ie2"),
            ),
        )

        // WHEN
        dockerComposeService.executeSyncPlan(syncPlan)

        // THEN
        verifySuspend(mode = VerifyMode.exhaustiveOrder) {
            dockerComposeClient.down(da1.name)
            dockerComposeClient.down(de1.name)
            dockerComposeClient.down(da3.name)
            dockerComposeClient.down(de3.name)
            dockerComposeClient.down(da4.name)
            dockerComposeClient.down(de4.name)
            dockerComposeClient.down(da2.name)
            dockerComposeClient.down(de2.name)
            fileReadService.readAndMergeEnvs(ue2, false)
            dockerComposeClient.up(ue2.manifestPath, ue2.name, any(), any())
            fileReadService.readAndMergeEnvs(ue5, false)
            dockerComposeClient.up(ue5.manifestPath, ue5.name, any(), any())
            fileReadService.readAndMergeEnvs(ue3, false)
            dockerComposeClient.up(ue3.manifestPath, ue3.name, any(), any())
            fileReadService.readAndMergeEnvs(ue6, false)
            fileReadService.readAndMergeEnvs(ue4, false)
            fileReadService.readAndMergeEnvs(ue1, false)
            dockerComposeClient.up(ue1.manifestPath, ue1.name, any(), any())
        }
    }

    @Test
    fun testListProjects(): Unit = runBlocking {
        // GIVEN
        everySuspend { dockerComposeClient.listProjects() } returns listOf(
            dcProjectInfo("p1", ""),
            dcProjectInfo("p2", "exited(1)"),
            dcProjectInfo("p3", "exited(1), running(5)"),
            dcProjectInfo("p4", "created(1), running(2), paused(3), restarting(4), removing(5), exited(6), dead(7)"),
            dcProjectInfo("p5", "exited(1), invalid(5)"),
            dcProjectInfo("p6", "exited(1), paused"),
        )
        // WHEN
        val actual = dockerComposeService.listProjects()
        // THEN
        actual shouldContainExactlyInAnyOrder listOf(
            ComposeProjectInfo(name = "p1", statuses = emptyMap(), manifestPath = "/tmp/p1/compose.yml".toPath()),
            ComposeProjectInfo(
                name = "p2",
                statuses = mapOf(ContainerState.Status.EXITED to 1),
                manifestPath = "/tmp/p2/compose.yml".toPath(),
            ),
            ComposeProjectInfo(
                name = "p3",
                statuses = mapOf(ContainerState.Status.EXITED to 1, ContainerState.Status.RUNNING to 5),
                manifestPath = "/tmp/p3/compose.yml".toPath(),
            ),
            ComposeProjectInfo(
                name = "p4",
                statuses = mapOf(
                    ContainerState.Status.CREATED to 1,
                    ContainerState.Status.RUNNING to 2,
                    ContainerState.Status.PAUSED to 3,
                    ContainerState.Status.RESTARTING to 4,
                    ContainerState.Status.REMOVING to 5,
                    ContainerState.Status.EXITED to 6,
                    ContainerState.Status.DEAD to 7,
                ),
                manifestPath = "/tmp/p4/compose.yml".toPath(),
            ),
            ComposeProjectInfo(
                name = "p5",
                statuses = mapOf(ContainerState.Status.EXITED to 1),
                manifestPath = "/tmp/p5/compose.yml".toPath(),
            ),
            ComposeProjectInfo(
                name = "p6",
                statuses = mapOf(ContainerState.Status.EXITED to 1),
                manifestPath = "/tmp/p6/compose.yml".toPath(),
            ),
        )

        verifySuspend(mode = VerifyMode.exactly(1)) { dockerComposeClient.listProjects() }
    }

    fun actualProjectInfo(name: String, isErr: Boolean = false, order: Int = Int.MAX_VALUE): ProjectInfo.Actual {
        val projectName = if (isErr) {
            "$name-err"
        } else {
            name
        }
        return TestCreator.actualProjectInfo().copy(
            name = projectName,
            order = order,
        )
    }

    fun expectedProjectInfo(
        name: String,
        isManifestErr: Boolean = false,
        isEnvErr: Boolean = false,
        order: Int = Int.MAX_VALUE,
    ): ProjectInfo.Expected {
        val manifestDir = if (isManifestErr) {
            "$name-err"
        } else {
            name
        }
        val envDir = if (isEnvErr) {
            "$name-err"
        } else {
            name
        }
        return TestCreator.expectedProjectInfo().copy(
            name = name,
            order = order,
            manifestPath = "/tmp/$manifestDir/compose.yml".toPath(),
            projectEnvPath = "/tmp/$envDir/.env".toPath(),
            projectSecretEnvPath = "/tmp/$envDir/secret.env".toPath(),
        )
    }

    private fun dcProjectInfo(name: String, status: String): DCProjectInfo = DCProjectInfo(
        name = name,
        status = status,
        manifestPath = "/tmp/$name/compose.yml",
    )
}
