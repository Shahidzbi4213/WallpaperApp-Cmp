@file:Suppress("DEPRECATION")

import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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

        // KSP Common sourceSet
        sourceSets.named("commonMain").configure {
            kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
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



dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

// Trigger Common Metadata Generation from Native tasks
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
