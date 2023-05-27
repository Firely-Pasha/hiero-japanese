package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compoze.hiero.datasource.database.DriverFactory
import space.compoze.hiero.domain.base.AppDispatchers

internal actual fun appPlatformModule() = module {
    single { DriverFactory().createDriver() } bind SqlDriver::class
}