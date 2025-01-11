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
package io.github.xzima.docomagos.docker

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.docker.apis.ContainerApi
import io.github.xzima.docomagos.docker.apis.SystemApi
import io.github.xzima.docomagos.docker.ktor.engine.socket.SocketCIO
import io.github.xzima.docomagos.logging.HttpClientLogger
import io.github.xzima.docomagos.logging.configureLogging
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import kotlin.test.Test

class DockerClientTest {
    private val client = HttpClient(SocketCIO) {
        KotlinLogging.configureLogging(Level.DEBUG)
        engine {
            unixSocketPath = "/var/run/docker.sock"
        }
        install(Logging.Companion) {
            level = LogLevel.ALL
            logger = HttpClientLogger()
        }
        install(ContentNegotiation.Plugin) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    private val containerApi = ContainerApi("http://1.47", client)
    private val systemApi = SystemApi("http://1.47", client)

    @Test
    fun testContainersList(): Unit = runBlocking {
        // WHEN
        val resp = containerApi.containerList()
        // THEN
        resp.status shouldBe HttpStatusCode.Companion.OK
        resp.headers should {
            it[HttpHeaders.ContentType].shouldNotBeNull() shouldContain ContentType.Application.Json.toString()
            it[HttpHeaders.Server].shouldNotBeNull() shouldContain "Docker/27.3.1 (linux)"
            it[HttpHeaders.Date].shouldNotBeNull()
        }
        val list = resp.body()
        println(list)
        list shouldHaveSize 6
    }

    @Test
    fun testInfo(): Unit = runBlocking {
        // WHEN
        val resp = systemApi.systemInfo()
        // THEN
        resp.status shouldBe HttpStatusCode.Companion.OK
        resp.headers should {
            it[HttpHeaders.ContentType].shouldNotBeNull() shouldContain ContentType.Application.Json.toString()
            it[HttpHeaders.Server].shouldNotBeNull() shouldContain "Docker/27.3.1 (linux)"
            it[HttpHeaders.Date].shouldNotBeNull()
        }
        val info = resp.body()
        println(info)
        info.containers shouldBe 7
        info.containersRunning shouldBe 6
        info.containersPaused shouldBe 0
        info.containersStopped shouldBe 1
    }

    @Test
    fun testStats(): Unit = runBlocking {
        // WHEN
        val resp = containerApi.containerStats(id = "ed5ab81c6ce2", stream = false)
        // THEN
        resp.status shouldBe HttpStatusCode.Companion.OK
        println(resp.body())
    }
}
