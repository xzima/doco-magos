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

import io.github.oshai.kotlinlogging.Formatter
import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KLoggingEvent
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.KotlinLoggingConfiguration
import io.github.oshai.kotlinlogging.Level
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

expect fun KotlinLogging.from(kClass: KClass<*>): KLogger

fun KotlinLogging.from(function: KCallable<*>): KLogger = logger(function.name)

fun configureLogging(logLevel: Level) {
    KotlinLoggingConfiguration.logLevel = logLevel
    KotlinLoggingConfiguration.appender = loggingAppender()
    KotlinLoggingConfiguration.formatter = object : Formatter {
        override fun formatMessage(loggingEvent: KLoggingEvent): String = ""
    }
}

internal tailrec fun createThrowableMsg(throwable: Throwable?, msg: String = ""): String =
    if (throwable == null || throwable.cause == throwable) {
        msg
    } else {
        createThrowableMsg(throwable.cause, "$msg, Caused by: '${throwable.message}'")
    }
