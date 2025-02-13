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
import io.github.xzima.docomagos.server.props.RepoStructureProps
import io.github.xzima.docomagos.server.services.RepoStructureService
import io.github.xzima.docomagos.server.services.models.RepoInfo
import kotlinx.coroutines.*
import okio.*

private val logger = KotlinLogging.from(RepoStructureServiceImpl::class)

class RepoStructureServiceImpl(
    private val repoProps: RepoStructureProps,
) : RepoStructureService {
    companion object {
        private const val PROJECT_NAME_REGEX_GROUP_NAME = "name"
        private const val PROJECT_ORDER_REGEX_GROUP_NAME = "order"
    }

    override suspend fun getBaseInfo(repoPath: Path, repoEncryptionKeyPath: Path?): RepoInfo.BaseRepoInfo =
        withContext(Dispatchers.IO) {
            val globalSecretEnvPaths = mutableListOf<Path>()
            val globalEnvPaths = mutableListOf<Path>()
            for (repoEntryPath in FileSystem.Companion.SYSTEM.list(repoPath)) {
                val repoEntryMeta = FileSystem.Companion.SYSTEM.metadata(repoEntryPath)
                if (repoEntryMeta.isRegularFile) {
                    if (!repoProps.globalEnvPattern.containsMatchIn(repoEntryPath.name)) continue
                    when {
                        repoProps.secretEnvPattern.containsMatchIn(repoEntryPath.name) -> {
                            globalSecretEnvPaths.add(repoEntryPath)
                        }

                        repoProps.envPattern.containsMatchIn(repoEntryPath.name) -> globalEnvPaths.add(repoEntryPath)
                    }
                }
            }
            val globalSecretEnvPath = globalSecretEnvPaths.singleOrWarn(
                "can't select secret env file for repo($repoPath) from: $globalSecretEnvPaths",
            )

            val globalEnvPath = globalEnvPaths.singleOrWarn(
                "can't select env file for repo($repoPath) from: $globalEnvPaths",
            )

            return@withContext RepoInfo.BaseRepoInfo(
                path = repoPath,
                encryptionKeyFilePath = repoEncryptionKeyPath,
                secretEnvPath = globalSecretEnvPath,
                envPath = globalEnvPath,
            )
        }

    override suspend fun getFullInfo(repoPath: Path, repoEncryptionKeyPath: Path?): RepoInfo.FullRepoInfo =
        withContext(Dispatchers.IO) {
            val globalEnvPaths = mutableListOf<Path>()
            val globalSecretEnvPaths = mutableListOf<Path>()
            val projects = mutableListOf<RepoInfo.FullRepoInfo.RepoProjectInfo>()
            for (repoEntryPath in FileSystem.Companion.SYSTEM.list(repoPath)) {
                val repoEntryMeta = FileSystem.Companion.SYSTEM.metadata(repoEntryPath)
                when {
                    repoEntryMeta.isRegularFile -> {
                        if (!repoProps.globalEnvPattern.containsMatchIn(repoEntryPath.name)) continue
                        when {
                            repoProps.secretEnvPattern.containsMatchIn(repoEntryPath.name) -> {
                                globalSecretEnvPaths.add(repoEntryPath)
                            }

                            repoProps.envPattern.containsMatchIn(repoEntryPath.name) -> {
                                globalEnvPaths.add(repoEntryPath)
                            }

                            else -> logger.warn {
                                "File $repoEntryPath match global env pattern, but not match specified env patterns"
                            }
                        }
                    }

                    repoEntryMeta.isDirectory -> {
                        val regexGroups = repoProps.projectNamePattern.find(repoEntryPath.name)?.groups ?: continue
                        val projectName = regexGroups[PROJECT_NAME_REGEX_GROUP_NAME]?.value ?: continue
                        val projectOrder = regexGroups[PROJECT_ORDER_REGEX_GROUP_NAME]?.value?.toInt() ?: Int.MAX_VALUE

                        val (composePaths, projectEnvPaths, projectSecretEnvPaths) = getProjectInfo(repoEntryPath)

                        val composePath = composePaths.singleOrWarn(
                            "ignore project($repoEntryPath) because too many composes: $composePaths",
                        ) ?: continue

                        val secretEnvPath = projectSecretEnvPaths.singleOrWarn(
                            "can't select secret env file for project($repoEntryPath) from: $projectSecretEnvPaths",
                        )

                        val envPath = projectEnvPaths.singleOrWarn(
                            "can't select env file for project($repoEntryPath) from: $projectEnvPaths",
                        )

                        projects.add(
                            RepoInfo.FullRepoInfo.RepoProjectInfo(
                                name = projectName,
                                path = repoEntryPath,
                                order = projectOrder,
                                manifestPath = composePath,
                                secretEnvPath = secretEnvPath,
                                envPath = envPath,
                            ),
                        )
                    }
                }
            }

            val globalSecretEnvPath = globalSecretEnvPaths.singleOrWarn(
                "can't select secret env file for repo($repoPath) from: $globalSecretEnvPaths",
            )

            val globalEnvPath = globalEnvPaths.singleOrWarn(
                "can't select env file for repo($repoPath) from: $globalEnvPaths",
            )

            return@withContext RepoInfo.FullRepoInfo(
                path = repoPath,
                encryptionKeyFilePath = repoEncryptionKeyPath,
                secretEnvPath = globalSecretEnvPath,
                envPath = globalEnvPath,
                projects = projects,
            )
        }

    /**
     * @return (composePaths, projectEnvPaths, projectSecretEnvPaths)
     */
    private fun getProjectInfo(projectPath: Path): Triple<List<Path>, List<Path>, List<Path>> {
        val composePaths = mutableListOf<Path>()
        val projectEnvPaths = mutableListOf<Path>()
        val projectSecretEnvPaths = mutableListOf<Path>()
        for (path in FileSystem.Companion.SYSTEM.list(projectPath)) {
            val projectEntryMeta = FileSystem.Companion.SYSTEM.metadata(path)
            if (!projectEntryMeta.isRegularFile) continue

            when {
                repoProps.composeNamePattern.containsMatchIn(path.name) -> composePaths.add(path)
                repoProps.secretEnvPattern.containsMatchIn(path.name) -> projectSecretEnvPaths.add(path)
                repoProps.envPattern.containsMatchIn(path.name) -> projectEnvPaths.add(path)
            }
        }
        return Triple(composePaths, projectEnvPaths, projectSecretEnvPaths)
    }

    private fun <T> List<T>.singleOrWarn(message: String): T? = when (size) {
        0 -> null
        1 -> this[0]
        else -> {
            logger.warn { message }
            null
        }
    }
}
