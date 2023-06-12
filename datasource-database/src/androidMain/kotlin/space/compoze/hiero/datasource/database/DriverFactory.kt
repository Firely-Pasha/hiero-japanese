package space.compoze.hiero.datasource.database

import android.content.Context
import app.cash.sqldelight.db.AfterVersion
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import space.compose.hiero.datasource.database.HieroDb

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            HieroDb.Schema,
            context,
            "hiero.db",
            callback = AndroidSqliteDriver.Callback(
                schema = HieroDb.Schema,
            )
        )
    }
}