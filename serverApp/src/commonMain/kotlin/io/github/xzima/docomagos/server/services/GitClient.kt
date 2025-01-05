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

import io.github.xzima.docomagos.server.services.models.GitVersion

interface GitClient {
    companion object {
        const val HEAD_REF_NAME = "HEAD"
    }

    suspend fun version(): GitVersion?

    /**
     * @return is success
     */
    suspend fun cloneRepo(
        repoUrl: String,
        repoPath: String,
        gitToken: String? = null,
        gitTokenFile: String? = null,
    ): Boolean

    /**
     * @return repo root path
     */
    suspend fun getRepoPathBy(repoPath: String): String?

    /**
     * @return repo origin url
     */
    suspend fun getRepoUrlBy(repoPath: String, remote: String): String?

    suspend fun fetchRemote(repoPath: String, remote: String, gitToken: String? = null, gitTokenFile: String? = null)

    suspend fun hardResetHeadToRef(repoPath: String, ref: String): Boolean

    suspend fun getLastCommitByRef(repoPath: String, ref: String): String?
}
