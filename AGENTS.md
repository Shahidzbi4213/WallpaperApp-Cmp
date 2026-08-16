# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## What this is

Screeny — a Compose Multiplatform wallpaper app (Android + iOS + Desktop) backed by the Pexels API. Shared UI and logic live in `:composeApp` (`commonMain`); `:androidApp` is the Android entry point, `:desktopApp` the JVM one, `iosApp/` is the Xcode project.

Desktop (macOS/Windows/Linux) does **not** reuse the mobile screens. It has its own screen tree in `composeApp/src/desktopMain/kotlin/.../ui/desktop/`, built to desktop conventions, over the same ViewModels, repositories and theme. See "Desktop" below.

## Commands

```bash
# Android debug build (the app APK is produced by :androidApp)
./gradlew :androidApp:assembleDebug

# Compile-check shared code across all targets without building apps
./gradlew :composeApp:build

# Run the common tests (commonTest, kotlin-test)
./gradlew :composeApp:allTests
# iOS-only test binary
./gradlew :composeApp:iosSimulatorArm64Test

# Run a single test class/method
./gradlew :composeApp:allTests --tests "com.google.wallpaperapp.SomeTest"

# Desktop: run the app
./gradlew :desktopApp:run
# Desktop: tests (includes headless UI renders written to composeApp/build/render/)
./gradlew :composeApp:desktopTest

# macOS installer. The vendor flag is needed only on a Homebrew JDK, which Compose
# Desktop refuses by default (JetBrains issue 3107). It packages fine here, but use a
# Temurin/Corretto JDK for anything you intend to sign or notarize.
./gradlew :desktopApp:packageDmg -Pcompose.desktop.packaging.checkJdkVendor=false
# packageMsi / packageDeb must be run on Windows / Linux — jpackage cannot cross-compile.
```

iOS is run from Xcode: open `iosApp/iosApp.xcodeproj`. The shared framework is `ComposeApp` (static), entry point `MainViewController()`.

## Architecture

Package root: `com.google.wallpaperapp` (note: `com.google.*` is a naming leftover, not a Google project).

Layering under `commonMain`:
- `data/remote` — Ktor client → `PexelWallpapersApi` / `...Impl`. Base URLs in `data/utils/HttpRoutes.kt`, page size in `Constant.kt`.
- `data/local` — Room (KMP) `ScreenyDatabase` + DAOs + entities. `data/paging` — `RemoteMediator` + `PagingSource` feeding Paging 3 (`app.cash.paging` multiplatform fork).
- `data/repositories` — repositories combine remote + local; `domain/mappers` convert response/entity ↔ `domain/models`.
- `ui/screens/<feature>` — each feature is a screen + `ViewModel` + (often) `State`/`Event`. `ui/components` and `ui/composables` are shared widgets; `ui/theme` holds the design system.

### Navigation (Navigation3, not the classic NavHost)

Two nested `NavDisplay`s:
- `ui/App.kt` — root back stack (`Routs` sealed interface): Splash → MainScreen → detail/search/language screens. Shared-element transitions via `SharedTransitionLayout`.
- `ui/screens/main/MainScreen.kt` — inner back stack for the 5 bottom-nav tabs (`TopLevelBackStack`: Home/Categories/MeshGradients/Favourite/Settings).

Desktop does not use Navigation3 at all — see "Desktop" below.

Every route is a `@Serializable data class/object`. **When you add a route you must register it in the `SavedStateConfiguration` polymorphic block** (in `App.kt` for top-level routes, `MainScreen.kt` for tabs) or state restoration crashes. Screens are decoupled from navigation via `MainNavigationAction` callbacks — screens never touch the back stack directly.

### DI (Koin annotations)

`di/initKoin.kt` starts Koin with annotation-generated modules (`NetworkModule`, `AppModule`, `DbModule`, `FavouriteModule`) plus the expect/actual `platformDbModule()`. ViewModels are `@KoinViewModel` and injected with `koinViewModel()`. Android bootstraps via `BaseApp.onCreate()`; iOS calls `initKoin()` before showing the view controller.

Note: `compileSafety` is disabled for the Koin compiler in `composeApp/build.gradle.kts` because platform DB wiring still comes from a hand-written expect/actual DSL module rather than annotations.

### Platform code (expect/actual)

Anything platform-specific lives in `core/platform` as an `expect` in `commonMain` with `.android.kt` / `.ios.kt` / `.desktop.kt` actuals: `applyWallpaper`, `WallpaperDownloader`, `LocaleManager`, `showToast`, `BackHandler`, `HttpEngineFactory` (OkHttp / Darwin / OkHttp), `PlatformDb` (Room builder), `ExitApp`, `AppLogger`, `getPlatformType`.

`PlatformType` has three values (`ANDROID`, `IOS`, `DESKTOP`). All existing call sites test `== IOS`, so adding a platform stays source-compatible — but check those branches when you touch them.

### Desktop

- **Entry point:** `:desktopApp` → `Main.kt` calls `initKoin()` then opens the window. `ScreenyMenuBar.kt` is the macOS/Windows menu bar.
- **UI:** `composeApp/src/desktopMain/.../ui/desktop/` — `DesktopShell` (fixed sidebar + toolbar with a persistent search field), `screens/` (its own Home/Categories/Favourites/Settings/Gradients/DetailPane/Language dialog), `components/` (16:9 `WallpaperCard`, `WallpaperPagedGrid`, scrollbars, tooltips, hover). Mobile never compiles any of it.
- **No Navigation3 on desktop.** `DesktopNavState` is plain Compose state (section + optional drill-down + preview). There is nothing to register in a `SavedStateConfiguration` block, and the mobile nav graph is untouched by desktop work.
- **`DesktopAppController`** is the seam between the window (menu bar, key events) and the composition; the composition registers handlers in a `DisposableEffect` and clears them on exit.
- **Desktop-only APIs used:** `VerticalScrollbar`, `TooltipArea`, `ContextMenuArea`, `DialogWindow`, `pointerHoverIcon` — all fine in `desktopMain`, none available in `commonMain`.
- **`DesktopPaths.kt` is the single place the app branches on OS** (`currentOs`, `appDataDir()`, `downloadsDir()`). Add OS-specific behaviour there, not inline.
- **Headless render harness:** `composeApp/src/desktopTest/.../RenderPreview.kt` renders any composable to a PNG with no window (`build/render/`). Use it to check desktop layout without launching the app; `AppIconRenderTest` also generates the app icon from the brand gradient.

## Gotchas

- **Room on KMP + KSP ordering:** the build forces every `ksp*` task to depend on `kspCommonMainKotlinMetadata`, and `commonMain` adds the generated KSP metadata dir to its source set. If you see unresolved generated Room/Koin symbols, it's usually a stale metadata build — re-run a clean build.
- **Dark-only re-skin:** the app is intentionally dark-only (Screeny glassmorphic theme in `ui/theme`, `auroraBackground()`). `MainActivity` hardcodes dark system bars. Don't add light-theme branches unless asked.
- **Pexels API key is committed** inline in `NetworkModule.kt` (`Authorization` header). It's a free Pexels key; leave it unless rotating.
- **Seed row:** `DbModule` inserts a default `user_preference` row (`en`, appMode 0, dynamicColor 1) in the Room `onCreate` callback — the app assumes exactly one preferences row exists.
- **Localization:** UI strings live in `composeApp/src/commonMain/composeResources/values*`; the supported-locale allowlist is duplicated in `androidApp/build.gradle.kts` (`localeFilters`). Update both when adding a language. Desktop strings are prefixed `desktop_` and exist in the base locale only — the 21 translations have not been updated for them.
- **Desktop language switching remounts the tree.** Compose Resources reads `Locale.getDefault()` at composition time and does not observe changes, so `LocaleManager.desktop.kt` sets the JVM locale and pushes to `desktopLocale`, and `DesktopApp` wraps its content in `key(locale) { … }`. Don't "optimise" that key away.
- **`Dispatchers.Main` on desktop** comes from `kotlinx-coroutines-swing` in `desktopMain`. Remove it and every `viewModelScope.launch` dies at runtime with "Module with the Main dispatcher is missing" — it compiles fine, so nothing catches it before you run.
- **`koin-compose` vs `koin-androidx-compose`:** `commonMain` must use `io.insert-koin:koin-compose` (multiplatform). `koin-androidx-compose` is an Android-only AAR and belongs to `:androidApp` alone; putting it in the common bundle breaks any JVM compile classpath.
- **Landscape vs portrait urls:** `SrcResponse`/`SrcEntity`/`Wallpaper` carry `portrait`, `landscape` and `original`. Use the `Wallpaper.gridUrl` accessor (landscape on desktop, portrait on mobile) and `Wallpaper.fullUrl` (original, for download/apply) rather than reading `.portrait` directly in new code. `/v1/curated` does not accept an `orientation` parameter — the landscape url comes from the same cached row, so there is one cache for both platforms.
- **Favourites are identified by the portrait url.** `FavouriteWallpaperEntity.wallpaper` always stores the portrait url (the detail screen and `deleteViaUrl` match on it); `landscape` is an extra column for desktop rendering. Don't repurpose `wallpaper`.
- **Room schema is at version 3** with a hand-written `MIGRATION_2_3` in `data/local/Migrations.kt`. `exportSchema = false`, so there are no schema json files — don't add a destructive-migration fallback, it would wipe favourites, preferences and recent searches.
