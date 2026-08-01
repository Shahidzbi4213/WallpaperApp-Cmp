# Screeny

A Kotlin Multiplatform wallpaper app for **Android** and **iOS**, built with Compose Multiplatform. Browse, search, and favourite high-resolution wallpapers, then download them or set them straight as your device wallpaper. Wallpapers are blended from several free providers ([Wallhaven](https://wallhaven.cc/help/api), [Pexels](https://www.pexels.com/api/) and Bing's daily archive), so no single source can take the app down.

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

Wallpapers are blended from several providers, so the feed keeps working even
when one of them is down or rate-limited:

| Source | API key | Rate limit |
|---|---|---|
| [Wallhaven](https://wallhaven.cc/help/api) | none | 45 requests/minute |
| [Pexels](https://www.pexels.com/api/) | free | 200/hour, 20,000/month |
| [Bing daily](https://www.bing.com) | none | undocumented |

**No keys are required to build or run.** Wallhaven and Bing need none, so a
fresh clone gives you a working feed. Providers whose key is missing report
themselves disabled and are skipped.

To enable the keyed providers, copy [`local.properties.example`](./local.properties.example)
to `local.properties` and fill in what you have:

```properties
PEXELS_API_KEY=your_key_here
```

`local.properties` is gitignored. The same names also work as environment
variables, which is what CI should use. Keys are read at build time and emitted
into a generated `ApiKeys` object — nothing is committed.

Wallhaven is queried with `purity=100` (SFW only) and `categories=100`
(general only), both hardcoded.

The Bing provider uses an undocumented endpoint and its images are personal-use
only; it is gated behind a single `ENABLED` flag in
[`BingDailyProvider.kt`](./composeApp/src/commonMain/kotlin/com/google/wallpaperapp/data/remote/provider/BingDailyProvider.kt)
if you need to drop it.

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
