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
package io.github.xzima.docomagos.logging

import io.github.oshai.kotlinlogging.Appender
import io.github.oshai.kotlinlogging.FormattingAppender
import io.github.oshai.kotlinlogging.KLoggingEvent
import io.github.oshai.kotlinlogging.Level
import kotlinx.cinterop.*
import kotlinx.datetime.*
import platform.posix.*

@OptIn(ExperimentalForeignApi::class)
actual fun loggingAppender(): Appender = object : FormattingAppender() {
    override fun logFormattedMessage(loggingEvent: KLoggingEvent, formattedMessage: Any?) {
        when (loggingEvent.level) {
            Level.OFF -> Unit
            Level.ERROR -> fprintf(stderr, "%s\n", formatMessage(loggingEvent))
            else -> println(formatMessage(loggingEvent))
        }
    }

    fun formatMessage(loggingEvent: KLoggingEvent): String {
        val resetColor = "\u001b[0m"
        val cyanColor = "\u001b[36m"
        val grayColor = "\u001b[90m"
        val levelColor = when (loggingEvent.level) {
            Level.ERROR -> "\u001b[31m" // ansi red
            Level.WARN -> "\u001b[33m" // ansi yellow
            else -> "\u001b[32m" // ansi green
        }
        with(loggingEvent) {
            return buildString {
                append("$grayColor${Clock.System.now()}$resetColor ")
                append("$levelColor${level.name.padStart(5)}$resetColor $grayColor---$resetColor ")
                append("$cyanColor${loggerName.padEnd(40).takeLast(40)}$resetColor $grayColor:$resetColor ")
                marker?.getName()?.let {
                    append("$it ")
                }
                append(message)
                append(createThrowableMsg(cause))
            }
        }
    }
}
