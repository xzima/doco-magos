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
package io.github.xzima.docomagos.server

import io.github.xzima.docomagos.koin.configureKoin
import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.env.EnvUtils
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.StaticUiService

fun initKoinModule() = configureKoin {
    single {
        RSocketEnv(
            EnvUtils.getEnvVar("RSOCKET_MAX_FRAGMENT_SIZE") { it.toInt() },
        )
    }
    single {
        KtorEnv(
            EnvUtils.getEnvVar("KTOR_PORT") { it.toInt() },
            EnvUtils.getEnvVar("KTOR_REUSE_ADDRESS") { it.toBoolean() },
            EnvUtils.getEnvVar("KTOR_GRACE_PERIOD_MILLIS") { it.toLong() },
            EnvUtils.getEnvVar("KTOR_GRACE_TIMEOUT_MILLIS") { it.toLong() },
        )
    }
    single {
        AppEnv(
            EnvUtils.getEnvVar("STATIC_UI_PATH"),
        )
    }
    single { StaticUiService(get()) }
    single { DockerComposeService() }
    // handlers
    single { ListProjectsHandler(get()) }
}
