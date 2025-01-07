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
package io.github.xzima.docomagos.server.cli

import com.github.ajalt.clikt.testing.test
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class SyncCommandTest {

    private lateinit var command: SyncCommand

    @BeforeTest
    fun before() {
        configureLogging(Level.DEBUG)
        command = SyncCommand()
    }

    @Test
    fun test(): Unit = runBlocking {
        // WHEN
        val actual = shouldThrow<RuntimeException> { command.test() }

        // THEN
        actual.message shouldBe "Error from ServeCommand"
    }
}
