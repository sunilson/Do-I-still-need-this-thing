package at.sunilson.doistillneedthisthing.androidApp.presentation.items.notneeded

import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsNotNeeded
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsNeeded
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsRemoved
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import com.github.michaelbull.result.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

data class ItemsNotNeededListState(val items: List<Item> = listOf())
sealed class ItemsNotNeededListSideEffects

@HiltViewModel
class ItemsNotNeededListViewModel @Inject constructor(
    private val getItemsNotNeeded: GetItemsNotNeeded,
    private val markItemAsNeeded: MarkItemAsNeeded,
    private val markItemAsRemoved: MarkItemAsRemoved
) : ViewModel(),
    ContainerHost<ItemsNotNeededListState, ItemsNotNeededListSideEffects> {

    override val container =
        container<ItemsNotNeededListState, ItemsNotNeededListSideEffects>(ItemsNotNeededListState())

    init {
        intent {
            getItemsNotNeeded(Unit).collect {
                reduce { state.copy(items = it) }
            }
        }
    }

    fun itemRemovedClicked(item: Item) = intent {
        markItemAsRemoved(item.id).fold(
            { Timber.d("Marked $item as needed!") },
            { Timber.e(it, "Could not mark $item as needed!") }
        )
    }

    fun itemStillNeededClicked(item: Item) = intent {
        markItemAsNeeded(item.id).fold(
            { Timber.d("Marked $item as removed!") },
            { Timber.e(it, "Could not mark $item as removed!") }
        )
    }
}