package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import kotlinx.datetime.Clock

class MarkItemAsNotNeeded(private val database: Database) : UseCase<Item, Unit>() {
    override suspend fun run(params: Item) {
        database.databaseItemQueries.upsert(
            params.id,
            params.name,
            params.imagePath,
            params.addedTimestamp,
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds(),
            null,
            params.location
        )
    }
}