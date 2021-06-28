package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.data.toItem
import at.sunilson.doistillneedthisthing.shared.domain.entities.Item
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.first
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random

/**
 * Get a random item that should be reviewed by the user or null if none should be reviewed at this time
 */
class GetRandomDecision(
    private val getSettings: GetSettings,
    private val database: Database
) : UseCase<Unit, Item?>() {
    override suspend fun run(params: Unit): Item? {

        // Check if its during the day, we dont want to show these notifications in the night
        val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        if (date.hour !in 6..22) {
            Napier.d("Return no random decision because it is not during the day! ${date.hour}")
            return null
        }

        // Get current day as date string and retrieve counter for the day
        val dateString = "${date.year}${date.month}${date.dayOfMonth}"
        val counter =
            database.dailyNotificationCounterQueries.get(dateString).executeAsOneOrNull()?.counter
                ?: 0

        // Check if we already shown the max amount of decisions per day
        val settings = getSettings(Unit).first()
        if (settings.randomSingleDecisionsPerDay <= counter) {
            Napier.d("Return no random decision because counter maxed out! $counter")
            return null
        }

        // Check if notifications are enabled
        if (!settings.notificationsEnabled) {
            Napier.d("Return no random decision because notifications not enabled!")
            return null
        }

        // Randomize if decision should be requested (1 in 4 chance)
        if(Random.nextInt(0, 4) != 3) return null

        // Get a random item and increase counter
        val items = database.databaseItemQueries.getNeeded().executeAsList().map { it.toItem() }
        if (items.isEmpty()) return null
        database.dailyNotificationCounterQueries.upsert(dateString)
        return items.random()
    }
}