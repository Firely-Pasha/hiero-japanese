package space.compoze.hiero.datasource.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import space.compose.hiero.datasource.database.HieroDb

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(HieroDb.Schema, "hiero.db")
    }
}