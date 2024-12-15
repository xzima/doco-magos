package io.github.xzima.docomagos.ui.components

import androidx.compose.material.*
import androidx.compose.runtime.*
import io.github.xzima.docomagos.ui.UiConstants

@Composable
fun AppTopBar() {
    TopAppBar(
        title = { Text(UiConstants.APP_NAME) },
    )
}
