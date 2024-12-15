package io.github.xzima.docomagos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import app.softwork.routingcompose.HashRouter
import io.github.xzima.docomagos.ui.UiConstants

@Composable
fun AppScaffold() {
    val scaffoldState = rememberScaffoldState()

    HashRouter("") {
        Row {
            AppNavigationRail()
            Scaffold(
                scaffoldState = scaffoldState,
                contentWindowInsets = ScaffoldDefaults.contentWindowInsets,
                topBar = { AppTopBar() },
            ) { innerPadding ->
                Box(Modifier.padding(innerPadding).fillMaxSize()) {
                    Row(modifier = Modifier.padding(PaddingValues(UiConstants.DEFAULT_PADDING))) {
                        DCProjectsCardColumn(
                            modifier = Modifier.weight(1f),
                            defaultPadding = UiConstants.DEFAULT_PADDING,
                            snackbarHostState = scaffoldState.snackbarHostState,
                        )
                        Box(Modifier.weight(2f))
                    }
                }
            }
        }
    }
}

