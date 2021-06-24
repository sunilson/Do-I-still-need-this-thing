package at.sunilson.doistillneedthisthing.androidApp.presentation.items.notneeded

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.home.HomeViewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.ItemList
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.ItemsAppBar

@ExperimentalAnimationApi
@Composable
fun ItemsNotNeededList(
    viewModel: ItemsNotNeededListViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val state = viewModel.container.stateFlow.collectAsState(initial = null)
    val items = state.value?.items
    val scrollState = rememberLazyListState()

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (menuBar, itemList) = createRefs()
        ItemsAppBar(
            scrollState = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(menuBar) { top.linkTo(parent.top) },
            onMenuClick = { homeViewModel.menuButtonClicked() }
        )
        ItemList(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(itemList) {
                    height = Dimension.fillToConstraints
                    top.linkTo(menuBar.bottom)
                    bottom.linkTo(parent.bottom)
                },
            items = items,
            onItemNeeded = { viewModel.itemStillNeededClicked(it) },
            onItemNotNeeded = { /* Not implemented */ },
            onItemRemoved = { /* Not implemented */ },
            onItemDelete = { viewModel.itemRemovedClicked(it) },
            scrollState = scrollState
        )
    }
}