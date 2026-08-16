package com.google.wallpaperapp.desktop

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.google.wallpaperapp.di.initKoin
import com.google.wallpaperapp.ui.desktop.DesktopApp
import com.google.wallpaperapp.ui.desktop.DesktopAppController
import com.google.wallpaperapp.ui.desktop.WindowStateStore
import com.google.wallpaperapp.ui.desktop.handleDesktopShortcut
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
fun main() {
    // Koin has to be up before the first composition: several platform actuals resolve their
    // dependencies through the service locator.
    initKoin()

    application {
        val windowState = remember { WindowStateStore.load() }
        val controller = remember { DesktopAppController() }

        // Persist geometry as it settles rather than only on close, so a force-quit or a crash
        // still leaves the window where the user put it.
        LaunchedEffect(windowState) {
            snapshotFlow { Triple(windowState.size, windowState.position, windowState.placement) }
                .distinctUntilChanged()
                .debounce(500.milliseconds)
                .collect { WindowStateStore.save(windowState) }
        }

        Window(
            onCloseRequest = {
                WindowStateStore.save(windowState)
                exitApplication()
            },
            title = "Screeny",
            state = windowState,
            onKeyEvent = { event ->
                handleDesktopShortcut(
                    event = event,
                    onFocusSearch = controller::focusSearch,
                    onOpenSettings = controller::openSettings,
                    onBack = controller::back
                )
            }
        ) {
            ScreenyMenuBar(controller)
            DesktopApp(controller)
        }
    }
}
