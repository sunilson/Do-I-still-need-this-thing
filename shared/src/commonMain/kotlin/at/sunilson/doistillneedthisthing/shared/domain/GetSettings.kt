package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Settings
import at.sunilson.doistillneedthisthing.shared.domain.shared.FlowUseCase
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetSettings(private val database: Database) : FlowUseCase<Unit, Settings>() {
    override fun run(params: Unit): Flow<Settings> {
        return database
            .databaseSettingsQueries
            .get()
            .asFlow()
            .mapToOneOrNull(dispatcher)
            .map { databaseSettings ->
                if (databaseSettings == null) {
                    Settings()
                } else {
                    Settings(
                        databaseSettings.notifications_enabled == 1L,
                        databaseSettings.random_single_decisions_per_day.toInt(),
                        databaseSettings.decisions_per_weekly.toInt()
                    )
                }
            }
    }
}