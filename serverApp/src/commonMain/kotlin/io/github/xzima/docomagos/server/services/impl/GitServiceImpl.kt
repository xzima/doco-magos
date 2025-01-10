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

import io.github.xzima.docomagos.server.props.GitProps
import io.github.xzima.docomagos.server.services.GitClient
import io.github.xzima.docomagos.server.services.GitService

class GitServiceImpl(
    private val gitEnv: GitProps,
    private val gitClient: GitClient,
) : GitService {

    override suspend fun checkMainRepoPath() {
        val realPath = gitClient.getRepoPathBy(gitEnv.mainRepoPath)
        if (gitEnv.mainRepoPath != realPath) {
            throw RuntimeException("unexpected git repo path. expected: ${gitEnv.mainRepoPath} actual: $realPath")
        }
    }

    override suspend fun checkMainRepoUrl() {
        val realUrl = gitClient.getRepoUrlBy(gitEnv.mainRepoPath, gitEnv.mainRepoRemote)
        if (gitEnv.mainRepoUrl != realUrl) {
            throw RuntimeException("unexpected git repo url. expected: ${gitEnv.mainRepoUrl} actual: $realUrl")
        }
    }

    override suspend fun checkMainRepoHead() {
        gitClient.fetchRemote(
            gitEnv.mainRepoPath,
            gitEnv.mainRepoRemote,
            gitToken = gitEnv.gitToken,
            gitTokenFile = gitEnv.gitTokenFile,
        )
        gitClient.getLastCommitByRef(
            gitEnv.mainRepoPath,
            "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}",
        )
    }

    override suspend fun isActualRepoHead(): Boolean {
        val headLastCommit = gitClient.getLastCommitByRef(gitEnv.mainRepoPath, GitClient.HEAD_REF_NAME)
        val remoteLastCommit = gitClient.getLastCommitByRef(
            gitEnv.mainRepoPath,
            "${gitEnv.mainRepoRemote}/${gitEnv.mainRepoBranch}",
        )
        return headLastCommit == remoteLastCommit
    }
}
