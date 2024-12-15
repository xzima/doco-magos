package io.github.xzima.docomagos.ui

import androidx.compose.ui.*
import androidx.compose.ui.window.*
import io.github.xzima.docomagos.client.createRsocketClient
import kotlinx.browser.*
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
suspend fun main() {
    val client = createRsocketClient(window.location.host)
    initKoinModule(client)
    onWasmReady {
        CanvasBasedWindow(title = UiConstants.APP_NAME) {
            App()
        }
    }
}
