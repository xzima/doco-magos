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
package io.github.xzima.docomagos.server.cli.valuesources

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlMap
import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlScalar
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.options.Option
import com.github.ajalt.clikt.sources.ValueSource
import okio.FileSystem
import okio.Path.Companion.toPath

class YamlValueSource(
    private val path: String,
) : ValueSource {

    private val root: YamlNode by lazy {
        val source = FileSystem.Companion.SYSTEM.source(path.toPath())
        Yaml.Companion.default.decodeFromSource<YamlNode>(source)
    }

    override fun getValues(context: Context, option: Option): List<ValueSource.Invocation> {
        val key = option.valueSourceKey ?: return emptyList()
        var node: YamlNode? = root
        for (item in key.split(".")) {
            if (node !is YamlMap) return emptyList()
            node = node.get<YamlNode>(item)
        }
        val value = (node as? YamlScalar)?.content ?: return emptyList()
        return ValueSource.Invocation.just(value)
    }
}
