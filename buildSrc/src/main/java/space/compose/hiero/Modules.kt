package space.compose.hiero

object Modules {
    object Datasource {
        const val Database = ":datasource-database"
    }
    const val Data = ":data"
    const val Domain = ":domain"
    object Ui {
        const val Compose = ":ui-compose"
        const val Shared = ":ui-shared"
    }
    object App {
        const val Shared = ":app-shared"
    }
}