import org.jetbrains.compose.ExperimentalComposeLibrary
import space.compose.hiero.Dependencies
import space.compose.hiero.Modules

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            export("com.arkivanov.decompose:decompose:2.0.0-alpha-02")
//                export("com.arkivanov.essenty:lifecycle:<essenty_version>")

            // Optional, only if you need state preservation on Darwin (Apple) targets
//                export("com.arkivanov.essenty:state-keeper:<essenty_version>")

            // Optional, only if you need state preservation on Darwin (Apple) targets
//                export("com.arkivanov.parcelize.darwin:runtime:<parcelize_darwin_version>")

        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(Modules.Ui.Shared))
                implementation(project(Modules.Domain))

                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation(Dependencies.Arrow.Core)
                implementation(Dependencies.Decompose.Decompose)
                implementation(Dependencies.Decompose.Extensions.Compose.JetBrains)
                implementation(Dependencies.MviKotlin.MviKotlin)
                implementation(Dependencies.MviKotlin.Main)
                implementation(Dependencies.MviKotlin.Extensions.Coroutines)
                implementation(Dependencies.KotlinX.Coroutines.Core)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }

}

android {
    namespace = "space.compoze.hiero"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}