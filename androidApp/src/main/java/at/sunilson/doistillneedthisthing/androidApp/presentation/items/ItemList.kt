package at.sunilson.doistillneedthisthing.androidApp.presentation.items

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import at.sunilson.doistillneedthisthing.androidApp.presentation.shared.composables.EmptyPlaceholder
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item

@ExperimentalAnimationApi
@Composable
fun ItemList(
    modifier: Modifier = Modifier,
    items: List<Item>?,
    onItemNeeded: (Item) -> Unit,
    onItemNotNeeded: (Item) -> Unit,
    onItemRemoved: (Item) -> Unit,
    onItemDelete: (Item) -> Unit,
    scrollState: LazyListState
) {
    Crossfade(modifier = modifier, targetState = items) {
        when {
            it == null -> {
            }
            it.isEmpty() -> EmptyPlaceholder(modifier = Modifier.fillMaxSize())
            else -> LazyColumn(Modifier.fillMaxSize(), state = scrollState) {
                it.forEach { item ->
                    item {
                        ItemCard(
                            item,
                            onItemNeeded = { onItemNeeded(item) },
                            onItemNotNeeded = { onItemNotNeeded(item) },
                            onItemRemoved = { onItemRemoved(item) },
                            onItemDelete = { onItemDelete(item) },
                        )
                    }
                }
            }
        }
    }
}