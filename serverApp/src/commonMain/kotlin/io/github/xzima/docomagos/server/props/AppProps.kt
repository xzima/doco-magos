/**
 * Copyright 2024-2025 Alex Zima(xzima@ro.ru)
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
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.int

interface AppProps {
    val hostname: String
    val staticUiPath: String
    val jobPeriodMs: Int
    val ignoreRepoExternalStacksSync: Boolean
}

class AppOptionGroup :
    OptionGroup(),
    AppProps {
    override val hostname: String by customOption("HOSTNAME").required()
    override val staticUiPath: String by customOption("app.static-ui-path").required()
    override val jobPeriodMs: Int by customOption("app.job-period-ms").int().required()
    override val ignoreRepoExternalStacksSync: Boolean by customOption("app.ignore-repo-external-stacks-sync")
        .boolean().required()
}
