package io.github.xzima.docomagos.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import io.github.xzima.docomagos.ui.components.AppScaffold
import org.koin.compose.KoinContext

@Composable
fun App() {
    KoinContext {
        MaterialTheme(colors = darkColors()) {
            AppScaffold()
        }
    }
}
