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
import io.github.xzima.docomagos.server.env.AppEnv
import io.github.xzima.docomagos.server.env.KtorEnv
import io.github.xzima.docomagos.server.env.RSocketEnv
import io.github.xzima.docomagos.server.handlers.ListProjectsHandler
import io.github.xzima.docomagos.server.services.DockerComposeService
import io.github.xzima.docomagos.server.services.StaticUiService
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initTestKoinModule(port: Int) = startKoin {
//    logger(//TODO)
    modules(
        module {
            single {
                RSocketEnv(
                    1024,
                )
            }
            single {
                KtorEnv(
                    port = port,
                    reuseAddress = true,
                    gracePeriodMillis = 0,
                    graceTimeoutMillis = 0,
                )
            }
            single {
                AppEnv(
                    "/static/ui/path",
                )
            }
            single { StaticUiService(get()) }
            single { DockerComposeService() }
            // handlers
            single { ListProjectsHandler(get()) }
        },
    )
}
