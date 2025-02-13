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
import com.github.ajalt.clikt.parameters.options.required

interface RepoStructureProps {
    val envPattern: Regex
    val secretEnvPattern: Regex
    val globalEnvPattern: Regex
    val projectNamePattern: Regex
    val composeNamePattern: Regex
}

class RepoStructureOptionGroup :
    OptionGroup(),
    RepoStructureProps {
    override val envPattern: Regex by customOption("repo-structure.env-pattern").regex().required()
    override val secretEnvPattern: Regex by customOption("repo-structure.secret-env-pattern").regex().required()
    override val globalEnvPattern: Regex by customOption("repo-structure.global-env-pattern").regex().required()
    override val projectNamePattern: Regex by customOption("repo-structure.project-name-pattern").regex().required()
    override val composeNamePattern: Regex by customOption("repo-structure.compose-name-pattern").regex().required()
}
