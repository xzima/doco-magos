/**
 * Copyright 2024 Alex Zima(xzima@ro.ru)
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
package io.github.xzima.docomagos.server.ext.ktor

import io.github.xzima.docomagos.koin.inject
import io.github.xzima.docomagos.server.env.KtorEnv
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun customEmbeddedServer(module: Application.() -> Unit): CIOApplicationEngine {
    val ktorEnv = inject<KtorEnv>()

    val configure: CIOApplicationEngine.Configuration.() -> Unit = {
        reuseAddress = ktorEnv.reuseAddress
    }
    val environment = applicationEngineEnvironment {
        log = ktorLogger("ktor.application")
        connector {
            port = ktorEnv.port
        }
        this.module(module)
    }
    return embeddedServer(CIO, environment = environment, configure = configure)
}
