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
package io.github.xzima.docomagos.server.services

import com.kgit2.kommand.process.Command
import com.kgit2.kommand.process.Stdio
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.services.models.DCListProjects
import io.github.xzima.docomagos.server.services.models.DCVersion
import kotlinx.coroutines.*
import kotlinx.serialization.json.*

private val logger = KotlinLogging.from(DockerComposeService::class)

class DockerComposeService {

    suspend fun version(): DCVersion = withContext(Dispatchers.IO) {
        val command = Command("docker-compose")
            .args("version", "--format=json")
            .stdout(Stdio.Pipe)
        val child = command.spawn()
        val output = child.waitWithOutput()
        logger.info { "version result: $output" }
        Json.decodeFromString<DCVersion>(output.stdout!!)
    }

    suspend fun listProjects(): List<DCListProjects> = withContext(Dispatchers.IO) {
        val command = Command("docker-compose")
            .args("ls", "--format=json")
            .stdout(Stdio.Pipe)
        val child = command.spawn()
        val output = child.waitWithOutput()
        // output.status == 0 //isOk
        logger.info { "ls result: $output" }
        Json.decodeFromString<List<DCListProjects>>(output.stdout!!)
    }
}
