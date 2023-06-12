package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.Koin
import org.koin.mp.KoinPlatformTools
import space.compoze.hiero.datasource.database.applyMigrationIfNeeded
import space.compoze.hiero.domain.application.interactor.ApplicationInit

fun startHieroApp() = KoinPlatformTools.defaultContext().get().run {
    initDatabase()
    initDomain()
}

fun Koin.initDatabase() {
    val driver: SqlDriver by inject()
    applyMigrationIfNeeded(driver)
}

fun Koin.initDomain() {
    val applicationInit: ApplicationInit by inject()
    applicationInit().getOrNone()
}