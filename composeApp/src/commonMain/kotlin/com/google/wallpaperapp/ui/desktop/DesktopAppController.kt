package com.google.wallpaperapp.ui.desktop

import androidx.compose.runtime.Stable
import com.google.wallpaperapp.ui.routs.TopLevelBackStack

/**
 * The seam between the window (menu bar, key events) and the composition.
 *
 * It is created in `main` and outlives any single composition, so it holds only intents. The
 * composition registers handlers when it starts and clears them when it leaves; before that, or
 * after, an intent is simply dropped rather than crashing a menu click during startup.
 */
@Stable
class DesktopAppController {

    internal var onFocusSearch: (() -> Unit)? = null
    internal var onSelectSection: ((TopLevelBackStack) -> Unit)? = null
    internal var onBackRequest: (() -> Boolean)? = null

    fun focusSearch() {
        onFocusSearch?.invoke()
    }

    fun selectSection(section: TopLevelBackStack) {
        onSelectSection?.invoke(section)
    }

    fun openSettings() {
        onSelectSection?.invoke(TopLevelBackStack.Settings)
    }

    /** True when something was actually dismissed, so Escape does not swallow other handling. */
    fun back(): Boolean = onBackRequest?.invoke() ?: false
}
