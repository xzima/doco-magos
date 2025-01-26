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
import io.github.xzima.docomagos.server.services.GitCryptClient
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(GitCryptClientImpl::class)

class GitCryptClientImpl : GitCryptClient {
    companion object {

        private val VERSION_REGEX = Regex("^git-crypt (?<version>.*)$")
    }

    override suspend fun version(): String? = withContext(Dispatchers.IO) {
        val output = gitCryptCommand {
            args("version")
        }
        logger.debug { "version result: $output" }

        val result = output.resultOrNull() ?: return@withContext null
        val version = VERSION_REGEX.find(result)
            ?.groups
            ?.get("version")
            ?.value
            ?.trim() ?: return@withContext null

        return@withContext version
    }

    override suspend fun getEncryptedFiles(repoRoot: String): Set<String> = withContext(Dispatchers.IO) {
        val output = gitCryptCommand {
            cwd(repoRoot)
            args("status", "-e")
        }
        logger.debug { "status result: $output" }

        val result = output.resultOrNull() ?: return@withContext emptySet()
        val files = result.splitToSequence("\n").mapNotNull {
            val items = it.split(":")
            if (2 != items.size) return@mapNotNull null
            items[1].trim()
        }

        return@withContext files.toSet()
    }

    override suspend fun unlockRepo(repoRoot: String, keyFile: String): Unit = withContext(Dispatchers.IO) {
        val output = gitCryptCommand {
            cwd(repoRoot)
            args("unlock", keyFile)
        }
        logger.debug { "unlock result: $output" }

        output.resultOrNull()
    }

    override suspend fun lockRepo(repoRoot: String): Unit = withContext(Dispatchers.IO) {
        val output = gitCryptCommand {
            cwd(repoRoot)
            args("lock")
        }
        logger.debug { "lock result: $output" }

        output.resultOrNull()
    }

    private fun gitCryptCommand(builder: Command.() -> Unit): Output {
        val command = Command("git-crypt").stdout(Stdio.Pipe).stderr(Stdio.Pipe)
        command.builder()
        val child = command.spawn()
        val output = child.waitWithOutput()
        return output
    }

    private fun Output.resultOrNull(): String? = if (0 == status) {
        stdout?.trim().takeUnless { it.isNullOrBlank() }
    } else {
        throw RuntimeException("command failed. status: $status message: $stderr")
    }
}
