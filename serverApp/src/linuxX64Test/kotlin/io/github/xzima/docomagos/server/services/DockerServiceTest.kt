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
import dev.mokkery.answering.sequentially
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
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
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.SyncJobProps
import io.github.xzima.docomagos.server.services.impl.DockerServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlin.test.AfterTest
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Test

class DockerServiceTest {

    private val appProps = object : AppProps {
        override val hostname: String = "server-hostname"
        override val staticUiPath: String = "any"
        override val jobPeriodMs: Int = 0
    }
    private val syncJobProps = object : SyncJobProps {
        override val containerName: String = "job-container-name"
        override val containerCmd: String = "sync"
    }
    private val dockerClient: DockerClient = mock()
    private lateinit var dockerService: DockerService

    @BeforeClass
    fun setUp() {
        KotlinLogging.configureLogging(Level.DEBUG)
    }

    @BeforeTest
    fun setup() {
        dockerService = DockerServiceImpl(
            appProps = appProps,
            syncJobProps = syncJobProps,
            dockerClient = dockerClient,
        )
    }

    @AfterTest
    fun tearDown() {
        verifyNoMoreCalls(dockerClient)
        resetCalls(dockerClient)
        resetAnswers(dockerClient)
    }

    @Test
    fun testSearchHostnameThrow(): Unit = runBlocking {
        // GIVEN
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
    }

    @Test
    fun testHostnameNotFound(): Unit = runBlocking {
        // GIVEN
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns null

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
    }

    @Test
    fun testJobContainerCreationThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
    }

    @Test
    fun testSearchJobContainerThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        everySuspend {
            dockerClient.containerInfoOrNull(syncJobProps.containerName)
        } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
    }

    @Test
    fun testJobContainerNotFound(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns null

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
    }

    @Test
    fun testJobContainerExistWithDifferentImageName(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        val jobContainer = TestCreator.dockerContainerInfo().copy(imageName = "anotherImage")
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
    }

    @Test
    fun testJobContainerExistWithRunningStatus(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
            status = ContainerState.Status.RUNNING,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
    }

    @Test
    fun testJobContainerDeleteThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
    }

    @Test
    fun testJobContainerDeleteNotFound(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns null
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns false

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
    }

    @Test
    fun testJobContainerRecreateThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } sequentially {
            returns(null)
            throws(RuntimeException("any exception"))
        }
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns true

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(2)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
    }

    @Test
    fun testJobContainerRecreateStillNull(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } sequentially {
            returns(null)
            returns(null)
        }
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns true

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(2)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
    }

    @Test
    fun testJobContainerRecreateAndStartThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } sequentially {
            returns(null)
            returns("newJobContainerId")
        }
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns true
        everySuspend { dockerClient.startContainer("newJobContainerId") } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(2)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }

    @Test
    fun testJobContainerRecreateAndStartNotFound(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } sequentially {
            returns(null)
            returns("newJobContainerId")
        }
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns true
        everySuspend { dockerClient.startContainer("newJobContainerId") } returns false

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(2)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }

    @Test
    fun testJobContainerRecreateAndStartSuccess(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } sequentially {
            returns(null)
            returns("newJobContainerId")
        }
        val jobContainer = TestCreator.dockerContainerInfo().copy(
            id = "jobContainerId",
            imageName = "targetImage",
            status = ContainerState.Status.DEAD,
        )
        everySuspend { dockerClient.containerInfoOrNull(syncJobProps.containerName) } returns jobContainer
        everySuspend { dockerClient.deleteContainer("jobContainerId") } returns true
        everySuspend { dockerClient.startContainer("newJobContainerId") } returns true

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(2)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(syncJobProps.containerName) }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.deleteContainer("jobContainerId") }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }

    @Test
    fun testJobContainerCreatedAndStartThrow(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns "newJobContainerId"
        everySuspend { dockerClient.startContainer("newJobContainerId") } throws RuntimeException("any exception")

        // WHEN
        val exception = shouldThrow<RuntimeException> { dockerService.tryStartSyncJobService() }

        // THEN
        exception.message shouldBe "any exception"
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }

    @Test
    fun testJobContainerCreatedAndStartNotFound(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns "newJobContainerId"
        everySuspend { dockerClient.startContainer("newJobContainerId") } returns false

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }

    @Test
    fun testJobContainerCreatedAndStartSuccess(): Unit = runBlocking {
        // GIVEN
        val currentContainer = TestCreator.dockerContainerInfo().copy(
            imageName = "targetImage",
        )
        everySuspend { dockerClient.containerInfoOrNull(appProps.hostname) } returns currentContainer
        everySuspend {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        } returns "newJobContainerId"
        everySuspend { dockerClient.startContainer("newJobContainerId") } returns true

        // WHEN
        dockerService.tryStartSyncJobService()

        // THEN
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.containerInfoOrNull(appProps.hostname) }
        verifySuspend(mode = VerifyMode.exactly(1)) {
            dockerClient.copyContainer(
                name = syncJobProps.containerName,
                cmd = syncJobProps.containerCmd,
                source = currentContainer,
            )
        }
        verifySuspend(mode = VerifyMode.exactly(1)) { dockerClient.startContainer("newJobContainerId") }
    }
}
