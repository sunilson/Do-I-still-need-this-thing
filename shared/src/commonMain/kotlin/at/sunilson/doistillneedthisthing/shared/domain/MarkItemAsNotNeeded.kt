package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import kotlinx.datetime.Clock

class MarkItemAsNotNeeded(private val database: Database) : UseCase<Long, Unit>() {
    override suspend fun run(params: Long) {
        database.databaseItemQueries.mark_as_not_needed(
            Clock.System.now().toEpochMilliseconds(),
            Clock.System.now().toEpochMilliseconds(),
            params
        )
    }
}