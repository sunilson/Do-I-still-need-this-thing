package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetAllItems(private val database: Database) : FlowUseCase<Unit, List<Item>>() {
    override fun run(params: Unit): Flow<List<Item>> {
        TODO("Not yet implemented")
    }
}