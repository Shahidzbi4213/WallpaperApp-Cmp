# Mesh Gradients & Shaders Implementation Plan

## Phase 1: Mesh Gradients (Current Phase)

### Overview
Mesh gradients provide organic, fluid, and multi-colored backgrounds that differ from traditional linear or radial gradients. They use multiple control points to blend colors smoothly in 2D space. Apps like Stripe, Canva, and dedicated wallpaper apps use them extensively for premium visual aesthetics.

### Implementation Details for Compose Multiplatform
1.  **Menu Integration:**
    *   Add a new bottom navigation or side menu item named "Mesh Gradient" (with a suitable icon like an abstract shape or palette).
2.  **UI & Presets:**
    *   Create a `MeshGradientScreen` that presents a list/grid of pre-defined mesh gradient presets (e.g., "Ocean Breeze", "Sunset Glow", "Aurora", etc.).
    *   Each preset will have a predefined set of colors and control points.
3.  **Rendering the Gradient:**
    *   Since this is a Compose Multiplatform app, we can use a custom Canvas implementation with overlapping radial gradients and blur effects to simulate a mesh gradient, or we can look into using `MeshGradientPainter` if the Compose version allows it, or a library like `ComposeMeshGradient` that supports KMP.
    *   The most cross-platform stable way to simulate them without an alpha API is to use multiple `Brush.radialGradient` overlapping on a `Canvas` with an `Modifier.blur` effect (if supported on target platforms) or just carefully spaced radial brushes.
4.  **Wallpaper Conversion:**
    *   When the user selects an effect, they can preview it.
    *   Provide an "Apply" button. When clicked, we will capture the Compose UI node into an `ImageBitmap` (using `ImageBitmap.captureToImage()` or a Compose-to-Bitmap utility) and then use the platform-specific Wallpaper Manager on Android (and image saving on iOS) to apply the wallpaper.

---

## Phase 2: Shader Gradients (Future Phase)

### Overview
Shader gradients use custom graphics shaders (like GLSL or AGSL) to calculate pixel colors mathematically in real-time, allowing for complex, animated, and highly performant effects.

### Future Implementation Details
1.  **Android Specifics:**
    *   Use `RuntimeShader` (available from Android 13+) or `RenderEffect` to execute Android Graphics Shading Language (AGSL).
    *   This provides extremely high performance for animated gradients (like shadows and fluid simulations).
2.  **Compose Multiplatform / Skia:**
    *   For cross-platform support (iOS/Desktop), explore Skia's `RuntimeEffect` API accessible via Compose Multiplatform's `org.jetbrains.skia.RuntimeEffect`. This allows compiling SKSL (Skia Shading Language) directly into a `ShaderBrush`.
3.  **UI Integration:**
    *   Add a "Shaders" section to the app alongside the Mesh Gradients.
    *   Provide controls for users to tweak shader uniforms (e.g., time, speed, color variables) to create interactive live wallpapers.
