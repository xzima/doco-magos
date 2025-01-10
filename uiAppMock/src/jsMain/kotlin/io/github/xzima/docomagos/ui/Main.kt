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
package io.github.xzima.docomagos.ui

import androidx.compose.ui.*
import androidx.compose.ui.window.*
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.api.ListProjectsResp
import io.github.xzima.docomagos.client.DockerComposeApiService
import io.github.xzima.docomagos.koin.configureKoin
import io.github.xzima.docomagos.logging.configureLogging
import io.github.xzima.docomagos.ui.states.DCProjectsListViewModel
import kotlinx.coroutines.*
import org.jetbrains.skiko.wasm.onWasmReady
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    KotlinLogging.configureLogging(Level.TRACE)
    configureKoin {
        single<DockerComposeApiService> {
            object : DockerComposeApiService {
                override suspend fun listProjects(): ListProjectsResp {
                    delay(2.seconds)
                    return ListProjectsResp(
                        listOf(
                            ListProjectsResp.ProjectItem(name = "first-compose", status = "running"),
                            ListProjectsResp.ProjectItem(name = "second-compose", status = "stopped"),
                        ),
                    )
                }
            }
        }
        factory { DCProjectsListViewModel(get()) }
    }
    onWasmReady {
        CanvasBasedWindow(title = UiConstants.APP_NAME) {
            App()
        }
    }
}
