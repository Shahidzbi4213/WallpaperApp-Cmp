package com.google.wallpaperapp.ui.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.core.platform.ToastDurationType
import com.google.wallpaperapp.core.platform.desktopToasts
import com.google.wallpaperapp.ui.desktop.components.DesktopSidebar
import com.google.wallpaperapp.ui.desktop.components.DesktopToolBar
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.theme.auroraBackground

/**
 * The desktop window frame: fixed left rail, toolbar with a live search field, content area.
 * Native window decorations are kept -- a macOS user expects real traffic lights, and a custom
 * title bar would only cost us window snapping and accessibility for no gain.
 */
@Composable
fun DesktopShell(
    selectedSection: TopLevelBackStack,
    title: String,
    searchQuery: String,
    searchFocusRequester: FocusRequester,
    canGoBack: Boolean,
    onSelectSection: (TopLevelBackStack) -> Unit,
    onSearchChange: (String) -> Unit,
    onBack: () -> Unit,
    content: @Composable (isWide: Boolean) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // ToastManager has no desktop equivalent, so its emissions land here instead.
    LaunchedEffect(Unit) {
        desktopToasts.collect { toast ->
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(
                message = toast.message,
                duration = when (toast.duration) {
                    ToastDurationType.SHORT -> SnackbarDuration.Short
                    ToastDurationType.LONG -> SnackbarDuration.Long
                }
            )
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().auroraBackground()) {
        val collapsed = maxWidth < DesktopDimens.SidebarCollapseThreshold
        val isWide = maxWidth >= DesktopDimens.SplitViewThreshold

        Row(modifier = Modifier.fillMaxSize()) {
            DesktopSidebar(
                selected = selectedSection,
                collapsed = collapsed,
                onSelect = onSelectSection
            )

            Column(modifier = Modifier.fillMaxSize()) {
                DesktopToolBar(
                    title = title,
                    searchQuery = searchQuery,
                    searchFocusRequester = searchFocusRequester,
                    canGoBack = canGoBack,
                    onBack = onBack,
                    onSearchChange = onSearchChange
                )
                Box(modifier = Modifier.fillMaxSize()) {
                    content(isWide)
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(24.dp)
        ) { data ->
            Snackbar(snackbarData = data)
        }
    }
}
