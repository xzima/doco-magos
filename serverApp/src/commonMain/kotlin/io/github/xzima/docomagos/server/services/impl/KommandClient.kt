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
package io.github.xzima.docomagos.server.services.impl

import com.kgit2.kommand.io.Output
import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import kotlinx.serialization.json.*

private val logger = KotlinLogging.from(KommandClient::class)

open class KommandClient(
    private val command: String,
) {
    protected open fun cmd(builder: Command.() -> Unit): Output {
        val command = Command(command).stdout(Stdio.Pipe).stderr(Stdio.Pipe)
        command.builder()
        logger.trace { "exec cmd: $command" }
        val child = command.spawn()
        val output = child.waitWithOutput()
        return output
    }

    protected fun Output.resultOrNull(): String? = if (0 == status) {
        stdout?.trim().takeUnless { it.isNullOrBlank() }
    } else {
        throw RuntimeException("command failed\nstatus: $status\nmessage: $stderr")
    }

    protected inline fun <reified T> Output.decode(): T {
        val result = resultOrNull() ?: return throw RuntimeException("command result is empty")
        return Json.Default.decodeFromString<T>(result)
    }
}
