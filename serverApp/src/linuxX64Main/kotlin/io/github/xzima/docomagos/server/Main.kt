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
package io.github.xzima.docomagos.server

import com.github.ajalt.clikt.core.CliktError
import com.github.ajalt.clikt.core.parse
import com.github.ajalt.clikt.core.subcommands
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.cli.DocoMagosCommand
import io.github.xzima.docomagos.server.cli.SyncCommand
import kotlin.system.exitProcess

private val logger = KotlinLogging.from(::main)

fun main(args: Array<String>) {
    val cli = DocoMagosCommand().subcommands(ServeCommand(), SyncCommand())
    try {
        cli.parse(args)
    } catch (e: CliktError) {
        cli.echoFormattedHelp(e)
        exitProcess(e.statusCode)
    } catch (e: Exception) {
        logger.error(e) { e.message }
        exitProcess(1)
    }
}
