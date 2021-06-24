package at.sunilson.doistillneedthisthing.androidApp.presentation.items.needed

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.home.HomeViewModel
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.ItemsAppBar
import at.sunilson.doistillneedthisthing.androidApp.presentation.items.ItemList

@ExperimentalAnimationApi
@Composable
@Preview(showSystemUi = true)
fun ItemsList(
    viewModel: NeededItemsListViewModel = viewModel(),
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
            onItemNeeded = { /* Not implemented */ },
            onItemNotNeeded = { viewModel.itemNotNeededClicked(it) },
            onItemRemoved = { /* Not implemented */ },
            onItemDelete = { viewModel.itemRemovedClicked(it) },
            scrollState = scrollState
        )
    }
}