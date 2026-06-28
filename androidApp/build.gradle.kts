plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.google.wallpaperapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.google.wallpaperapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        androidResources.localeFilters += mutableSetOf(
            "en", "ar", "ru", "in", "bn", "hi", "uk",
            "vi", "ko", "ja", "zh", "sv", "pl", "ms", "fr",
            "it", "fa", "tr", "th", "pt", "es", "de", "nl", "ta", "cs", "ur"
        )
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }



    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(platform(libs.koin.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
}
