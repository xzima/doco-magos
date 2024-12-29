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

import io.github.oshai.kotlinlogging.Level
import org.koin.core.logger.Level as KoinLevel

fun Level.toKoinLevel() = when (this) {
    Level.TRACE, Level.DEBUG -> KoinLevel.DEBUG
    Level.INFO -> KoinLevel.INFO
    Level.WARN -> KoinLevel.WARNING
    Level.ERROR -> KoinLevel.ERROR
    Level.OFF -> KoinLevel.NONE
}

fun String.toLevel(): Level {
    val level = Level.entries.firstOrNull { it.toString().equals(toString(), ignoreCase = true) }
    if (null == level) {
        throw IllegalStateException("$this is not valid logging level")
    }
    return level
}
