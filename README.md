<div align="center">

  <img src="desktopApp/src/main/resources/screeny.png" alt="Screeny Logo" width="100" />

  # Screeny

  **A modern, dark-first wallpaper application crafted with Compose Multiplatform for Android, iOS, macOS, Windows, and Linux.**

  [![Kotlin](https://img.shields.io/badge/Kotlin-2.4.0-7F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
  [![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.3-4285F4.svg?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
  [![Platform](https://img.shields.io/badge/Platforms-Android%20%7C%20iOS%20%7C%20macOS%20%7C%20Windows%20%7C%20Linux-E01F8B.svg?style=for-the-badge)](https://github.com)
  [![License](https://img.shields.io/badge/License-Apache%202.0-2FE6A6.svg?style=for-the-badge)](LICENSE)

  <p align="center">
    <a href="#-features">Features</a> •
    <a href="#-desktop-experience">Desktop Showcase</a> •
    <a href="#-mobile-showcase">Mobile Showcase</a> •
    <a href="#-tech-stack">Tech Stack</a> •
    <a href="#-getting-started">Getting Started</a> •
    <a href="#-shortcuts">Shortcuts</a>
  </p>

</div>

---

## 🌟 Highlights

Screeny delivers high-resolution wallpapers sourced from the [Pexels API](https://www.pexels.com/api/) with a custom dark-glassmorphism design system. 

Unlike apps that simply stretch a phone interface onto larger screens, **Screeny features purpose-built desktop and mobile layouts sharing the same domain, ViewModels, and Room database.**

---

## 🖥️ Desktop Showcase

<div align="center">
  <h3>✨ Immersive Full-Screen High-Resolution Preview with Toggleable HUD</h3>
  <img src="screenshots/desktop-preview.png" alt="Desktop Full-Screen Preview" width="100%" />
</div>

<br />

<div align="center">
  <h3>🗂️ Browse Feed with Numbered Pagination & Detail Pane</h3>
  <img src="screenshots/desktop-home.png" alt="Desktop Browse Grid" width="100%" />
</div>

<br />

<div align="center">
  <table width="100%">
    <tr>
      <td width="50%" align="center"><b>📂 Curated Categories</b></td>
      <td width="50%" align="center"><b>❤️ Saved Favourites (Room KMP)</b></td>
    </tr>
    <tr>
      <td><img src="screenshots/desktop-categories.png" alt="Desktop Categories" width="100%" /></td>
      <td><img src="screenshots/desktop-favourites.png" alt="Desktop Favourites" width="100%" /></td>
    </tr>
  </table>
</div>

---

## 📱 Mobile Showcase

<div align="center">
  <table border="0">
    <tr>
      <td align="center"><b>Curated Feed</b></td>
      <td align="center"><b>Wallpaper Detail</b></td>
      <td align="center"><b>Similar Wallpapers</b></td>
    </tr>
    <tr>
      <td><img src="screenshots/home.png" width="240" alt="Mobile Home" /></td>
      <td><img src="screenshots/detail.png" width="240" alt="Mobile Detail" /></td>
      <td><img src="screenshots/similar.png" width="240" alt="Mobile Similar" /></td>
    </tr>
  </table>
</div>

---

## ✨ Features

- 🖥️ **Tailored Desktop UI (macOS / Windows / Linux)**:
  - Fixed glassmorphic sidebar and persistent instant-search toolbar.
  - Numbered page navigation (40 items/page) with direct jump-to-page dialog.
  - Hover action overlays and right-click context menus.
  - Native window size & position state persistence.
  - Native OS wallpaper setting (`osascript` on macOS, PowerShell on Windows, `gsettings` on Linux).
- 🔍 **Immersive Full-Screen Lightbox**:
  - Displays original high-resolution imagery.
  - Click anywhere or press <kbd>Space</kbd> to toggle the floating glass HUD.
  - Photographer credit, resolution info, and quick profile navigation.
  - Seamless in-viewer feed browsing with <kbd>←</kbd> / <kbd>→</kbd> arrow keys.
- 📱 **Fluid Mobile Experience (Android & iOS)**:
  - Infinite scroll feed powered by Paging 3 Multiplatform.
  - Gesture-driven bottom sheets, shared element transitions, and parallax scrolling.
- 🎨 **Mesh Gradient Generator**:
  - Interactive multi-point aurora gradient presets with instant wallpaper application and PNG export.
- 💾 **Local Persistence**:
  - Offline favourites, user preferences, and search history powered by multiplatform Room SQLite.
- 🌐 **Internationalization**:
  - Multi-language support with runtime language switching across 20+ locales.
- 🔒 **Zero-Leak Secret Architecture**:
  - API keys injected at build time from `local.properties` or environment variables without committing secrets to git.

---

## 🛠️ Tech Stack & Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                       Compose Multiplatform UI              │
│   ┌───────────────────────────┐ ┌───────────────────────┐   │
│   │   Mobile UI (Navigation3) │ │ Dedicated Desktop UI  │   │
│   └─────────────┬─────────────┘ └───────────┬───────────┘   │
└─────────────────┼───────────────────────────┼───────────────┘
                  ▼                           ▼
┌─────────────────────────────────────────────────────────────┐
│                 Shared ViewModels & Domain Models           │
├─────────────────────────────────────────────────────────────┤
│         Koin DI (Annotations)  •  Mappers & UseCases       │
├─────────────────────────────────────────────────────────────┤
│   Ktor Client (OkHttp / Darwin) │   Room Database (KMP SQLite)│
└─────────────────────────────────────────────────────────────┘
```

| Layer | Technologies |
| :--- | :--- |
| **Framework** | [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform/) (Kotlin 2.4 / AGP 9.1) |
| **Dependency Injection** | [Koin](https://insert-koin.io/) + Koin Annotations (KSP) |
| **Networking** | [Ktor](https://ktor.io/) 3.5 (OkHttp on Android/JVM, Darwin on iOS) |
| **Local Storage** | [Room KMP](https://developer.android.com/kotlin/multiplatform/room) 2.8 + SQLite |
| **Paging** | [CashApp Multiplatform Paging 3](https://github.com/cashapp/multiplatform-paging) |
| **Image Loading** | [Landscapist](https://github.com/skydoves/landscapist) + [Coil 3](https://coil-kt.github.io/coil/) |
| **Design System** | Glassmorphism (`Ink950` dark space palette, Aurora gradients, frosted scrims) |

---

## 🚀 Getting Started

### 1. Clone the repository
```bash
git clone https://github.com/Shahidzbi4213/WallpaperApp-Cmp.git
cd WallpaperApp-Cmp
```

### 2. Configure your Pexels API Key
Get a free API key at [pexels.com/api](https://www.pexels.com/api/). Copy [`local.properties.example`](./local.properties.example) to `local.properties` and paste your key:

```properties
PEXELS_API_KEY=your_pexels_api_key_here
```
*(You can also export `PEXELS_API_KEY` as an environment variable).*

---

### 3. Build & Run

#### 🖥️ Desktop (macOS, Windows, Linux)
```bash
./gradlew :desktopApp:run
```

To create native OS packages:
```bash
./gradlew :desktopApp:packageDmg   # macOS (.dmg)
./gradlew :desktopApp:packageMsi   # Windows (.msi)
./gradlew :desktopApp:packageDeb   # Linux (.deb)
```

#### 🤖 Android
```bash
./gradlew :androidApp:assembleDebug
```

#### 🍎 iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and press **Run**, or use Android Studio / Fleet with the KMP plugin.

---

## ⌨️ Desktop Keyboard Shortcuts

| Shortcut | Action |
| :--- | :--- |
| <kbd>⌘</kbd> / <kbd>Ctrl</kbd> + <kbd>F</kbd> | Focus search bar |
| <kbd>⌘</kbd> / <kbd>Ctrl</kbd> + <kbd>,</kbd> | Open Settings |
| <kbd>Esc</kbd> | Close preview / full-screen viewer / back |
| <kbd>Space</kbd> | Toggle HUD controls in full-screen preview |
| <kbd>←</kbd> / <kbd>→</kbd> | Browse previous / next wallpaper in full-screen |
| <kbd>F</kbd> | Toggle favourite status |

---

## 🧪 Testing

```bash
# Run multiplatform shared unit tests
./gradlew :composeApp:allTests

# Run Desktop UI smoke tests & headless preview renders
./gradlew :composeApp:desktopTest
```

---

## 💖 Star the Repo

If you like Screeny or find this Compose Multiplatform reference helpful, please give it a **⭐ on GitHub**!

---

<div align="center">
  <sub>Built with ❤️ using Kotlin & Compose Multiplatform.</sub>
</div>
