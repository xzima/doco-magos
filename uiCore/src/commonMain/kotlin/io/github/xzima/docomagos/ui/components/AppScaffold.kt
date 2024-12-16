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
