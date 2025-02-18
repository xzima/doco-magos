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
package io.github.xzima.docomagos.server.cli.commands

import TestUtils
import com.github.ajalt.clikt.testing.test
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.DockerComposeProps
import io.github.xzima.docomagos.server.props.DockerProps
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RepoStructureProps
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.props.SyncJobProps
import io.github.xzima.docomagos.server.routes.RouteInjector
import io.github.xzima.docomagos.server.services.DockerClient
import io.github.xzima.docomagos.server.services.DockerComposeClient
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.DockerService
import io.github.xzima.docomagos.server.services.FileReadService
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.GitCryptClient
import io.github.xzima.docomagos.server.services.GitService
import io.github.xzima.docomagos.server.services.JobService
import io.github.xzima.docomagos.server.services.PingService
import io.github.xzima.docomagos.server.services.RepoStructureService
import io.github.xzima.docomagos.server.services.SyncProjectService
import io.github.xzima.docomagos.server.services.SyncService
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.context.stopKoin
import org.koin.mp.KoinPlatform
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class AppCommandTest {

    private lateinit var command: AppCommand

    @BeforeTest
    fun before() {
        command = AppCommand(TestUtils.testResourcesDir.resolve("test-config.yaml").toString())
    }

    @AfterTest
    fun after() {
        stopKoin()
    }

    private val instanceGroupTypes = listOf(
        RouteInjector::class to 2,
    )
    private val instanceTypes = listOf(
        RsocketProps::class,
        KtorProps::class,
        AppProps::class,
        GitProps::class,
        DockerProps::class,
        SyncJobProps::class,
        PingService::class,
        RepoStructureProps::class,
        DockerComposeProps::class,
        DockerComposeClient::class,
        DockerClient::class,
        DockerService::class,
        GitClient::class,
        GitService::class,
        RepoStructureService::class,
        SyncProjectService::class,
        GitCryptClient::class,
        FileReadService::class,
        DockerComposeService::class,
        SyncService::class,
        JobService::class,
        ListProjectsHandler::class,
    )

    @Test
    @OptIn(KoinInternalApi::class)
    fun testPositive() {
        // GIVEN
        val envs = mapOf(
            "HOSTNAME" to "localhost",
            "LOGGING_LEVEL" to "WARN",
            "KTOR_PORT" to "14444",
            "KTOR_REUSE_ADDRESS" to "true",
            "GIT_GIT_ASK_PASS" to "/tmp/GIT_ASKPASS",
            "GIT_MAIN_REPO_URL" to "https://github.com/xzima/home-composes.git",
        )

        // WHEN
        val actual = command.test(envvars = envs)
        val koin = KoinPlatform.getKoin()
        val instances = koin.instanceRegistry.instances
        val rootScope = koin.scopeRegistry.rootScope

        // THEN
        actual.statusCode shouldBe 0
        instanceTypes.forAll {
            koin.get<Any?>(it).shouldNotBeNull()
        }
        instanceGroupTypes.forAll { (type, count) ->
            rootScope.getAll<Any?>(type) shouldHaveSize count
        }
        instances.size shouldBe instanceTypes.size + instanceGroupTypes.size + instanceGroupTypes.sumOf { it.second }
    }

    @Test
    fun testFailedWithoutEnvs() {
        // WHEN
        val actual = command.test()

        // THEN
        actual.statusCode shouldBe 1
        actual.output shouldContain "missing option --git-main-repo-url"
    }
}
