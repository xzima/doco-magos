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
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.boolean
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.long

interface KtorProps {
    val port: Int
    val reuseAddress: Boolean
    val gracePeriodMillis: Long
    val graceTimeoutMillis: Long
}

class KtorOptionGroup :
    OptionGroup(),
    KtorProps {
    override val port: Int by option(valueSourceKey = "ktor.port").int().required()
    override val reuseAddress: Boolean by option(valueSourceKey = "ktor.reuse-address").boolean().required()
    override val gracePeriodMillis: Long by option(valueSourceKey = "ktor.grace-period-millis").long().required()
    override val graceTimeoutMillis: Long by option(valueSourceKey = "ktor.grace-timeout-millis").long().required()
}
