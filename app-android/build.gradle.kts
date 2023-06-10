import space.compose.hiero.Dependencies
import space.compose.hiero.Modules

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "space.compoze.hiero.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "space.compoze.hiero.android"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(Modules.Ui.Compose))
    implementation(project(Modules.Ui.Shared))
    implementation(project(Modules.App.Shared))

    implementation(Dependencies.KotlinX.Coroutines.Core)
    implementation(Dependencies.Koin.Core)
    implementation(Dependencies.Koin.Android)
    implementation(Dependencies.Koin.AndroidCompat)
    implementation(Dependencies.Decompose.Core)
    implementation(Dependencies.MviKotlin.Core)
    implementation(Dependencies.MviKotlin.Main)
    implementation(Dependencies.MviKotlin.Extensions.Coroutines)
    implementation(Dependencies.AndroidX.Compose.Ui)
    implementation(Dependencies.AndroidX.Compose.Tooling)
    implementation(Dependencies.AndroidX.Compose.Preview)
    implementation(Dependencies.AndroidX.Compose.Foundation)
    implementation(Dependencies.AndroidX.Compose.Material3)
    implementation(Dependencies.AndroidX.Activity.Compose)
    implementation(Dependencies.AndroidX.Navigation.Compose)
}