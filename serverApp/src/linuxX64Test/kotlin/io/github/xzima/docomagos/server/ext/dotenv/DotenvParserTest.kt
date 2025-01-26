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
package io.github.xzima.docomagos.server.ext.dotenv

import TestUtils
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeClass
import kotlin.test.Test

private val logger = KotlinLogging.from(DotenvParserTest::class)

class DotenvParserTest {

    companion object {
        @BeforeClass
        fun setUp() {
            KotlinLogging.configureLogging(Level.TRACE)
        }
    }

    private val envVars = mapOf(
        "MY_TEST_EV1" to "my test ev 1",
        "MY_TEST_EV2" to "my test ev 2",
        "WITHOUT_VALUE" to "",
        "MULTI_LINE" to "hello\nworld\n# not a comment\nmulti",
        "TWO_LINE" to "hello\nworld",
        "TRAILING_COMMENT" to "value",
        "QUOTED_VALUE" to "iH4>hb_d0#_GN8d]6",
        "MY_TEST_EV4" to "my test ev 4",
        "MULTI_LINE_WITH_SHARP" to "hello\n#world",
        "QUOTED_EV1" to "jdbc:hive2://[domain]:10000/default;principal=hive/_HOST@[REALM]",
        "HOME" to "dotenv_test_home",
    )

    @Test
    fun configureWithIgnoreMalformed() {
        val dotenv = parseDotEnvFromReader(
            okioFileReader(TestUtils.testResourcesDir.resolve("dotenv/full-case")),
            throwIfMalformed = false,
        )

        for (envName in envVars.keys) {
            logger.info { "$envName: ${dotenv[envName]}" }
            envVars[envName] shouldBe dotenv[envName]
        }
    }

    @Test
    fun throwIfMalconfigured() {
        val actual = shouldThrow<DotenvException> {
            parseDotEnvFromReader(
                okioFileReader(TestUtils.testResourcesDir.resolve("dotenv/full-case")),
                throwIfMalformed = true,
            )
        }

        actual.message shouldBe "Malformed entry MY_TEST_EV3"
        actual.cause.shouldBeNull()
    }

    @Test
    fun malformedWithUncloseQuote() {
        val dotenv = parseDotEnvFromReader(
            okioFileReader(TestUtils.testResourcesDir.resolve("dotenv/unclosed-quote-case")),
            throwIfMalformed = false,
        )

        dotenv["FOO"].shouldBeNull()
        dotenv["BAZ"].shouldBeNull()
    }
}
