package at.sunilson.doistillneedthisthing.androidApp.presentation.items.deleted

import androidx.lifecycle.ViewModel
import at.sunilson.doistillneedthisthing.shared.domain.GetItemsDeleted
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

data class DeletedItemsListState(val items: List<Item> = listOf())
sealed class DeletedItemsListSideEffects

@HiltViewModel
class ItemsDeletedListViewModel @Inject constructor(
    private val getItemsDeleted: GetItemsDeleted
) : ViewModel(),
    ContainerHost<DeletedItemsListState, DeletedItemsListSideEffects> {

    override val container =
        container<DeletedItemsListState, DeletedItemsListSideEffects>(DeletedItemsListState())

    init {
        intent {
            getItemsDeleted(Unit).collect {
                reduce { state.copy(items = it) }
            }
        }
    }
}