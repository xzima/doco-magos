package io.github.xzima.docomagos.ui

import androidx.compose.ui.*
import androidx.compose.ui.window.*
import io.github.xzima.docomagos.api.ListProjectsResp
import io.github.xzima.docomagos.client.DockerComposeApiService
import io.github.xzima.docomagos.ui.states.DCProjectsListViewModel
import kotlinx.coroutines.*
import org.jetbrains.skiko.wasm.onWasmReady
import org.koin.core.context.startKoin
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
//    logger(//TODO)
        modules(
            module {
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
            },
        )
    }
    onWasmReady {
        CanvasBasedWindow(title = UiConstants.APP_NAME) {
            App()
        }
    }
}
