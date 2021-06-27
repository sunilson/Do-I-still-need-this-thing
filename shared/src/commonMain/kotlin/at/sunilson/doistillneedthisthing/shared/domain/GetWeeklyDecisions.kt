package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import kotlinx.datetime.Clock

/**
 * Returns a list of items that the user should decide if they still need it or not, but only if
 * the last decisions were done at least a week ago, otherwise returns null
 */
class GetWeeklyDecisions(private val database: Database) : UseCase<Unit, List<Item>?>() {
    override suspend fun run(params: Unit): List<Item>? {
        val now = Clock.System.now().toEpochMilliseconds()
       val lastTimestamp = database.weeklyNotificationsQueries.getLatest().executeAsOneOrNull() ?: 0L
        TODO()
    }
}