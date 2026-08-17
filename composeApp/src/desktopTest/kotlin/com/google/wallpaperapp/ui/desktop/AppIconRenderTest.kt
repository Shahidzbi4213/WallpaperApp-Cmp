package com.google.wallpaperapp.ui.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.ScreenyGradient
import com.google.wallpaperapp.ui.theme.Void
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Generates the desktop app icon from the same brand tokens the sidebar mark uses, so the icon
 * cannot drift from the in-app branding. Run, then convert to .icns / .ico with the OS tooling.
 */
class AppIconRenderTest {

    @Test
    fun `renders the 1024px app icon`() {
        val file = renderToPng("build/icon/screeny-icon-1024.png", 1024, 1024) {
            // Full bleed: macOS applies its own mask, so no transparent margin here.
            Box(
                modifier = Modifier.fillMaxSize().background(Void),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .padding(96.dp)
                        .fillMaxSize()
                        .clip(RoundedCornerShape(184.dp))
                        .background(ScreenyGradient)
                )
            }
        }
        assertTrue(file.length() > 0)
    }
}
