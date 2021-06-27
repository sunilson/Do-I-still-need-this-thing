package at.sunilson.doistillneedthisthing.androidApp.presentation.items.needed

import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsStillNeeded
import at.sunilson.doistillneedthisthing.shared.domain.MarkItemAsNotNeeded
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

data class NeededItemsListState(val items: List<Item> = listOf())
sealed class NeededItemsListSideEffects

@HiltViewModel
class NeededItemsListViewModel @Inject constructor(
    private val getItemsStillNeeded: GetItemsStillNeeded,
    private val markItemAsNotNeeded: MarkItemAsNotNeeded,
    private val markItemAsRemoved: MarkItemAsRemoved
) : ViewModel(),
    ContainerHost<NeededItemsListState, NeededItemsListSideEffects> {

    override val container = container<NeededItemsListState, NeededItemsListSideEffects>(
        NeededItemsListState()
    )

    init {
        intent {
            getItemsStillNeeded(Unit).collect {
                reduce { state.copy(items = it) }
            }
        }
    }

    fun itemNotNeededClicked(item: Item) = intent {
        markItemAsNotNeeded(item.id).fold(
            { Timber.d("Marked $item as not needed!") },
            { Timber.e(it, "Could not mark $item as not needed!") }
        )
    }

    fun itemRemovedClicked(item: Item) = intent {
        markItemAsRemoved(item.id).fold(
            { Timber.d("Marked $item as removed!") },
            { Timber.e(it, "Could not mark $item as removed!") }
        )
    }
}