package com.google.wallpaperapp.ui.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import com.google.wallpaperapp.core.platform.appDataDir
import java.io.File
import java.util.Properties

/**
 * Remembers where the window was last time.
 *
 * Deliberately a properties file rather than the Room `user_preference` row: window geometry is
 * transient, desktop-only, and not worth four new columns plus a schema migration on a table that
 * holds real user data.
 */
object WindowStateStore {

    private val file: File get() = File(appDataDir(), "window.properties")

    private const val KEY_WIDTH = "width"
    private const val KEY_HEIGHT = "height"
    private const val KEY_X = "x"
    private const val KEY_Y = "y"
    private const val KEY_MAXIMIZED = "maximized"

    fun load(): WindowState {
        val props = readProperties() ?: return defaultState()

        val width = props.float(KEY_WIDTH) ?: return defaultState()
        val height = props.float(KEY_HEIGHT) ?: return defaultState()
        val maximized = props.getProperty(KEY_MAXIMIZED)?.toBoolean() ?: false
        val x = props.float(KEY_X)
        val y = props.float(KEY_Y)

        return WindowState(
            // Guard against a stored size from a monitor that is no longer attached.
            size = androidx.compose.ui.unit.DpSize(
                width.coerceIn(MIN_WIDTH, MAX_WIDTH).dp,
                height.coerceIn(MIN_HEIGHT, MAX_HEIGHT).dp
            ),
            position = if (x != null && y != null && x >= 0f && y >= 0f) {
                WindowPosition(x.dp, y.dp)
            } else {
                WindowPosition.PlatformDefault
            },
            placement = if (maximized) WindowPlacement.Maximized else WindowPlacement.Floating
        )
    }

    fun save(state: WindowState) {
        runCatching {
            val props = Properties().apply {
                setProperty(KEY_WIDTH, state.size.width.value.toString())
                setProperty(KEY_HEIGHT, state.size.height.value.toString())
                setProperty(KEY_MAXIMIZED, (state.placement == WindowPlacement.Maximized).toString())
                val position = state.position
                if (position is WindowPosition.Absolute) {
                    setProperty(KEY_X, position.x.value.toString())
                    setProperty(KEY_Y, position.y.value.toString())
                }
            }
            file.outputStream().use { props.store(it, "Screeny window state") }
        }
        // A failure here is cosmetic -- the next launch just uses the default geometry.
    }

    private fun readProperties(): Properties? = runCatching {
        if (!file.exists()) return null
        Properties().apply { file.inputStream().use { load(it) } }
    }.getOrNull()

    private fun Properties.float(key: String): Float? = getProperty(key)?.toFloatOrNull()

    private fun defaultState() = WindowState(
        size = androidx.compose.ui.unit.DpSize(1440.dp, 900.dp)
    )

    private const val MIN_WIDTH = 900f
    private const val MIN_HEIGHT = 600f
    private const val MAX_WIDTH = 8000f
    private const val MAX_HEIGHT = 5000f
}
