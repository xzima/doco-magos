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
package io.github.xzima.docomagos.docker.ktor.engine.socket

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.engines
import io.ktor.util.InternalAPI

@OptIn(InternalAPI::class)
object SocketCIO : HttpClientEngineFactory<SocketCIOEngineConfig> {
    init {
        engines.append(SocketCIO)
    }

    override fun create(block: SocketCIOEngineConfig.() -> Unit): HttpClientEngine =
        SocketCIOEngine(SocketCIOEngineConfig().apply(block))

    override fun toString(): String = "SocketCIO"
}
