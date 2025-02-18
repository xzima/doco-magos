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

import TestUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.services.impl.DockerComposeClientImpl
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlin.test.BeforeClass
import kotlin.test.Test

class DockerComposeClientTest {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private lateinit var dockerComposeClient: DockerComposeClient

    @Test
    fun testVersion() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(false)

        // WHEN
        val result = dockerComposeClient.version()

        // THEN
        result.version shouldBe "2.32.4"
    }

    @Test
    fun testVersionDryRun() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(true)

        // WHEN
        val result = dockerComposeClient.version()

        // THEN
        result.version shouldBe "2.32.4"
    }

    @Test
    fun testListProjects() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(false)

        // WHEN
        val result = dockerComposeClient.listProjects()

        // THEN
        val projects = result.associateBy { it.name }
        projects["docker"].shouldNotBeNull() should {
            it.status shouldStartWith "running"
            it.manifestPath.shouldNotBeEmpty()
        }
    }

    @Test
    fun testListProjectsDryRun() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(true)

        // WHEN
        val result = dockerComposeClient.listProjects()

        // THEN
        val projects = result.associateBy { it.name }
        projects["docker"].shouldNotBeNull() should {
            it.status shouldStartWith "running"
            it.manifestPath.shouldNotBeEmpty()
        }
    }

    @Test
    fun testUpDown() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(false)
        val stackName = "test_up_down"

        // THEN BEFORE
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
        }

        // WHEN UP
        val stackPath = TestUtils.testResourcesDir.resolve("compose-project")
        val manifestPath = stackPath.resolve("docker-compose.yml")
        dockerComposeClient.up(
            manifestPath = manifestPath,
            stackName = stackName,
            stackPath = stackPath,
            envs = mapOf(
                "SUPER_ENV" to "Hello from testUpDown",
                "VERSION" to "SNAPSHOT",
            ),
        )

        // THEN UP
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
            projects[stackName].shouldNotBeNull() should {
                it.status shouldBe "running(2)"
                it.manifestPath shouldBe manifestPath.toString()
            }
        }

        // WHEN DOWN
        dockerComposeClient.down(stackName)

        // THEN DOWN
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
        }
    }

    @Test
    fun testUpDownDryRun() {
        // GIVEN
        dockerComposeClient = DockerComposeClientImpl(true)
        val stackName = "test_up_down_dry_run"

        // THEN BEFORE
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
        }

        // WHEN UP
        val stackPath = TestUtils.testResourcesDir.resolve("compose-project")
        val manifestPath = stackPath.resolve("docker-compose.yml")
        dockerComposeClient.up(
            manifestPath = manifestPath,
            stackName = stackName,
            stackPath = stackPath,
            envs = mapOf(
                "SUPER_ENV" to "Hello from testUpDown",
                "VERSION" to "SNAPSHOT",
            ),
        )

        // THEN UP
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
        }

        // WHEN DOWN
        dockerComposeClient.down("AAA")

        // THEN DOWN
        dockerComposeClient.listProjects() should { result ->
            val projects = result.associateBy { it.name }
            projects["docker"].shouldNotBeNull() should {
                it.status shouldStartWith "running"
                it.manifestPath.shouldNotBeEmpty()
            }
        }
    }
}
