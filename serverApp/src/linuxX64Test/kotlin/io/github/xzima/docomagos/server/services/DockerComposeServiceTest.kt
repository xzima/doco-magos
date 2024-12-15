package io.github.xzima.docomagos.server.services

import io.github.xzima.docomagos.server.services.models.DCListProjects
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.*
import kotlin.test.Test

class DockerComposeServiceTest {

    private val service = DockerComposeService()

    @Test
    fun versionTest(): Unit = runBlocking {
        // WHEN
        val result = service.version()

        // THEN
        result.version shouldBe "2.31.0"
    }

    @Test
    fun listProjectsTest(): Unit = runBlocking {
        // WHEN
        val result = service.listProjects()

        // THEN
        result.shouldHaveSize(1)
        result shouldContain DCListProjects(
            name = "docker",
            status = "running(6)",
            configFiles = "/home/alex/Docker/docker-compose.yml",
        )
    }
}
