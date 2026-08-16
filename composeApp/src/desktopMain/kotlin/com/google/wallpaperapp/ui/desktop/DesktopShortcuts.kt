package com.google.wallpaperapp.ui.desktop

import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import com.google.wallpaperapp.core.platform.DesktopOs
import com.google.wallpaperapp.core.platform.currentOs

/** Command on macOS, Control everywhere else -- matching what each platform's users expect. */
val KeyEvent.isMenuModifierPressed: Boolean
    get() = if (currentOs == DesktopOs.MAC) isMetaPressed else isCtrlPressed

/** The modifier name to print in a menu item. */
val menuModifierLabel: String
    get() = if (currentOs == DesktopOs.MAC) "⌘" else "Ctrl+"

/**
 * Window-level shortcuts. Returns true when the event was consumed.
 *
 * Quit and close-window are deliberately absent: the OS and the Compose window already handle
 * ⌘Q / ⌘W, and rebinding them here would only break the platform behaviour.
 */
fun handleDesktopShortcut(
    event: KeyEvent,
    onFocusSearch: () -> Unit,
    onOpenSettings: () -> Unit,
    onBack: () -> Boolean
): Boolean {
    if (event.type != KeyEventType.KeyDown) return false

    return when {
        event.key == Key.Escape -> onBack()

        event.isMenuModifierPressed && event.key == Key.F -> {
            onFocusSearch(); true
        }

        event.isMenuModifierPressed && event.key == Key.Comma -> {
            onOpenSettings(); true
        }

        else -> false
    }
}
