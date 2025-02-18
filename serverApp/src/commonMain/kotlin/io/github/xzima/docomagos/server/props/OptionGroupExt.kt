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

import com.github.ajalt.clikt.completion.CompletionCandidates
import com.github.ajalt.clikt.core.ParameterHolder
import com.github.ajalt.clikt.parameters.arguments.ProcessedArgument
import com.github.ajalt.clikt.parameters.arguments.RawArgument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.options.NullableOption
import com.github.ajalt.clikt.parameters.options.RawOption
import com.github.ajalt.clikt.parameters.options.convert
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.transform.TransformContext

fun ParameterHolder.customOption(
    name: String,
    help: String = "",
    metavar: String? = null,
    hidden: Boolean = false,
    envvar: String? = null,
    helpTags: Map<String, String> = emptyMap(),
    completionCandidates: CompletionCandidates? = null,
    eager: Boolean = false,
): RawOption = this.option(
    "--" + name.replace(".", "-"),
    help = help,
    metavar = metavar,
    hidden = hidden,
    envvar = envvar,
    helpTags = helpTags,
    completionCandidates = completionCandidates,
    valueSourceKey = name,
    eager = eager,
)

private val regexConversion: TransformContext.(String) -> Regex = {
    runCatching { Regex(it) }.getOrNull() ?: fail("A regex string is required")
}

fun RawArgument.regex(): ProcessedArgument<Regex, Regex> = convert(conversion = regexConversion)

fun RawOption.regex(): NullableOption<Regex, Regex> = convert("regex", conversion = regexConversion)
