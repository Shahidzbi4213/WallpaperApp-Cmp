package com.google.wallpaperapp.ui.desktop

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.Composable
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.routs.TopLevelBackStack

/**
 * What the content area is currently showing. A section plus an optional drill-down, which is
 * all a sidebar-driven desktop app needs.
 */
sealed interface DesktopDestination {
    data class Section(val key: TopLevelBackStack) : DesktopDestination
    data class Category(val name: String, val query: String) : DesktopDestination
    data class Search(val query: String) : DesktopDestination
}

/** What the preview pane is showing, and which list it can page through. */
data class DesktopPreview(
    val wallpaper: Wallpaper,
    val source: PreviewSource
)

enum class PreviewSource { PAGED, CATEGORY, SEARCH, FAVOURITE }

/**
 * Deliberately not Navigation3. Desktop has no process death to restore from, so the
 * SavedStateConfiguration polymorphic registration that the mobile graph needs buys nothing
 * here -- and skipping it means the mobile navigation graph is untouched by desktop work.
 */
@Stable
class DesktopNavState {

    var destination: DesktopDestination by mutableStateOf(
        DesktopDestination.Section(TopLevelBackStack.Home)
    )
        private set

    var preview: DesktopPreview? by mutableStateOf(null)
        private set

    /** Drill-downs only; the sidebar itself is a flat switch, as on any desktop app. */
    private val history = ArrayDeque<DesktopDestination>()

    val canGoBack: Boolean get() = preview != null || history.isNotEmpty()

    fun selectSection(key: TopLevelBackStack) {
        if (destination == DesktopDestination.Section(key)) return
        history.clear()
        preview = null
        destination = DesktopDestination.Section(key)
    }

    fun openCategory(name: String, query: String) {
        history.addLast(destination)
        preview = null
        destination = DesktopDestination.Category(name, query)
    }

    fun openSearch(query: String) {
        val next = DesktopDestination.Search(query)
        if (destination is DesktopDestination.Search) {
            // Typing in the toolbar refines the same search rather than stacking history.
            destination = next
            return
        }
        history.addLast(destination)
        preview = null
        destination = next
    }

    fun openPreview(wallpaper: Wallpaper, source: PreviewSource) {
        preview = DesktopPreview(wallpaper, source)
    }

    fun closePreview() {
        preview = null
    }

    /** Escape / back button: closes the preview first, then unwinds a drill-down. */
    fun back(): Boolean {
        if (preview != null) {
            preview = null
            return true
        }
        val previous = history.removeLastOrNull() ?: return false
        destination = previous
        return true
    }
}

@Composable
fun rememberDesktopNavState(): DesktopNavState = remember { DesktopNavState() }
