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
package io.github.xzima.docomagos.server

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.posix.*
import kotlin.concurrent.AtomicReference

private val logger = KotlinLogging.from(::initGracefulShutdown)
private val holder = AtomicReference(Job())

@OptIn(ExperimentalForeignApi::class)
fun initGracefulShutdown(): CompletableJob {
    val handler = staticCFunction<Int, Unit> {
        logger.info { "start holder complete" }
        holder.value.complete()
        logger.info { "end holder complete" }
    }
    signal(SIGINT, handler)
    signal(SIGTERM, handler)
    return holder.value
}
