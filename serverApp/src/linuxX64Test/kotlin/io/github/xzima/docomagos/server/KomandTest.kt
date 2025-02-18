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
@file:OptIn(FlowPreview::class)

package io.github.xzima.docomagos.server

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

private val logger = KotlinLogging.from(KomandTest::class)

@Ignore
class KomandTest {

    @Test
    fun testKomand() = runBlocking {
        getForever(getStats())
            .onCompletion { logger.info { "Completion: $it" } }
            .timeout(10.seconds)
            .collect { logger.info { it } }
    }

    private fun getContainers() {
        val c = Command("docker-compose").args("-p", "docker", "ps", "--format=json")
            .stdout(Stdio.Pipe).spawn()
        c.waitWithOutput()
    }

    private fun <T> getForever(source: Flow<T>) = flow {
        var i = 0
        do {
            emitAll(source)
            logger.info { "retry: $i" }
            delay(1000)
            i++
        } while (true)
    }

    private fun getStats() = flow {
        val c = Command("docker-compose").args("-p", "docker", "stats", "--format=json")
            .stdout(Stdio.Pipe).spawn()
        c.bufferedStdout()?.lines()?.forEach {
            emit(it)
        }
    }

    private fun getLogs() = flow {
        val c = Command("docker-compose").args("-p", "docker", "logs", "--follow")
            .stdout(Stdio.Pipe).spawn()
        c.bufferedStdout()?.lines()?.forEach {
            emit(it)
        }
    }
}
