package com.google.wallpaperapp.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/* ============================================================
   SCREENY — COLOR SYSTEM
   Dark-first, futuristic. Anchored on the logo's sunset -> nebula
   gradient (crimson -> orange fused with magenta -> violet).
   Mirrors tokens/colors.css from the Screeny design system.
   ============================================================ */

// ---- Neutral ramp: cool, near-black "space" neutrals ----
val Void = Color(0xFF07070B)      // deepest app backdrop
val Ink950 = Color(0xFF0B0B12)
val Ink900 = Color(0xFF101019)
val Ink850 = Color(0xFF16161F)
val Ink800 = Color(0xFF1C1C28)
val Ink750 = Color(0xFF232331)
val Ink700 = Color(0xFF2C2C3B)
val Ink600 = Color(0xFF3A3A4C)
val Ink500 = Color(0xFF4C4C61)
val Ink400 = Color(0xFF6A6A83)
val Ink300 = Color(0xFF8E8DA6)
val Ink200 = Color(0xFFB7B5CB)
val Ink100 = Color(0xFFDEDCEC)
val Paper = Color(0xFFF5F4FC)

// ---- Brand spectrum (sampled from the Screeny mark) ----
val Flare = Color(0xFFFF8A1E)     // amber / warm anchor
val Ember = Color(0xFFFF5A2E)     // orange-red — primary
val Crimson = Color(0xFFFF2E5B)   // hot pink-red
val Magenta = Color(0xFFE01F8B)
val Fuchsia = Color(0xFFC21FD6)
val Violet = Color(0xFF7A2FF0)    // cool anchor
val Indigo = Color(0xFF5B2FE0)

// ---- Semantic status ----
val Success = Color(0xFF2FE6A6)   // aurora teal-green
val Warning = Color(0xFFFFB23D)
val Danger = Color(0xFFFF4D5E)
val Info = Color(0xFF48A9FF)

// ---- Text ----
val TextHi = Color(0xFFF4F3FC)
val TextMid = Color(0xFFADA9C6)
val TextLow = Color(0xFF726E8C)

// ---- Glassmorphism — the core surface treatment ----
val GlassFill = Color.White.copy(alpha = 0.055f)
val GlassFillStrong = Color.White.copy(alpha = 0.10f)
val GlassBorder = Color.White.copy(alpha = 0.10f)
val GlassBorderHi = Color.White.copy(alpha = 0.18f)

/** Legacy alias kept for callers; now a glass fill. */
val ActionIconBgColor = GlassFillStrong

// ---- Signature gradients ----
// grad-screeny (105deg): the full warm -> cool sweep. Reserved for hero moments.
val ScreenyGradient = Brush.horizontalGradient(
    0.0f to Flare, 0.36f to Crimson, 0.66f to Fuchsia, 1.0f to Violet
)
// grad-ember (135deg): warm CTA
val EmberGradient = Brush.linearGradient(listOf(Flare, Crimson))
// grad-nebula (135deg): cool accent
val NebulaGradient = Brush.linearGradient(listOf(Magenta, Violet))
// grad-aurora (135deg): status / success
val AuroraGradient = Brush.linearGradient(listOf(Success, Info))
// bottom scrim over full-bleed imagery for legibility
val ScrimGradient = Brush.verticalGradient(listOf(Color.Transparent, Void.copy(alpha = 0.85f)))
// top scrim
val ScrimTopGradient = Brush.verticalGradient(listOf(Void.copy(alpha = 0.75f), Color.Transparent))
