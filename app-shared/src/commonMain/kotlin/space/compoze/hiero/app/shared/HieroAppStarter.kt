package space.compoze.hiero.app.shared

import app.cash.sqldelight.db.SqlDriver
import org.koin.core.Koin
import org.koin.mp.KoinPlatformTools
import space.compose.hiero.datasource.database.Database

fun startHieroApp() = KoinPlatformTools.defaultContext().get().run {
    initDatabase()
}

fun Koin.initDatabase() {
    val sqlDriver: SqlDriver by inject()
    val database: Database by inject()
    Database.Schema.migrate(sqlDriver, 0, Database.Schema.version)
    println(database.collectionQueries.getByUuid("hiragana").executeAsOne())
}