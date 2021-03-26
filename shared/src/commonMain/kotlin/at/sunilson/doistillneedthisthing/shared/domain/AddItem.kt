package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.domain.shared.UseCase

class AddItem(private val database: Database) : UseCase<Item, Unit>() {
    override suspend fun run(params: Item) {
        TODO("Not yet implemented")
    }
}