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

import io.github.xzima.docomagos.server.services.impl.DockerComposeClientImpl
import io.kotest.common.runBlocking
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import kotlin.test.Test

class DockerComposeClientTest {

    private val service = DockerComposeClientImpl()

    @Test
    fun versionTest(): Unit = runBlocking {
        // WHEN
        val result = service.version()

        // THEN
        result.version shouldBe "2.32.4"
    }

    @Test
    fun listProjectsTest(): Unit = runBlocking {
        // WHEN
        val result = service.listProjects()

        // THEN
        result.shouldHaveSize(1)
        result.first() should {
            it.name shouldBe "docker"
            it.status shouldStartWith "running"
            it.manifestPath.shouldNotBeEmpty()
        }
    }
}
