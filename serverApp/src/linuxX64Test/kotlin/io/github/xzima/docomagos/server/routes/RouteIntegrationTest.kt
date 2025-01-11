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
package io.github.xzima.docomagos.server.routes

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.api.ListProjectsResp
import io.github.xzima.docomagos.api.Req
import io.github.xzima.docomagos.client.DockerComposeApiServiceImpl
import io.github.xzima.docomagos.client.createRsocketClient
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.App
import io.github.xzima.docomagos.server.handlers.ReqHandler
import io.github.xzima.docomagos.server.props.KtorProps
import io.github.xzima.docomagos.server.props.RsocketProps
import io.github.xzima.docomagos.server.services.JobService
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.rsocket.kotlin.RSocket
import kotlinx.coroutines.*
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.from(RouteIntegrationTest::class)

class RouteIntegrationTest {
    private companion object {
        const val TEST_PORT = 14444
        const val TEST_HOST = "localhost:$TEST_PORT"
    }

    private val rsocketProps = object : RsocketProps {
        override val maxFragmentSize: Int = 1024
    }
    private val ktorProps = object : KtorProps {
        override val port: Int = TEST_PORT
        override val reuseAddress: Boolean = true
        override val gracePeriodMillis: Long = 0
        override val graceTimeoutMillis: Long = 0
    }
    private val jobService = object : JobService {
        override fun createJob(scope: CoroutineScope): Job = scope.launch {
            logger.debug { "createJob: start" }
        }
    }
    private val listProjectsHandler = object : ReqHandler<Req.ListProjects, ListProjectsResp> {
        override suspend fun handle(request: Req.ListProjects): ListProjectsResp = ListProjectsResp(
            projects = listOf(
                ListProjectsResp.ProjectItem(
                    name = "test-project",
                    status = "running(404)",
                ),
            ),
        )
    }

    private lateinit var server: ApplicationEngine
    private lateinit var httpClient: HttpClient
    private lateinit var rSocketClient: RSocket

    @BeforeTest
    fun beforeTest(): Unit = runBlocking {
        KotlinLogging.configureLogging(Level.TRACE)
        server = App.createServer(
            rsocketProps = rsocketProps,
            jobService = jobService,
            routeInjectors = listOf(
                RsocketRouteInjector(
                    listProjectsHandler = listProjectsHandler,
                ),
            ),
            ktorProps = ktorProps,
        ).start(wait = false)

        httpClient = HttpClient(CIO) {
            defaultRequest {
                url("http://$TEST_HOST")
            }
        }
        rSocketClient = createRsocketClient(CIO, TEST_HOST, LogLevel.ALL)
    }

    @AfterTest
    fun afterTest(): Unit = runBlocking {
        server.stop()
        stopKoin()
        delay(1.seconds)
    }

    @Test
    fun testHttpHealth(): Unit = runBlocking {
        // WHEN
        val response = httpClient.get("/health")

        // THEN
        response shouldHaveStatus HttpStatusCode.NoContent
        response.bodyAsText() shouldBe ""
    }

    @Test
    fun testListProjects(): Unit = runBlocking {
        val service = DockerComposeApiServiceImpl(rSocketClient)

        // WHEN
        val response = service.listProjects()

        // THEN
        response.projects shouldHaveSize 1
        response.projects.first() should {
            it.name shouldBe "test-project"
            it.status shouldBe "running(404)"
        }
    }
}
