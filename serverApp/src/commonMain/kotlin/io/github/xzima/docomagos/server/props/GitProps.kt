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
package io.github.xzima.docomagos.server.props

import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required

interface GitProps {
    val mainRepoPath: String
    val mainRepoUrl: String
    val mainRepoRemote: String
    val mainRepoBranch: String

    /**
     * example:
     *
     * ```
     * #!/bin/bash
     * [[ -v GIT_TOKEN ]] && echo $GIT_TOKEN || ([[ -v GIT_TOKEN_FILE ]] && cat $GIT_TOKEN_FILE)
     * ```
     */
    val gitAskPass: String
    val gitToken: String?
    val gitTokenFile: String?
}

class GitOptionGroup :
    OptionGroup(),
    GitProps {
    override val mainRepoPath: String by option(valueSourceKey = "git.main-repo-path").required()
    override val mainRepoUrl: String by option(valueSourceKey = "git.main-repo-url").required()
    override val mainRepoRemote: String by option(valueSourceKey = "git.main-repo-remote").required()
    override val mainRepoBranch: String by option(valueSourceKey = "git.main-repo-branch").required()
    override val gitAskPass: String by option(valueSourceKey = "git.git-ask-pass").required()
    override val gitToken: String? by option(valueSourceKey = "git.git-token")
    override val gitTokenFile: String? by option(valueSourceKey = "git.git-token-file")
}
