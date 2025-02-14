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
import io.github.xzima.docomagos.server.ext.dotenv.okioFileReader
import io.github.xzima.docomagos.server.ext.dotenv.parseDotEnvFromReader
import io.github.xzima.docomagos.server.services.FileReadService
import io.github.xzima.docomagos.server.services.GitCryptClient
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.RepoInfo
import kotlinx.coroutines.*
import okio.*

private val logger = KotlinLogging.from(FileReadServiceImpl::class)

class FileReadServiceImpl(
    private val gitCryptClient: GitCryptClient,
) : FileReadService {
    override suspend fun checkRepoEncryption(repoRoot: String, keyFilePath: String?) {
        if (null == keyFilePath) {
            val encryptedFiles = gitCryptClient.getEncryptedFiles(repoRoot)
            if (encryptedFiles.isNotEmpty()) {
                throw RuntimeException(
                    "For repo($repoRoot) has encrypted files, but key file not specified: $encryptedFiles",
                )
            }
            logger.info { "Repo not encrypted" }
        } else {
            gitCryptClient.unlockRepo(repoRoot, keyFilePath)
            logger.info { "Repo decryption check successful" }
            gitCryptClient.lockRepo(repoRoot)
            logger.info { "Repo encryption check successful" }
        }
    }

    override suspend fun readAndMergeEnvs(info: RepoInfo, maskSecrets: Boolean): Map<String, String> =
        withContext(Dispatchers.IO) {
            var repoDecrypted = false
            try {
                repoDecrypted = decryptRepoIfNeed(
                    info.path,
                    info.encryptionKeyFilePath,
                    info.envPath,
                    info.secretEnvPath,
                )

                return@withContext readEnvs(
                    info.envPath to false,
                    info.secretEnvPath to maskSecrets,
                )
            } finally {
                encryptRepoIfNeed(repoDecrypted, info.path)
            }
        }

    override suspend fun readAndMergeEnvs(info: ProjectInfo.Expected, maskSecrets: Boolean): Map<String, String> =
        withContext(Dispatchers.IO) {
            var repoDecrypted = false
            try {
                repoDecrypted = decryptRepoIfNeed(
                    info.repoPath,
                    info.repoEncryptionKeyFilePath,
                    info.repoEnvPath,
                    info.repoSecretEnvPath,
                    info.projectEnvPath,
                    info.projectSecretEnvPath,
                )

                return@withContext readEnvs(
                    info.repoEnvPath to false,
                    info.repoSecretEnvPath to maskSecrets,
                    info.projectEnvPath to false,
                    info.projectSecretEnvPath to maskSecrets,
                )
            } finally {
                encryptRepoIfNeed(repoDecrypted, info.repoPath)
            }
        }

    private fun readEnvs(vararg elements: Pair<Path?, Boolean>): Map<String, String> {
        val accumulator = mutableMapOf<String, String>()
        for ((path, isNeedMask) in elements) {
            if (null == path) continue

            val envs = parseDotEnvFromReader(okioFileReader(path))
            logger.trace { "read env file $path: $envs" }

            val maskedEnvs = if (isNeedMask) {
                envs.mapValues { "***" }
            } else {
                envs
            }

            accumulator.putAll(maskedEnvs)
        }

        logger.trace { "env aggregation result: $accumulator" }
        return accumulator
    }

    private suspend fun decryptRepoIfNeed(
        repoPath: Path,
        repoEncryptionKeyFilePath: Path?,
        vararg envPaths: Path?,
    ): Boolean {
        if (null == repoEncryptionKeyFilePath) {
            logger.debug { "Repo encryption not need" }
            return false
        }

        val encryptedFiles = gitCryptClient.getEncryptedFiles(repoPath.toString()).map {
            repoPath.resolve(it)
        }.toSet()
        if (encryptedFiles.isEmpty()) {
            logger.debug { "Repo don't contain encrypted files" }
            return false
        }

        val needDecrypt = envPaths.any { null != it && it in encryptedFiles }
        if (!needDecrypt) {
            logger.debug { "All readable files are not encrypted" }
            return false
        }

        gitCryptClient.unlockRepo(repoPath.toString(), repoEncryptionKeyFilePath.toString())
        logger.debug { "Repo decrypt successfully" }
        return true
    }

    private suspend fun encryptRepoIfNeed(repoDecrypted: Boolean, repoPath: Path) {
        if (repoDecrypted) {
            try {
                gitCryptClient.lockRepo(repoPath.toString())
                logger.debug { "Repo encrypt successfully" }
            } catch (e: Exception) {
                logger.error(e) { "Repo encrypt failed" }
            }
        }
    }
}
