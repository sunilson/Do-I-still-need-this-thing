package at.sunilson.doistillneedthisthing.shared.data

import android.content.Context
import at.sunilson.doistillneedthisthing.Database
import com.squareup.sqldelight.android.AndroidSqliteDriver

lateinit var appContext: Context

actual fun createDatabase(): Database {
    val driver = AndroidSqliteDriver(Database.Schema, appContext, "atime.db")
    return Database(driver)
}