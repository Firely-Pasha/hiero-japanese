package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import org.koin.dsl.bind
import org.koin.dsl.module
import space.compoze.hiero.datasource.database.DriverFactory
import space.compoze.hiero.datasource.settings.SettingsFactory

internal actual fun appPlatformModule() = module {
    single { DriverFactory(get()).createDriver() } bind SqlDriver::class
    single { SettingsFactory(get()).create() } bind FlowSettings::class
}