package space.compoze.hiero.datasource.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import space.compose.hiero.datasource.database.Database

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            Database.Schema,
            context,
            "hiero.db",
        )
    }
}