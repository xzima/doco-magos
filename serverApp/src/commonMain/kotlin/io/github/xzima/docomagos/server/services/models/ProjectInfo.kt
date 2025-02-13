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
package io.github.xzima.docomagos.server.services.models

import io.github.xzima.docomagos.docker.models.ContainerState
import okio.*

sealed interface ProjectInfo {
    val name: String
    val manifestPath: Path
    val order: Int

    data class Actual(
        override val name: String,
        override val manifestPath: Path,
        override val order: Int,
        val statuses: Map<ContainerState.Status, Int>,
    ) : ProjectInfo {
        constructor(projectInfo: ComposeProjectInfo, order: Int? = Int.MAX_VALUE) : this(
            name = projectInfo.name,
            manifestPath = projectInfo.manifestPath,
            order = order ?: Int.MAX_VALUE,
            statuses = projectInfo.statuses,
        )
    }

    data class Expected(
        override val name: String,
        override val manifestPath: Path,
        override val order: Int,
        val stackPath: Path,
        val envPaths: List<Path>,
    ) : ProjectInfo {
        constructor(projectInfo: RepoProjectInfo, repoInfo: RepoInfo) : this(
            name = projectInfo.name,
            manifestPath = projectInfo.manifestPath,
            order = projectInfo.order,
            stackPath = projectInfo.path,
            envPaths = listOfNotNull(
                repoInfo.envPath,
                repoInfo.secretEnvPath,
                projectInfo.envPath,
                projectInfo.secretEnvPath,
            ),
        )
    }
}
