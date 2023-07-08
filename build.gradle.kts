plugins {
    id("com.android.application") version space.compose.hiero.Versions.AndroidApplication apply false
    id("com.android.library") version space.compose.hiero.Versions.AndroidApplication apply false
    kotlin("android") version space.compose.hiero.Versions.Kotlin apply false
    kotlin("multiplatform") version space.compose.hiero.Versions.Kotlin apply false
    id("org.jetbrains.compose") version space.compose.hiero.Versions.JetbrainsCompose apply false
    id("app.cash.sqldelight") version space.compose.hiero.Versions.SqlDelight apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
