package at.sunilson.doistillneedthisthing.shared.domain

import at.sunilson.doistillneedthisthing.Database
import at.sunilson.doistillneedthisthing.shared.domain.entities.Settings
import at.sunilson.doistillneedthisthing.shared.domain.shared.UseCase

class UpdateSettings(private val database: Database) : UseCase<Settings, Unit>() {
    override suspend fun run(params: Settings) {
        database.databaseSettingsQueries.insert(if (params.notificationsEnabled) 1 else 0)
    }
}