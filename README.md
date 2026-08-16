# Screeny

A Kotlin Multiplatform wallpaper app for **Android** and **iOS**, built with Compose Multiplatform. Browse, search, and favourite high-resolution wallpapers, then download them or set them straight as your device wallpaper. Wallpapers are sourced from the [Pexels API](https://www.pexels.com/api/).

<p align="center">
  <img src="screenshots/home.png" width="200" />
  <img src="screenshots/detail.png" width="200" />
</p>

## Features

- **Curated feed** — endlessly paged wallpaper feed with smooth image loading.
- **Categories** — browse wallpapers grouped by category.
- **Search** — find wallpapers by keyword.
- **Favourites** — save wallpapers offline; stored locally with Room.
- **Detail view** — preview full-resolution wallpapers, download them, or apply as home/lock-screen wallpaper.
- **Settings & languages** — in-app settings and language selection.
- **Dark-first glassmorphic UI** — shared Compose UI across both platforms.

## Tech Stack

| Concern | Library |
|---|---|
| UI | Compose Multiplatform |
| Language | Kotlin 2.4 (AGP 9.1) |
| DI | Koin (with Koin Annotations) |
| Networking | Ktor 3.5 (OkHttp on Android, Darwin on iOS) |
| Local storage | Room 2.8 (KMP) |
| Paging | Paging 3 / `app.cash.paging` (multiplatform) |
| Navigation | Navigation 3 |
| Image loading | Landscapist + Coil 3 |

## Project Structure

- **[/composeApp](./composeApp/src)** — shared Compose Multiplatform code.
  - **[commonMain](./composeApp/src/commonMain/kotlin)** — shared UI, DI, data, and domain layers (`ui`, `di`, `core`, `data`, `domain`, `utils`).
  - **[androidMain](./composeApp/src/androidMain/kotlin)** — Android-specific implementations (e.g. wallpaper apply/download).
  - **[iosMain](./composeApp/src/iosMain/kotlin)** — iOS-specific implementations.
- **[/iosApp](./iosApp/iosApp)** — iOS entry point (Xcode project + any SwiftUI code).

## Getting Started

The app calls the Pexels API. The repo ships with a demo API key in
[`NetworkModule.kt`](./composeApp/src/commonMain/kotlin/com/google/wallpaperapp/di/NetworkModule.kt);
replace it with your own free key from the [Pexels API dashboard](https://www.pexels.com/api/) for your own builds.

### Build and Run — Android

Use the run configuration in your IDE's run widget, or build from the terminal:

The APK is produced by `:androidApp`, not `:composeApp`:

- macOS/Linux
  ```shell
  ./gradlew :androidApp:assembleDebug
  ```
- Windows
  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

### Build and Run — iOS

Use the run configuration in your IDE's run widget, or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Build and Run — Desktop (macOS, Windows, Linux)

```shell
./gradlew :desktopApp:run
```

The desktop app is not a scaled-up phone layout — it has its own screens built to desktop
conventions (fixed sidebar, persistent search field, hover and right-click actions, keyboard
shortcuts, resizable master–detail preview) and it requests landscape imagery instead of portrait.
Browsing uses numbered pages — 40 to a page, with a page bar and a jump box — rather than the
phone's infinite scroll, so you always know where you are and can get back to it.

Installers:

```shell
./gradlew :desktopApp:packageDmg   # macOS
./gradlew :desktopApp:packageMsi   # Windows
./gradlew :desktopApp:packageDeb   # Linux
```

`jpackage` cannot cross-compile, so each installer has to be built on its own OS. On a Homebrew
JDK, Compose Desktop refuses to package by default; add
`-Pcompose.desktop.packaging.checkJdkVendor=false`, or use a Temurin/Corretto JDK for builds you
intend to sign or notarize.

**Known limitations.** "Set as wallpaper" is implemented per OS (macOS via `osascript`, Windows via
PowerShell, Linux via `gsettings`) but only the macOS path has been tested; on Windows, Linux
outside GNOME, or any failure, the image is still downloaded and the app tells you where it went.
Desktop-specific UI strings currently ship in English only.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).
