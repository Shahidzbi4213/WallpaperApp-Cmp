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
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.categories
import wallpaperapp.composeapp.generated.resources.favourite
import wallpaperapp.composeapp.generated.resources.home
import wallpaperapp.composeapp.generated.resources.settings


@Stable
data class BottomNavRoutes(val name: StringResource, val route: Routs, val icon: ImageVector, val selectedIcon: ImageVector)

val bottomNavigationItems = listOf(
    BottomNavRoutes(
        name = Res.string.home, route = Routs.Home, icon = Icons.Outlined.Dashboard, selectedIcon = Icons.Filled.Dashboard
    ), BottomNavRoutes(
        name = Res.string.categories, route = Routs.Categories, icon = Icons.Outlined.Category, selectedIcon = Icons.Filled.Category
    ), BottomNavRoutes(
        name = Res.string.favourite, route = Routs.Favourite, icon = Icons.Outlined.FavoriteBorder, selectedIcon = Icons.Filled.Favorite
    ), BottomNavRoutes(
        name = Res.string.settings, route = Routs.Settings, icon = Icons.Outlined.Settings, selectedIcon = Icons.Filled.Settings
    )
)

