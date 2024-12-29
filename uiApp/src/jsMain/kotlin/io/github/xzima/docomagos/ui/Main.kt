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
package io.github.xzima.docomagos.ui

import androidx.compose.ui.*
import androidx.compose.ui.window.*
import io.github.oshai.kotlinlogging.Level
import io.github.xzima.docomagos.client.createRsocketClient
import io.github.xzima.docomagos.logging.configureLogging
import io.ktor.client.plugins.logging.*
import kotlinx.browser.*
import org.jetbrains.skiko.wasm.onWasmReady

@OptIn(ExperimentalComposeUiApi::class)
suspend fun main() {
    val loggingLevel = Level.INFO
    val reqRespLogLevel = LogLevel.ALL

    configureLogging(loggingLevel)

    val client = createRsocketClient(window.location.host, reqRespLogLevel)
    initKoinModule(client)
    onWasmReady {
        CanvasBasedWindow(title = UiConstants.APP_NAME) {
            App()
        }
    }
}
