package com.google.wallpaperapp.core.platform

import androidx.compose.runtime.Composable

/**
 * No-op, like the iOS actual. Desktop has no system back gesture; the Escape key is bound
 * at the window level in [com.google.wallpaperapp.ui.desktop.DesktopShell] instead.
 */
@Composable
actual fun BackHandler(enable: Boolean, onBack: () -> Unit) {
}
