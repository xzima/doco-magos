package io.github.xzima.docomagos.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import app.softwork.routingcompose.Router

@Composable
fun AppNavigationRail() {
    val router = Router.current
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Home", "Search", "Settings")
    val icons = listOf(Icons.Filled.Home, Icons.Filled.Search, Icons.Filled.Settings)
    NavigationRail(windowInsets = NavigationRailDefaults.windowInsets) {
        items.forEachIndexed { index, item ->
            NavigationRailItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    if (selectedItem == 0) router.navigate("")
                },
            )
        }
    }
}
