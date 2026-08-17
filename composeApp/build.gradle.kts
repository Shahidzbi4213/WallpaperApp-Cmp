@file:Suppress("DEPRECATION")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.koin.compiler)
}

kotlin {
    android {
        namespace = "com.google.wallpaperapp.composeapp"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        val desktopMain by getting
        val desktopTest by getting

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.okhttp)
            // Supplies Dispatchers.Main on the JVM; viewModelScope is unusable without it.
            implementation(libs.kotlinx.coroutines.swing)
        }

        desktopTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(compose.uiTooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            //Material Icons
            implementation(compose.materialIconsExtended)

            //ImageLoading
            implementation(libs.landscapist.coil3)
            implementation(libs.landscapist.animation)

            //Navigation
            implementation(libs.navigation.compose)
            implementation(libs.viewmodelNavigation3)


            //Pagination
            implementation(libs.paging.multiplatform.common)
            implementation(libs.paging.multiplatform.compose.common)

            //Koin
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.koin.di)
            api(libs.koin.annotations)

            //Ktor
            implementation(libs.bundles.ktor.networking)

            //Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.room.paging)


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }

        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

        // KSP Common sourceSet & Generated AppConfig
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
            kotlin.srcDir("build/generated/appconfig/commonMain/kotlin")
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

}

val generateAppConfig = tasks.register("generateAppConfig") {
    val localPropertiesFile = rootProject.file("local.properties")
    val outputDir = layout.buildDirectory.dir("generated/appconfig/commonMain/kotlin")
    outputs.dir(outputDir)
    inputs.file(localPropertiesFile).optional()

    doLast {
        val properties = Properties()
        if (localPropertiesFile.exists()) {
            FileInputStream(localPropertiesFile).use { properties.load(it) }
        }
        val pexelsApiKey = properties.getProperty("PEXELS_API_KEY")
            ?: (project.findProperty("PEXELS_API_KEY") as? String)
            ?: System.getenv("PEXELS_API_KEY")
            ?: ""
        val unsplashKey = properties.getProperty("UNSPLASH_ACCESS_KEY")
            ?: (project.findProperty("UNSPLASH_ACCESS_KEY") as? String)
            ?: System.getenv("UNSPLASH_ACCESS_KEY")
            ?: ""

        val configFile = outputDir.get().file("com/google/wallpaperapp/AppConfig.kt").asFile
        configFile.parentFile.mkdirs()
        configFile.writeText(
            """
            |package com.google.wallpaperapp
            |
            |object AppConfig {
            |    const val PEXELS_API_KEY: String = "$pexelsApiKey"
            |    const val UNSPLASH_ACCESS_KEY: String = "$unsplashKey"
            |}
            |
            """.trimMargin()
        )
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

// Trigger Common Metadata Generation from Native tasks & ensure AppConfig is generated
tasks.matching {
    it.name.startsWith("ksp") ||
    it.name.startsWith("compileKotlin") ||
    it.name.startsWith("transformCommonMain") ||
    it.name == "metadataCommonMainClasses"
}.configureEach {
    dependsOn(generateAppConfig)
}

tasks.matching { it.name.startsWith("ksp") && it.name != "kspCommonMainKotlinMetadata" }.configureEach {
    dependsOn("kspCommonMainKotlinMetadata")
}

room {
    schemaDirectory("$projectDir/schemas")
}

configure<org.koin.compiler.plugin.KoinGradleExtension> {
    // ponytail: platform DI still comes from expect/actual + DSL modules; re-enable once that is modeled with Koin annotations.
    compileSafety.set(false)
}
