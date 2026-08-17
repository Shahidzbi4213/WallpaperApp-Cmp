import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(projects.composeApp)
    implementation(compose.desktop.currentOs)
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
}

compose.desktop {
    application {
        mainClass = "com.google.wallpaperapp.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Screeny"
            packageVersion = "1.0.0"
            description = "Wallpapers for your desktop"
            vendor = "Screeny"

            // Generated from the Screeny brand gradient by AppIconRenderTest, so the icon
            // cannot drift from the in-app mark. Re-run that test to regenerate the source png.
            macOS {
                bundleID = "com.google.wallpaperapp.desktop"
                iconFile.set(project.file("src/main/resources/screeny.icns"))
            }
            windows {
                iconFile.set(project.file("src/main/resources/screeny.ico"))
                menuGroup = "Screeny"
            }
            linux {
                iconFile.set(project.file("src/main/resources/screeny.png"))
            }
        }
    }
}
