package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compoze.hiero.datasource.database.DriverFactory

internal actual fun appPlatformModule() = module {
    single { DriverFactory(get()).createDriver() } bind SqlDriver::class
}