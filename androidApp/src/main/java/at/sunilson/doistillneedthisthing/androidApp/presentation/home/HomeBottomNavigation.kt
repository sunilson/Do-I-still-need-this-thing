package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ThumbDown
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeBottomNavigation(selectedMenuItem: HomeMenuItem, viewModel: HomeViewModel = viewModel()) {
    BottomNavigation {
        BottomNavigationItem(
            selected = selectedMenuItem is HomeMenuItem.Needed,
            onClick = { viewModel.menuItemSelected(HomeMenuItem.Needed) },
            label = { Text("Brauche ich") },
            icon = { Icon(Icons.Rounded.ThumbUp, contentDescription = "") }
        )
        BottomNavigationItem(
            selected = selectedMenuItem is HomeMenuItem.NotNeeded,
            onClick = { viewModel.menuItemSelected(HomeMenuItem.NotNeeded) },
            label = { Text("Brauche ich nicht") },
            icon = { Icon(Icons.Rounded.ThumbDown, contentDescription = "") }
        )
        BottomNavigationItem(
            selected = selectedMenuItem is HomeMenuItem.Removed,
            onClick = { viewModel.menuItemSelected(HomeMenuItem.Removed) },
            label = { Text("Entsorgt") },
            icon = { Icon(Icons.Rounded.Delete, contentDescription = "") }
        )
    }
}