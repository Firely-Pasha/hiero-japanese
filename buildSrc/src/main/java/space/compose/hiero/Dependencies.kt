package space.compose.hiero

object Dependencies {

    object AndroidX {
        object Compose {
            const val Ui = "androidx.compose.ui:ui:${Versions.AndroidCompose}"
            const val Tooling = "androidx.compose.ui:ui-tooling:${Versions.AndroidCompose}"
            const val Preview = "androidx.compose.ui:ui-tooling-preview:${Versions.AndroidCompose}"
            const val Foundation = "androidx.compose.foundation:foundation:${Versions.AndroidCompose}"
            const val Material = "androidx.compose.material:material:${Versions.AndroidCompose}"
            const val Material3 = "androidx.compose.material3:material3:${Versions.AndroidComposeMaterial3}"
        }
        object Activity {
            const val Compose = "androidx.activity:activity-compose:${Versions.AndroidActivityCompose}"
        }
        object Navigation {
            const val Compose = "androidx.navigation:navigation-compose:${Versions.AndroidNavigation}"
        }
    }

    object KotlinX {
        object Coroutines {
            const val Core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Coroutines}"
        }
    }

    object Decompose {
        const val Core = "com.arkivanov.decompose:decompose:${Versions.Decompose}"

        object Extensions {
            object Compose {
                const val JetBrains =
                    "com.arkivanov.decompose:extensions-compose-jetbrains:${Versions.Decompose}"
            }
        }
    }

    object MviKotlin {
        const val Core = "com.arkivanov.mvikotlin:mvikotlin:${Versions.MviKotlin}"
        const val Main = "com.arkivanov.mvikotlin:mvikotlin-main:${Versions.MviKotlin}"

        object Extensions {
            const val Coroutines =
                "com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:${Versions.MviKotlin}"
        }
    }

    object Essenty {
        const val Lifecycle = "com.arkivanov.essenty:lifecycle:${Versions.Essenty}"
    }

    object SqlDelight {
        object Drivers {
            const val Android = "app.cash.sqldelight:android-driver:${Versions.SqlDelight}"
            const val Native = "app.cash.sqldelight:native-driver:${Versions.SqlDelight}"
        }
        object Extensions {
            const val Coroutines = "app.cash.sqldelight:coroutines-extensions:${Versions.SqlDelight}"
        }
    }

    object Koin {
        const val Core = "io.insert-koin:koin-core:${Versions.Koin}"
        const val Android = "io.insert-koin:koin-android:${Versions.Koin}"
        const val AndroidCompat = "io.insert-koin:koin-android-compat:${Versions.Koin}"
    }

    object Arrow {
        const val Core = "io.arrow-kt:arrow-core:${Versions.Arrow}"
        object FX {
            const val Coroutines = "io.arrow-kt:arrow-fx-coroutines:${Versions.Arrow}"
        }
    }

    object MultiplatformSettings {
        const val Core = "com.russhwolf:multiplatform-settings:${Versions.MultiplatformSettings}"
        const val Coroutines = "com.russhwolf:multiplatform-settings-coroutines:${Versions.MultiplatformSettings}"
    }
}