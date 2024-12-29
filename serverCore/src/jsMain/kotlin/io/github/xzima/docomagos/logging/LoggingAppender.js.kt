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
import kotlinx.datetime.*

actual fun loggingAppender(): Appender = object : FormattingAppender() {
    override fun logFormattedMessage(loggingEvent: KLoggingEvent, ignore: Any?) {
        when (loggingEvent.level) {
            Level.OFF -> Unit
            Level.INFO -> console.info(*formatMessage(loggingEvent))
            Level.WARN -> console.warn(*formatMessage(loggingEvent))
            Level.ERROR -> console.error(*formatMessage(loggingEvent))
            else -> console.log(*formatMessage(loggingEvent))
        }
    }

    fun formatMessage(loggingEvent: KLoggingEvent): Array<String> {
        val levelColor = when (loggingEvent.level) {
            Level.ERROR -> "red"
            Level.WARN -> "yellow"
            else -> "green"
        }
        val time = Clock.System.now()
        val level = loggingEvent.level.name.padStart(5)
        val logger = loggingEvent.loggerName.padEnd(40).takeLast(40)
        return arrayOf(
            "%c$time %c$level %c--- %c$logger %c: ",
            "color: gray",
            "color: $levelColor",
            "color: gray",
            "color: cyan",
            "color: gray",
            "${loggingEvent.message} ${createThrowableMsg(loggingEvent.cause)}",
        )
    }
}
