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
import io.github.xzima.docomagos.server.services.DockerComposeClient
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.FileReadService
import io.github.xzima.docomagos.server.services.models.ComposeProjectInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan
import okio.Path.Companion.toPath

private val logger = KotlinLogging.from(DockerComposeServiceImpl::class)

class DockerComposeServiceImpl(
    private val dockerComposeClient: DockerComposeClient,
    private val fileReadService: FileReadService,
) : DockerComposeService {
    companion object {
        private const val PROJECT_STATUS_REGEX_STATUS_NAME = "status"
        private const val PROJECT_STATUS_REGEX_COUNT_NAME = "count"
        private val PROJECT_STATUS_REGEX = Regex("""^(?<status>\w*)\((?<count>\d*)\)$""")
    }

    override fun executeSyncPlan(syncPlan: SyncStackPlan) {
        logger.trace { "Ignored stacks: ${syncPlan.ignored}" }

        val toDown = syncPlan.toDown.sortedByDescending { it.order }
        logger.trace { "Down stacks: $toDown" }
        for (item in toDown) {
            try {
                dockerComposeClient.down(item.name)
            } catch (e: Exception) {
                logger.warn(e) { "Stack $item down failed" }
            }
        }

        val toUp = syncPlan.toUp.sortedBy { it.order }
        logger.trace { "Up stacks: $toUp" }
        for (item in toUp) {
            try {
                val envs = fileReadService.readAndMergeEnvs(item, maskSecrets = false)
                when {
                    logger.isTraceEnabled() -> logger.trace { "Stack ${item.name} envs: $envs" }
                    logger.isDebugEnabled() -> logger.debug { "Stack ${item.name} envs: ${envs.mapValues { "***" }}" }
                }

                dockerComposeClient.up(
                    manifestPath = item.manifestPath,
                    stackName = item.name,
                    stackPath = item.stackPath,
                    envs = envs,
                )
            } catch (e: Exception) {
                logger.warn(e) { "Stack $item up failed" }
            }
        }
    }

    override fun listProjects(): List<ComposeProjectInfo> = dockerComposeClient.listProjects().map { p ->

        val statuses = mutableMapOf<ContainerState.Status, Int>()
        for (item in p.status.split(",")) {
            val clearItem = item.trim()
            val match = PROJECT_STATUS_REGEX.find(clearItem) ?: continue
            val statusStr = match.groups[PROJECT_STATUS_REGEX_STATUS_NAME]?.value ?: continue
            val status = ContainerState.Status.entries.firstOrNull { it.value == statusStr } ?: continue
            val count = match.groups[PROJECT_STATUS_REGEX_COUNT_NAME]?.value?.toIntOrNull() ?: 0
            statuses[status] = count
        }
        logger.debug { "Status parsing for project(${p.name}): ${p.status} -> $statuses" }

        ComposeProjectInfo(
            name = p.name,
            statuses = statuses,
            manifestPath = p.manifestPath.toPath(),
        )
    }
}
