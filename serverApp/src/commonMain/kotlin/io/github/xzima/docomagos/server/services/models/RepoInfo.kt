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

import okio.*

sealed interface RepoInfo {
    val path: Path
    val encryptionKeyFilePath: Path?
    val secretEnvPath: Path?
    val envPath: Path?

    data class FullRepoInfo(
        override val path: Path,
        override val encryptionKeyFilePath: Path?,
        override val secretEnvPath: Path?,
        override val envPath: Path?,
        val projects: List<RepoProjectInfo>,
    ) : RepoInfo {
        data class RepoProjectInfo(
            val name: String,
            val path: Path,
            val order: Int,
            val manifestPath: Path,
            val secretEnvPath: Path?,
            val envPath: Path?,
        )
    }

    data class BaseRepoInfo(
        override val path: Path,
        override val encryptionKeyFilePath: Path?,
        override val secretEnvPath: Path?,
        override val envPath: Path?,
    ) : RepoInfo
}
