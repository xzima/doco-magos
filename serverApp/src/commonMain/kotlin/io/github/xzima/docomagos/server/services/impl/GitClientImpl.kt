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
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.models.GitVersion
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(GitClientImpl::class)

class GitClientImpl(
    private val gitAskPass: String,
) : GitClient {
    companion object {
        private const val GIT_ASK_PASS_ENV_NAME = "GIT_ASKPASS"
        private const val GIT_TOKEN_ENV_NAME = "GIT_TOKEN"
        private const val GIT_TOKEN_FILE_ENV_NAME = "GIT_TOKEN_FILE"

        private val VERSION_REGEX = Regex("^git version (?<version>.*)$")
    }

    override suspend fun version(): GitVersion? = withContext(Dispatchers.IO) {
        val output = gitCommand {
            args("--version")
        }
        logger.debug { "version result: $output" }

        val result = output.resultOrNull() ?: return@withContext null
        val version = VERSION_REGEX.find(result)
            ?.groups
            ?.get("version")
            ?.value
            ?.trim() ?: return@withContext null

        return@withContext GitVersion(version)
    }

    /**
     * @return is success
     */
    override suspend fun cloneRepo(
        repoUrl: String,
        repoPath: String,
        gitToken: String?,
        gitTokenFile: String?,
    ): Boolean = withContext(Dispatchers.IO) {
        val output = gitCommand {
            args("clone", repoUrl, repoPath)
            configureCredentials(gitToken = gitToken, gitTokenFile = gitTokenFile)
        }
        logger.debug { "clone result: $output" }

        return@withContext 0 == output.status
    }

    /**
     * @return repo root path
     */
    override suspend fun getRepoPathBy(repoPath: String): String? = withContext(Dispatchers.IO) {
        val output = gitCommand {
            cwd(repoPath)
            args("rev-parse", "--show-toplevel")
        }

        logger.debug { "get repo path result: $output" }

        return@withContext output.resultOrNull()
    }

    /**
     * @return repo origin url
     */
    override suspend fun getRepoUrlBy(repoPath: String, remote: String): String? = withContext(Dispatchers.IO) {
        val output = gitCommand {
            cwd(repoPath)
            args("remote", "get-url", remote)
        }

        logger.debug { "get repo url result: $output" }

        return@withContext output.resultOrNull()
    }

    override suspend fun fetchRemote(
        repoPath: String,
        remote: String,
        gitToken: String?,
        gitTokenFile: String?,
    ): Unit = withContext(Dispatchers.IO) {
        val output = gitCommand {
            cwd(repoPath)
            args("fetch", remote)
            configureCredentials(gitToken = gitToken, gitTokenFile = gitTokenFile)
        }

        logger.debug { "fetch repo result: $output" }

        output.resultOrNull()
    }

    override suspend fun hardResetHeadToRef(repoPath: String, ref: String): Boolean = withContext(Dispatchers.IO) {
        val output = gitCommand {
            cwd(repoPath)
            args("reset", "--hard", ref)
        }

        logger.debug { "hard reset repo result: $output" }

        return@withContext 0 == output.status
    }

    override suspend fun getLastCommitByRef(repoPath: String, ref: String): String? = withContext(Dispatchers.IO) {
        val output = gitCommand {
            cwd(repoPath)
            args("rev-parse", ref)
        }

        logger.debug { "get last commit by ref=$ref result: $output" }

        return@withContext output.resultOrNull()
    }

    private fun Command.configureCredentials(gitToken: String? = null, gitTokenFile: String? = null) {
        val gitToken = gitToken.takeUnless { it.isNullOrBlank() }
        val gitTokenFile = gitTokenFile.takeUnless { it.isNullOrBlank() }
        if (null != gitToken || null != gitTokenFile) {
            env(GIT_ASK_PASS_ENV_NAME, gitAskPass)
            if (null != gitTokenFile) {
                env(GIT_TOKEN_FILE_ENV_NAME, gitTokenFile)
            }
            if (null != gitToken) {
                env(GIT_TOKEN_ENV_NAME, gitToken)
            }
        }
    }

    private fun gitCommand(builder: Command.() -> Unit): Output {
        val command = Command("git").stdout(Stdio.Pipe).stderr(Stdio.Pipe)
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
