package com.google.wallpaperapp.ui.desktop

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.components.PaginationBar
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import com.google.wallpaperapp.ui.theme.TextLow
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The page window has fiddly edge cases (near the start, near the end, few pages). Rendering all
 * of them in one image makes a regression obvious at a glance.
 */
class PaginationBarRenderTest {

    @Test
    fun `grid and pagination bar compose without the bar being pushed off`() {
        val items = List(40) {
            com.google.wallpaperapp.domain.models.Wallpaper(
                id = it.toLong(), photographerName = "p", photographerUrl = "u",
                medium = "", portrait = "", small = "", landscape = "", original = "", alt = "a"
            )
        }
        val file = renderToPng("build/render/page-grid.png", 1440, 900) {
            DesktopTheme {
                com.google.wallpaperapp.ui.desktop.components.WallpaperPageGrid(
                    state = com.google.wallpaperapp.ui.desktop.paging.PageState(
                        items = items, page = 3, totalPages = 1655
                    ),
                    favouriteIds = emptySet(),
                    onPageSelected = {}, onRetry = {}, onOpen = {}, onOpenFullScreen = {},
                    onToggleFavourite = {}, onApply = {}, onDownload = {}, onCopyUrl = {},
                    onOpenPhotographer = {}, emptyTitle = "", emptySubtitle = ""
                )
            }
        }
        assertTrue(file.length() > 0)
    }

    @Test
    fun `renders every page-window edge case`() {
        val cases = listOf(
            1 to 1,        // single page -> bar hides itself entirely
            1 to 3,        // fewer than the window -> plain 1 2 3
            1 to 1655,     // first page of many
            2 to 1655,
            42 to 1655,    // middle -> gaps on both sides
            1654 to 1655,  // near the end
            1655 to 1655,  // last page
            100 to 200     // search-sized feed
        )

        val file = renderToPng("build/render/pagination-bar.png", 1100, 60 + cases.size * 78) {
            DesktopTheme {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    cases.forEach { (current, total) ->
                        Text("page $current of $total", color = TextLow)
                        PaginationBar(
                            currentPage = current,
                            totalPages = total,
                            onPageSelected = {}
                        )
                    }
                }
            }
        }
        assertTrue(file.length() > 0)
    }
}
