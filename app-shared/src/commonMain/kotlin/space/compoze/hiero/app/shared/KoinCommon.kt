package space.compoze.hiero.app.shared

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.mp.KoinPlatformTools
import space.compose.hiero.datasource.database.Database

internal expect fun appPlatformModule(): Module

fun appModule() = appPlatformModule() + module {
    single { Database(get()) }
}