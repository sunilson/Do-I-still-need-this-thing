package at.sunilson.doistillneedthisthing.shared.domain.entities

data class Settings(
    val notificationsEnabled: Boolean = true,
    val randomSingleDecisionsPerDay: Int = 3,
    val decisionsPerWeekly: Int = 10
)
