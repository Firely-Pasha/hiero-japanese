package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.Koin
import org.koin.mp.KoinPlatformTools
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.application.interactor.ApplicationInit

fun startHieroApp() = KoinPlatformTools.defaultContext().get().run {
    initDatabase()
    initDomain()
}

fun Koin.initDatabase() {
    val sqlDriver: SqlDriver by inject()
    val database: Database by inject()
    Database.Schema.migrate(sqlDriver, 0, Database.Schema.version)
}

fun Koin.initDomain() {
    val applicationInit: ApplicationInit by inject()
    applicationInit().getOrNone()
}