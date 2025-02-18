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
import io.github.xzima.docomagos.server.props.AppProps
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.RepoStructureService
import io.github.xzima.docomagos.server.services.SyncProjectService
import io.github.xzima.docomagos.server.services.SyncService
import io.github.xzima.docomagos.server.services.models.ComposeProjectInfo
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.RepoInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import okio.Path.Companion.toPath

@Suppress("ktlint:standard:max-line-length")
private typealias MutableProjectInfoAggregation = Pair<MutableList<ComposeProjectInfo>, MutableList<RepoInfo.FullRepoInfo.RepoProjectInfo>>
private typealias ProjectInfoAggregation = Pair<List<ComposeProjectInfo>, List<RepoInfo.FullRepoInfo.RepoProjectInfo>>

private val logger = KotlinLogging.from(SyncServiceImpl::class)

class SyncServiceImpl(
    private val gitProps: GitProps,
    private val appProps: AppProps,
    private val repoStructureService: RepoStructureService,
    private val syncProjectService: SyncProjectService,
    private val dockerComposeService: DockerComposeService,
) : SyncService {
    override fun isMainRepoStacksUpdateRequired(): Boolean = buildProjectInfoSequence().any {
        val (actualInfo, expectedInfo) = it
        val isUpdateRequired = syncProjectService.isUpdateRequiredForProject(
            actual = actualInfo,
            expected = expectedInfo,
            ignoreExternal = appProps.ignoreRepoExternalStacksSync,
        )
        logger.trace { "($actualInfo, $expectedInfo) -> $isUpdateRequired" }
        isUpdateRequired
    }

    override fun createSyncPlanForMainRepo(): SyncStackPlan = buildProjectInfoSequence()
        .fold(SyncStackPlan()) { plan, (actualInfo, expectedInfo) ->
            syncProjectService.addSyncPlanForProject(
                plan = plan,
                actual = actualInfo,
                expected = expectedInfo,
                ignoreExternal = appProps.ignoreRepoExternalStacksSync,
            )
            logger.trace { "($actualInfo, $expectedInfo) -> $plan" }
            plan
        }

    private fun buildProjectInfoSequence(): Sequence<Pair<ProjectInfo.Actual?, ProjectInfo.Expected?>> {
        val allComposeProjects = dockerComposeService.listProjects()
        val repoInfo = repoStructureService.getFullInfo(
            repoPath = gitProps.mainRepoPath.toPath(),
            repoEncryptionKeyPath = gitProps.gitCryptKeyFile?.toPath(),
        )
        when {
            logger.isTraceEnabled() -> logger.trace {
                "Project info aggregation complete:\ncompose: $allComposeProjects\nrepo: $repoInfo"
            }

            logger.isDebugEnabled() -> logger.debug { "Project info aggregation complete" }
        }

        val nameToProjects = groupProjectsByName(allComposeProjects, repoInfo)
        when {
            logger.isTraceEnabled() -> logger.trace { "Project info groping by name complete:\n$nameToProjects" }
            logger.isDebugEnabled() -> logger.debug { "Project info groping by name complete" }
        }

        return sequence {
            for (item in nameToProjects) {
                val (projectName, value) = item
                val (composeProjects, repoProjects) = value

                if (composeProjects.size > 1) {
                    logger.warn {
                        "[Ignore $projectName] Find too many deployed projects with same name: $composeProjects"
                    }
                    continue
                }
                if (repoProjects.size > 1) {
                    logger.warn { "[Ignore $projectName] Find too many projects in repo with same name: $repoProjects" }
                    continue
                }

                val expectedInfo = repoProjects.firstOrNull()?.let {
                    ProjectInfo.Expected(it, repoInfo)
                }
                val actualInfo = composeProjects.firstOrNull()?.let {
                    ProjectInfo.Actual(it, order = expectedInfo?.order)
                }

                yield(actualInfo to expectedInfo)
            }
        }
    }

    private fun groupProjectsByName(
        allComposeProjects: List<ComposeProjectInfo>,
        repoInfo: RepoInfo.FullRepoInfo,
    ): Map<String, ProjectInfoAggregation> {
        val nameToProjects = mutableMapOf<String, MutableProjectInfoAggregation>()
        for (info in allComposeProjects) {
            val (item, _) = nameToProjects.getOrPut(info.name) { Pair(mutableListOf(), mutableListOf()) }
            item.add(info)
        }
        for (info in repoInfo.projects) {
            val (_, item) = nameToProjects.getOrPut(info.name) { Pair(mutableListOf(), mutableListOf()) }
            item.add(info)
        }
        return nameToProjects
    }
}
