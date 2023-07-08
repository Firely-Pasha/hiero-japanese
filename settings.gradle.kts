pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Hiero"
include(":app-android")
include(":app-shared")
include(":datasource-database")
include(":datasource-preferences")
include(":data")
include(":domain")
include(":ui-compose")
include(":ui-compose-modal")
include(":ui-shared")
