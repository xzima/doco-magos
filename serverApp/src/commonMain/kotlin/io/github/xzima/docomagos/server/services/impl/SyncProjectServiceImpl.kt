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
import io.github.xzima.docomagos.docker.models.ContainerState
import io.github.xzima.docomagos.logging.from
import io.github.xzima.docomagos.server.ext.docker.runningStatuses
import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.SyncProjectService
import io.github.xzima.docomagos.server.services.models.ProjectInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan

private val logger = KotlinLogging.from(SyncProjectServiceImpl::class)

class SyncProjectServiceImpl(
    private val gitProps: GitProps,
) : SyncProjectService {
    override fun addSyncPlanForProject(
        plan: SyncStackPlan,
        actual: ProjectInfo.Actual?,
        expected: ProjectInfo.Expected?,
        ignoreExternal: Boolean,
    ) {
        require(null != actual || null != expected)
        require(null == actual || null == expected || actual.name == expected.name)
        when {
            null == actual -> plan.toUp.add(expected!!)

            !isComposeDeclaredInRepo(actual) -> if (ignoreExternal) {
                logger.warn {
                    "project ${actual.name} is external declared(${actual.manifestPath}) -> update ignored"
                }
                plan.ignored.add(actual)
                expected?.let { plan.ignored.add(it) }
            } else {
                plan.toDown.add(actual)
                expected?.let { plan.toUp.add(it) }
            }

            null == expected -> plan.toDown.add(actual)

            actual.manifestPath != expected.manifestPath -> {
                plan.toDown.add(actual)
                plan.toUp.add(expected)
            }

            else -> plan.toUp.add(expected)
        }
    }

    override fun isUpdateRequiredForProject(
        actual: ProjectInfo.Actual?,
        expected: ProjectInfo.Expected?,
        ignoreExternal: Boolean,
    ): Boolean {
        require(null != actual || null != expected)
        require(null == actual || null == expected || actual.name == expected.name)
        return when {
            null == actual -> true

            !isComposeDeclaredInRepo(actual) -> {
                if (ignoreExternal) {
                    logger.warn {
                        "project ${actual.name} is external declared(${actual.manifestPath}) -> update ignored"
                    }
                }
                !ignoreExternal
            }

            null == expected -> true

            actual.manifestPath != expected.manifestPath -> true

            else -> isUpdateRequiredByStatuses(actual.statuses)
        }
    }

    private fun isUpdateRequiredByStatuses(statuses: Map<ContainerState.Status, Int>): Boolean {
        val statusSet = statuses.filter { (_, count) -> count > 0 }
            .map { it.key }
            .toSet()
        val isProjectFullyUp = ContainerState.Status.runningStatuses.containsAll(statusSet)

        return !isProjectFullyUp
    }

    private fun isComposeDeclaredInRepo(actual: ProjectInfo.Actual): Boolean {
        val manifestPath = actual.manifestPath.toString()
        return gitProps.mainRepoPath != manifestPath && manifestPath.startsWith(gitProps.mainRepoPath)
    }
}
