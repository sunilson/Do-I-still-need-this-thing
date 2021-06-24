package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase

class AddItem(private val database: Database) : UseCase<Item, Unit>() {
    override suspend fun run(params: Item) {
        database.databaseItemQueries.insert(
            params.name,
            params.imagePath,
            params.addedTimestamp,
            null,
            null,
            null,
            params.location
        )
    }
}