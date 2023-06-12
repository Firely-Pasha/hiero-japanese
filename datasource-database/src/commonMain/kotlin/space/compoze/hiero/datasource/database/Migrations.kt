package space.compoze.hiero.datasource.database

import app.cash.sqldelight.db.SqlDriver
import space.compose.hiero.datasource.database.HieroDb

fun applyMigrationIfNeeded(driver: SqlDriver) {
    val currentVer = getVersion(driver)
    println("Current db version: $currentVer")
    if (currentVer == 0) {
        println("Prepare to init db")
        HieroDb.Schema.create(driver)
        setVersion(driver, HieroDb.Schema.version)
        println("Created tables, set version to ${HieroDb.Schema.version}")
    } else {
        val schemaVer: Int = HieroDb.Schema.version
        if (schemaVer > currentVer) {
            println("Prepare to migrate from $currentVer to $schemaVer")
            HieroDb.Schema.migrate(driver, currentVer, schemaVer)
            setVersion(driver, schemaVer)
            println("Migrated from $currentVer to $schemaVer")
        } else {
            println("No migration needed. ")
        }
    }

}

private fun getVersion(driver: SqlDriver): Int {
    val sqlCursor = driver.executeQuery(null, "PRAGMA user_version;", {
        if (it.next()) {
            it.getLong(0)
        } else {
            0
        }
    }, 0);
    return sqlCursor.value?.toInt() ?: 0
}

private fun setVersion(driver: SqlDriver, version: Int) {
    driver.execute(null, "PRAGMA user_version = $version;", 0, null);
}
