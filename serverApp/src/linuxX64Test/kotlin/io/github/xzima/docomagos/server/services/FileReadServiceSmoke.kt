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
package io.github.xzima.docomagos.server.services

import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.impl.FileReadServiceImpl
import io.github.xzima.docomagos.server.services.impl.GitClientImpl
import io.github.xzima.docomagos.server.services.impl.GitCryptClientImpl
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.RepoInfo
import kotlinx.coroutines.*
import okio.*
import okio.Path.Companion.toPath
import kotlin.test.BeforeClass
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test

private val logger = KotlinLogging.from(FileReadServiceSmoke::class)

class FileReadServiceSmoke {
    companion object {
        @BeforeClass
        fun setUp() = KotlinLogging.configureLogging(Level.TRACE)
    }

    private lateinit var repoRoot: Path
    private lateinit var keyFile: Path
    private lateinit var fileReadService: FileReadService

    @BeforeTest
    fun setUp(): Unit = runBlocking {
        val pwdPath = FileSystem.Companion.SYSTEM.canonicalize("./".toPath())

        keyFile = pwdPath.resolve("../.git-crypt-key", normalize = true)

        repoRoot = pwdPath.resolve("build/repo", normalize = true)
        FileSystem.Companion.SYSTEM.deleteRecursively(repoRoot, false)

        val gitProps = object : GitProps {
            override val mainRepoPath: String = repoRoot.toString()
            override val mainRepoUrl: String = "https://github.com/xzima/home-composes.git"
            override val mainRepoRemote: String = "origin"
            override val mainRepoBranch: String = "master"
            override val gitAskPass: String = pwdPath.resolve("../GIT_ASKPASS", normalize = true).toString()
            override val gitToken: String? = null
            override val gitTokenFile: String? = pwdPath.resolve("../.git-token", normalize = true).toString()
        }

        val gitCryptClient = GitCryptClientImpl()
        fileReadService = FileReadServiceImpl(gitCryptClient)
        val gitClient = GitClientImpl(gitProps.gitAskPass)
        gitClient.cloneRepo(gitProps.mainRepoUrl, gitProps.mainRepoPath, gitTokenFile = gitProps.gitTokenFile)
    }

    @Test
    @Ignore
    fun testRead(): Unit = runBlocking {
        // GIVEN
        val repoInfo = RepoInfo.BaseRepoInfo(
            path = repoRoot,
            encryptionKeyFilePath = keyFile,
            secretEnvPath = repoRoot.resolve("global.secret.env"),
            envPath = repoRoot.resolve("global.env"),
        )
        val projectInfo = ProjectInfo.Expected(
            name = "traefik",
            manifestPath = repoRoot.resolve("traefik/compose.yml"),
            order = 1,
            stackPath = repoRoot.resolve("traefik"),
            repoPath = repoRoot,
            repoEncryptionKeyFilePath = keyFile,
            repoEnvPath = repoRoot.resolve("global.env"),
            repoSecretEnvPath = repoRoot.resolve("global.secret.env"),
            projectEnvPath = repoRoot.resolve("traefik/.env"),
            projectSecretEnvPath = null,
        )
        // WHEN
        fileReadService.readAndMergeEnvs(repoInfo, maskSecrets = true).also {
            logger.info { "repoInfo(maskSecrets = true)\n$it" }
        }
        fileReadService.readAndMergeEnvs(repoInfo, maskSecrets = false).also {
            logger.info { "repoInfo(maskSecrets = false)\n$it" }
        }
        fileReadService.readAndMergeEnvs(projectInfo, maskSecrets = true).also {
            logger.info { "projectInfo(maskSecrets = true)\n$it" }
        }
        fileReadService.readAndMergeEnvs(projectInfo, maskSecrets = false).also {
            logger.info { "projectInfo(maskSecrets = false)\n$it" }
        }
    }
}
