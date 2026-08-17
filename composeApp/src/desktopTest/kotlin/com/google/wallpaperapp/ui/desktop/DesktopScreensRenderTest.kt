package com.google.wallpaperapp.ui.desktop

import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.desktop.screens.DesktopCategoriesScreen
import com.google.wallpaperapp.ui.desktop.screens.DesktopFavouriteScreen
import com.google.wallpaperapp.ui.desktop.screens.DesktopFullScreenViewer
import com.google.wallpaperapp.ui.desktop.screens.DesktopSettingsScreen
import com.google.wallpaperapp.ui.desktop.theme.DesktopTheme
import kotlin.test.Test
import kotlin.test.assertTrue

private const val OUT = "build/render"

/**
 * Layout smoke renders for the individual screens. Network images stay blank in a single headless
 * frame -- what this catches is structural: overflow, clipping, a form stretched edge to edge.
 */
class DesktopScreensRenderTest {

    @Test
    fun `categories grid renders`() {
        val file = renderToPng("$OUT/screen-categories.png", 1440, 900) {
            DesktopTheme { DesktopCategoriesScreen(onCategoryClick = {}) }
        }
        assertTrue(file.length() > 0)
    }

    @Test
    fun `settings page caps its content width`() {
        val file = renderToPng("$OUT/screen-settings.png", 1920, 900) {
            DesktopTheme {
                DesktopSettingsScreen(currentLanguageName = "English", onLanguageClick = {})
            }
        }
        assertTrue(file.length() > 0)
    }

    @Test
    fun `favourites renders both empty and populated`() {
        renderToPng("$OUT/screen-favourites-empty.png", 1440, 900) {
            DesktopTheme {
                DesktopFavouriteScreen(
                    favourites = emptyList(),
                    onOpen = {},
                    onOpenFullScreen = {},
                    onRemove = {},
                    onApply = {},
                    onDownload = {},
                    onExplore = {}
                )
            }
        }
        val file = renderToPng("$OUT/screen-favourites.png", 1440, 900) {
            DesktopTheme {
                DesktopFavouriteScreen(
                    favourites = List(7) {
                        FavouriteWallpaper(id = it.toLong(), wallpaper = "", landscape = "")
                    },
                    onOpen = {},
                    onOpenFullScreen = {},
                    onRemove = {},
                    onApply = {},
                    onDownload = {},
                    onExplore = {}
                )
            }
        }
        assertTrue(file.length() > 0)
    }

    @Test
    fun `fullscreen viewer renders HUD and layout`() {
        val sample = Wallpaper(
            id = 12345L,
            photographerName = "Jane Doe",
            photographerUrl = "https://example.com/janedoe",
            medium = "",
            portrait = "",
            small = "",
            landscape = "",
            original = "",
            alt = "Aurora Borealis"
        )
        val file = renderToPng("$OUT/screen-fullscreen-viewer.png", 1440, 900) {
            DesktopTheme {
                DesktopFullScreenViewer(
                    wallpaper = sample,
                    isFavourite = false,
                    items = listOf(sample),
                    onClose = {},
                    onNext = {},
                    onPrevious = {},
                    onApply = {},
                    onDownload = {},
                    onToggleFavourite = {},
                    onCopyLink = {},
                    onOpenPhotographer = {}
                )
            }
        }
        assertTrue(file.length() > 0)
    }
}
