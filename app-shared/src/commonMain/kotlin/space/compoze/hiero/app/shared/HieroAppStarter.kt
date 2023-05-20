package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.Koin
import org.koin.mp.KoinPlatformTools
import space.compose.hiero.datasource.database.Database
import space.compoze.hiero.domain.application.interactor.ApplicationInitUseCase
import space.compoze.hiero.domain.collectionitem.interactor.notification.CollectionItemNotificationGetFlowUseCase

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
    val applicationInitUseCase: ApplicationInitUseCase by inject()
    applicationInitUseCase().getOrNone()
}