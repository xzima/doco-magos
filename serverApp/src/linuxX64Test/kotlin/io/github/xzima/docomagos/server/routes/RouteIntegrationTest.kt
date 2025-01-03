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

import initTestKoinModule
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.client.DockerComposeApiServiceImpl
import io.github.xzima.docomagos.client.createRsocketClient
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.server.App
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.rsocket.kotlin.RSocket
import kotlinx.coroutines.delay
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class RouteIntegrationTest {
    private companion object {
        const val TEST_PORT = 14444
        const val TEST_HOST = "localhost:$TEST_PORT"
    }

    private lateinit var server: ApplicationEngine
    private lateinit var httpClient: HttpClient
    private lateinit var rSocketClient: RSocket

    @BeforeTest
    fun beforeTest(): Unit = runBlocking {
        configureLogging(Level.TRACE)
        initTestKoinModule(port = TEST_PORT)
        server = App.createServer().start(wait = false)

        httpClient = HttpClient {
            defaultRequest {
                url("http://$TEST_HOST")
            }
        }
        rSocketClient = createRsocketClient(TEST_HOST, LogLevel.ALL)
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
    }
}
