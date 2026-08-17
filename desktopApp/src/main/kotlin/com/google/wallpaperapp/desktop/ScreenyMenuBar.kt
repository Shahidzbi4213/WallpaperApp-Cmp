package com.google.wallpaperapp.desktop

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.MenuBar
import com.google.wallpaperapp.core.platform.DesktopOs
import com.google.wallpaperapp.core.platform.currentOs
import com.google.wallpaperapp.ui.desktop.DesktopAppController
import com.google.wallpaperapp.ui.routs.TopLevelBackStack

/**
 * A real menu bar -- on macOS it lands in the system bar, elsewhere at the top of the window.
 * Every entry mirrors a keyboard shortcut so the shortcuts are discoverable rather than folklore.
 */
@Composable
fun FrameWindowScope.ScreenyMenuBar(controller: DesktopAppController) {
    val meta = currentOs == DesktopOs.MAC

    MenuBar {
        Menu("View", mnemonic = 'V') {
            Item(
                "Home",
                shortcut = KeyShortcut(Key.One, meta = meta, ctrl = !meta),
                onClick = { controller.selectSection(TopLevelBackStack.Home) }
            )
            Item(
                "Categories",
                shortcut = KeyShortcut(Key.Two, meta = meta, ctrl = !meta),
                onClick = { controller.selectSection(TopLevelBackStack.Categories) }
            )
            Item(
                "Gradients",
                shortcut = KeyShortcut(Key.Three, meta = meta, ctrl = !meta),
                onClick = { controller.selectSection(TopLevelBackStack.MeshGradients) }
            )
            Item(
                "Favourites",
                shortcut = KeyShortcut(Key.Four, meta = meta, ctrl = !meta),
                onClick = { controller.selectSection(TopLevelBackStack.Favourite) }
            )
            Separator()
            Item(
                "Settings",
                shortcut = KeyShortcut(Key.Comma, meta = meta, ctrl = !meta),
                onClick = controller::openSettings
            )
        }

        Menu("Search", mnemonic = 'S') {
            Item(
                "Find wallpapers…",
                shortcut = KeyShortcut(Key.F, meta = meta, ctrl = !meta),
                onClick = controller::focusSearch
            )
        }
    }
}
