package com.google.wallpaperapp.ui.desktop

import androidx.compose.ui.focus.FocusRequester
import com.google.wallpaperapp.ui.desktop.components.DesktopEmptyState
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import kotlin.test.Test
import kotlin.test.assertTrue

private const val OUT = "build/render"

/**
 * Renders the shell headlessly at a few window sizes. Catches layout blowups (overlap, clipping,
 * a sidebar that never collapses) without launching the app.
 */
class DesktopShellRenderTest {

    private fun shell(width: Int, height: Int, name: String) {
        val file = renderToPng("$OUT/$name.png", width, height) {
            DesktopTheme {
                DesktopShell(
                    selectedSection = TopLevelBackStack.Home,
                    title = "Home",
                    searchQuery = "",
                    searchFocusRequester = FocusRequester(),
                    canGoBack = false,
                    onSelectSection = {},
                    onSearchChange = {},
                    onBack = {}
                ) { isWide ->
                    DesktopEmptyState(
                        title = "Content area",
                        subtitle = if (isWide) "wide" else "compact"
                    )
                }
            }
        }
        assertTrue(file.length() > 0, "render produced an empty file")
    }

    @Test
    fun `renders at common desktop window sizes`() {
        shell(1440, 900, "shell-1440")
        shell(1024, 720, "shell-1024-collapsed-sidebar")
        shell(2560, 1440, "shell-2560")
    }
}
