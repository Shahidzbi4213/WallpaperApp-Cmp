package com.google.wallpaperapp.ui.routs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.StringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.categories
import wallpaperapp.composeapp.generated.resources.favourite
import wallpaperapp.composeapp.generated.resources.home
import wallpaperapp.composeapp.generated.resources.settings

data class BottomNavItem(
    val key: TopLevelBackStack,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val label: StringResource
)


@Stable
@Serializable
sealed interface TopLevelBackStack : NavKey {

    @Serializable
    data object Home :  TopLevelBackStack

    @Serializable
    data object Categories :  TopLevelBackStack

    @Serializable
    data object Favourite :  TopLevelBackStack

    @Serializable
    data object Settings :  TopLevelBackStack

    companion object {
        val Saver: Saver<TopLevelBackStack, String> = Saver(
            save = {
                when (it) {
                    Home -> "home"
                    Categories -> "categories"
                    Favourite -> "favourite"
                    Settings -> "settings"
                }
            },
            restore = {
                when (it) {
                    "home" -> Home
                    "categories" -> Categories
                    "favourite" -> Favourite
                    "settings" -> Settings
                    else -> Home
                }
            }
        )
    }

}


val bottomNavigationItems = listOf(
    BottomNavItem(
        label = Res.string.home, key = TopLevelBackStack.Home, icon = Icons.Outlined.Dashboard, selectedIcon = Icons.Filled.Dashboard
    ), BottomNavItem(
        label = Res.string.categories, key = TopLevelBackStack.Categories, icon = Icons.Outlined.Category, selectedIcon = Icons.Filled.Category
    ), BottomNavItem(
        label = Res.string.favourite, key = TopLevelBackStack.Favourite, icon = Icons.Outlined.FavoriteBorder, selectedIcon = Icons.Filled.Favorite
    ), BottomNavItem(
        label = Res.string.settings, key = TopLevelBackStack.Settings, icon = Icons.Outlined.Settings, selectedIcon = Icons.Filled.Settings
    )
)



