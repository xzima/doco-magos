package io.github.xzima.docomagos.server.routes

import initTestKoinModule
import io.github.xzima.docomagos.client.DockerComposeApiServiceImpl
import io.github.xzima.docomagos.client.createRsocketClient
import io.github.xzima.docomagos.server.createServer
import io.kotest.assertions.ktor.client.shouldHaveStatus
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.engine.*
import io.rsocket.kotlin.RSocket
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

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
        initTestKoinModule(port = TEST_PORT)
        server = createServer().start(wait = false)

        httpClient = HttpClient {
            defaultRequest {
                url("http://$TEST_HOST")
            }
        }
        rSocketClient = createRsocketClient(TEST_HOST)
    }

    @AfterTest
    fun afterTest(): Unit = runBlocking {
            server.stop()
            stopKoin()
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
