package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.data.toItem
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.FlowUseCase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetItemsDeleted(private val database: Database) : FlowUseCase<Unit, List<Item>>() {
    override fun run(params: Unit): Flow<List<Item>> {
        return database
            .databaseItemQueries
            .getAll()
            .asFlow()
            .mapToList(dispatcher)
            .map { dbItems -> dbItems.map { dbItem -> dbItem.toItem() } }
            .map { items -> items.filter { item -> item.state == Item.State.REMOVED } }
    }
}