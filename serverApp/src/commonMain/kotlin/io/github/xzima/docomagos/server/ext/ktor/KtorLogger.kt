/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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
package io.github.xzima.docomagos.server.ext.ktor

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import io.ktor.util.logging.*

fun ktorLogger(name: String): Logger = object : Logger {
    private val logger = KotlinLogging.logger(name)

    override val level: LogLevel = when (KotlinLoggingConfiguration.logLevel) {
        Level.TRACE -> LogLevel.TRACE
        Level.DEBUG -> LogLevel.DEBUG
        Level.INFO -> LogLevel.INFO
        Level.WARN -> LogLevel.WARN
        Level.ERROR, Level.OFF -> LogLevel.ERROR
    }

    override fun error(message: String) = logger.error { message }

    override fun error(message: String, cause: Throwable) = logger.error(cause) { message }

    override fun warn(message: String) = logger.warn { message }

    override fun warn(message: String, cause: Throwable) = logger.warn(cause) { message }

    override fun info(message: String) = logger.info { message }

    override fun info(message: String, cause: Throwable) = logger.info(cause) { message }

    override fun debug(message: String) = logger.debug { message }

    override fun debug(message: String, cause: Throwable) = logger.debug(cause) { message }

    override fun trace(message: String) = logger.trace { message }

    override fun trace(message: String, cause: Throwable) = logger.trace(cause) { message }
}
