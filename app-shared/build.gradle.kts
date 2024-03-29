    import space.compose.hiero.Dependencies
import space.compose.hiero.Modules

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("kotlin-parcelize")
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
            baseName = "HieroApp"

            export(project(Modules.Datasource.Database))
            export(project(Modules.Ui.Shared))
            export(Dependencies.Koin.Core)
            export(Dependencies.Decompose.Core)
            export(Dependencies.Essenty.Lifecycle)
            export(Dependencies.MviKotlin.Main)
            linkerOpts.add("-lsqlite3")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(Modules.Datasource.Database))
                api(project(Modules.Datasource.Preferences))
                api(project(Modules.Ui.Shared))
                implementation(project(Modules.Data))
                implementation(project(Modules.Domain))

                implementation(Dependencies.KotlinX.Coroutines.Core)
                implementation(Dependencies.Arrow.Core)
                implementation(Dependencies.Koin.Core)
                implementation(Dependencies.Decompose.Core)
                implementation(Dependencies.MultiplatformSettings.Core)
                implementation(Dependencies.MultiplatformSettings.Coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting
        val androidUnitTest by getting

        val nativeMain by creating {
            dependsOn(commonMain)
            dependencies {

            }
        }
        val nativeTest by creating

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(nativeMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(nativeTest)
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
