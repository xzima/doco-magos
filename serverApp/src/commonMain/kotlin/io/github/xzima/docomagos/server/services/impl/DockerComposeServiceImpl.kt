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
import io.github.xzima.docomagos.server.services.DockerComposeClient
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.FileReadService
import io.github.xzima.docomagos.server.services.models.ComposeProjectInfo
import io.github.xzima.docomagos.server.services.models.SyncStackPlan

private val logger = KotlinLogging.from(DockerComposeServiceImpl::class)

class DockerComposeServiceImpl(
    private val dockerComposeClient: DockerComposeClient,
    private val fileReadService: FileReadService,
) : DockerComposeService {
    override suspend fun executeSyncPlan(syncPlan: SyncStackPlan) {
        logger.debug { "Ignored stacks: ${syncPlan.ignored}" }

        val toDown = syncPlan.toDown.sortedByDescending { it.order }
        logger.debug { "Down stacks: $toDown" }
        for (item in toDown) {
            try {
                dockerComposeClient.down(item.name)
            } catch (e: Exception) {
                logger.warn(e) { "Stack $item down failed" }
            }
        }

        val toUp = syncPlan.toUp.sortedBy { it.order }
        logger.debug { "Up stacks: $toUp" }
        for (item in toUp) {
            try {
                val envs = fileReadService.readAndMergeEnvs(item, maskSecrets = false)
                when {
                    logger.isDebugEnabled() -> logger.debug { "Stack ${item.name} envs: ${envs.mapValues { "***" }}" }
                    logger.isTraceEnabled() -> logger.trace { "Stack ${item.name} envs: $envs" }
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

    override suspend fun listProjects(): List<ComposeProjectInfo> {
        TODO("Not yet implemented")
    }
}
