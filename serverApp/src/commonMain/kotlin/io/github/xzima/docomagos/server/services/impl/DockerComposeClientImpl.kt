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
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.services.DockerComposeClient
import io.github.xzima.docomagos.server.services.models.DCProjectInfo
import io.github.xzima.docomagos.server.services.models.DCVersion
import kotlinx.coroutines.*
import okio.*

private val logger = KotlinLogging.from(DockerComposeClientImpl::class)

class DockerComposeClientImpl(
    private val dryRun: Boolean,
) : KommandClient("docker-compose"),
    DockerComposeClient {

    override suspend fun version(): DCVersion = withContext(Dispatchers.IO) {
        val output = cmd {
            args("version", "--format=json")
        }
        logger.debug { "version result: $output" }

        return@withContext output.decode<DCVersion>()
    }

    override suspend fun listProjects(): List<DCProjectInfo> = withContext(Dispatchers.IO) {
        val output = cmd {
            args("ls", "--format=json")
        }
        logger.debug { "ls result: $output" }

        return@withContext output.decode<List<DCProjectInfo>>()
    }

    override suspend fun down(stackName: String) {
        val output = cmd {
            args("-p=$stackName", "down", "--remove-orphans", "--rmi=all", "-v")
        }
        logger.debug { "down result: $output" }

        output.resultOrNull()
        output.propagateWarn()
    }

    override suspend fun up(manifestPath: Path, stackName: String, stackPath: Path, envs: Map<String, String>) {
        val output = cmd {
            args("-p=$stackName", "--project-directory=$stackPath", "-f=$manifestPath", "up", "-d", "--remove-orphans")
            envs(envs)
        }
        logger.debug { "up result: $output" }

        output.resultOrNull()
        output.propagateWarn()
    }

    override fun cmd(builder: Command.() -> Unit): Output = super.cmd {
        builder()
        if (dryRun) {
            arg("--dry-run")
        }
    }

    private fun Output.propagateWarn() = logger.warn {
        val lines = sequenceOf(stdout, stderr)
            .flatMap { it?.split("\n") ?: emptyList() }
            .filter { it.contains("warning", ignoreCase = true) }
            .toList()
        if (lines.isNotEmpty()) {
            return@warn lines.joinToString(prefix = "executions warnings:\n", separator = "\n")
        }
    }
}
