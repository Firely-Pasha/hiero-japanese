package space.compoze.hiero.datasource.database

import app.cash.sqldelight.db.SqlDriver
import space.compose.hiero.datasource.database.Database

expect class DriverFactory {
    fun createDriver(): SqlDriver
}