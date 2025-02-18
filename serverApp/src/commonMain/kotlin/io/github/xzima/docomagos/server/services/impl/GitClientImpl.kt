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
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.models.GitVersion
import kotlinx.coroutines.*

private val logger = KotlinLogging.from(GitClientImpl::class)

class GitClientImpl(
    private val gitAskPass: String,
) : KommandClient("git"),
    GitClient {
    companion object {
        private const val GIT_ASK_PASS_ENV_NAME = "GIT_ASKPASS"
        private const val GIT_TOKEN_ENV_NAME = "GIT_TOKEN"
        private const val GIT_TOKEN_FILE_ENV_NAME = "GIT_TOKEN_FILE"
        private const val GIT_TERMINAL_PROMPT_ENV_NAME = "GIT_TERMINAL_PROMPT"

        private val VERSION_REGEX = Regex("^git version (?<version>.*)$")
    }

    override fun version(): GitVersion? {
        val output = cmd {
            args("--version")
        }
        logger.debug { "version result: $output" }

        val result = output.resultOrNull() ?: return null
        val version = VERSION_REGEX.find(result)
            ?.groups
            ?.get("version")
            ?.value
            ?.trim() ?: return null

        return GitVersion(version)
    }

    /**
     * @return is success
     */
    override fun cloneRepo(repoUrl: String, repoPath: String, gitToken: String?, gitTokenFile: String?): Boolean {
        val output = cmd {
            args("clone", repoUrl, repoPath)
            configureCredentials(gitToken = gitToken, gitTokenFile = gitTokenFile)
        }
        logger.debug { "clone result: $output" }

        return 0 == output.status
    }

    /**
     * @return repo root path
     */
    override fun getRepoPathBy(repoPath: String): String? {
        val output = cmd {
            cwd(repoPath)
            args("rev-parse", "--show-toplevel")
        }

        logger.debug { "get repo path result: $output" }

        return output.resultOrNull()
    }

    /**
     * @return repo origin url
     */
    override fun getRepoUrlBy(repoPath: String, remote: String): String? {
        val output = cmd {
            cwd(repoPath)
            args("remote", "get-url", remote)
        }

        logger.debug { "get repo url result: $output" }

        return output.resultOrNull()
    }

    override fun fetchRemote(repoPath: String, remote: String, gitToken: String?, gitTokenFile: String?) {
        val output = cmd {
            cwd(repoPath)
            args("fetch", remote)
            configureCredentials(gitToken = gitToken, gitTokenFile = gitTokenFile)
        }

        logger.debug { "fetch repo result: $output" }

        output.resultOrNull()
    }

    override fun hardResetHeadToRef(repoPath: String, ref: String): Boolean {
        val output = cmd {
            cwd(repoPath)
            args("reset", "--hard", ref)
        }

        logger.debug { "hard reset repo result: $output" }

        return 0 == output.status
    }

    override fun getLastCommitByRef(repoPath: String, ref: String): String? {
        val output = cmd {
            cwd(repoPath)
            args("rev-parse", ref)
        }

        logger.debug { "get last commit by ref=$ref result: $output" }

        return output.resultOrNull()
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

    override fun cmd(builder: Command.() -> Unit): Output = super.cmd {
        builder()
        env(GIT_TERMINAL_PROMPT_ENV_NAME, "0") // disable prompting for all commands
    }
}
