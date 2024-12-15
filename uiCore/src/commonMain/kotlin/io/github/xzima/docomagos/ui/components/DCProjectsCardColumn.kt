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
fun DCProjectsCardColumn(
    modifier: Modifier,
    defaultPadding: Dp,
    snackbarHostState: SnackbarHostState,
) {
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
