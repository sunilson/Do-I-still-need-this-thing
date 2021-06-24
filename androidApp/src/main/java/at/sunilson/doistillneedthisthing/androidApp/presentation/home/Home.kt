package at.sunilson.doistillneedthisthing.androidApp.presentation.home

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.needed.ItemsList
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.notneeded.ItemsNotNeededList

@ExperimentalAnimationApi
@Composable
fun Home(viewModel: HomeViewModel = viewModel()) {
    val state = viewModel.container.stateFlow.collectAsState(initial = HomeState())
    val selectedMenuItem = state.value.menuItem

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.addItemClicked() }) {
                Icon(Icons.Rounded.Add, contentDescription = "Add item button")
            }
        },
        bottomBar = { HomeBottomNavigation(selectedMenuItem) }
    ) {
        Crossfade(targetState = selectedMenuItem) {
            when (it) {
                HomeMenuItem.Needed -> ItemsList()
                HomeMenuItem.NotNeeded -> ItemsNotNeededList()
                HomeMenuItem.Removed -> Text("TODO")
            }
        }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun HomePreview() {
    Home(HomeViewModel())
}