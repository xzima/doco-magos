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

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import io.github.xzima.docomagos.ui.states.DCProjectsListViewModel
import kotlinx.coroutines.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DCProjectsCardColumn(defaultPadding: Dp, snackbarHostState: SnackbarHostState, modifier: Modifier = Modifier) {
    val coroutineScope = rememberCoroutineScope()
    val projectsViewModel = koinViewModel<DCProjectsListViewModel>()
    val projectsList by projectsViewModel
        .projectsList
        .collectAsState(initial = emptyList())

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(defaultPadding),
    ) {
        items(projectsList) { item ->
            val cardModifier = Modifier.clickable {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Snackbar: ${item.name}")
                }
            }
            Card(cardModifier) {
                Column(Modifier.padding(2 * defaultPadding).fillMaxSize()) {
                    Text(item.name, Modifier.align(Alignment.CenterHorizontally))
                    Text("status: ${item.status}")
                }
            }
        }
    }
}
