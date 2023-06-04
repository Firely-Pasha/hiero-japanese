import space.compose.hiero.Dependencies

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
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

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependencies.SqlDelight.Drivers.Android)
            }
        }
        val androidUnitTest by getting

        val nativeMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation(Dependencies.SqlDelight.Drivers.Native)
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
    namespace = "space.compoze.hiero.datasource.database"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set("space.compose.hiero.datasource.database")
            deriveSchemaFromMigrations.set(true)
            verifyMigrations.set(true)
            linkSqlite.set(true)
        }
    }
}