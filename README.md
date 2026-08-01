# Screeny

A Kotlin Multiplatform wallpaper app for **Android** and **iOS**, built with Compose Multiplatform. Browse, search, and favourite high-resolution wallpapers, then download them or set them straight as your device wallpaper. Wallpapers are sourced from the [Pexels API](https://www.pexels.com/api/).

<img width="736" height="1600" alt="Screeny" src="https://github.com/user-attachments/assets/f0dd660b-58a7-4118-8881-78c7e36c6d41" />

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

- macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run — iOS

Use the run configuration in your IDE's run widget, or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/).
