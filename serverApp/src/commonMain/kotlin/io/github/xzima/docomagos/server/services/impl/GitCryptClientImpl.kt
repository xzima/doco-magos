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

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.services.GitCryptClient

private val logger = KotlinLogging.from(GitCryptClientImpl::class)

class GitCryptClientImpl :
    KommandClient("git-crypt"),
    GitCryptClient {
    companion object {

        private val VERSION_REGEX = Regex("^git-crypt (?<version>.*)$")
    }

    override fun version(): String? {
        val output = cmd {
            args("version")
        }
        logger.debug { "version result: $output" }

        val result = output.resultOrNull() ?: return null
        val version = VERSION_REGEX.find(result)
            ?.groups
            ?.get("version")
            ?.value
            ?.trim() ?: return null

        return version
    }

    override fun getEncryptedFiles(repoRoot: String): Set<String> {
        val output = cmd {
            cwd(repoRoot)
            args("status", "-e")
        }
        logger.debug { "status result: $output" }

        val result = output.resultOrNull() ?: return emptySet()
        val files = result.splitToSequence("\n").mapNotNull {
            val items = it.split(":")
            if (2 != items.size) return@mapNotNull null
            items[1].trim()
        }

        return files.toSet()
    }

    override fun unlockRepo(repoRoot: String, keyFilePath: String) {
        val output = cmd {
            cwd(repoRoot)
            args("unlock", keyFilePath)
        }
        logger.debug { "unlock result: $output" }

        output.resultOrNull()
    }

    override fun lockRepo(repoRoot: String) {
        val output = cmd {
            cwd(repoRoot)
            args("lock")
        }
        logger.debug { "lock result: $output" }

        output.resultOrNull()
    }
}
