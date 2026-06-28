package com.google.wallpaperapp.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.BottomNavigationBar
import com.google.wallpaperapp.ui.components.TopBar
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import com.google.wallpaperapp.ui.screens.category.CategoryScreen
import com.google.wallpaperapp.ui.screens.favourite.FavouriteScreen
import com.google.wallpaperapp.ui.screens.home.HomeScreen
import com.google.wallpaperapp.ui.screens.settings.SettingsScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    wallpapers: LazyPagingItems<Wallpaper>,
    onNavigate: (MainNavigationAction) -> Unit
) {

    val configurationBottomNav = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(TopLevelBackStack.Home::class, TopLevelBackStack.Home.serializer())
                subclass(TopLevelBackStack.Categories::class, TopLevelBackStack.Categories.serializer())
                subclass(TopLevelBackStack.Favourite::class, TopLevelBackStack.Favourite.serializer())
                subclass(TopLevelBackStack.Settings::class, TopLevelBackStack.Settings.serializer())
            }
        }
    }


    val bottomNavBackStack = rememberNavBackStack(configuration = configurationBottomNav, TopLevelBackStack.Home)
    var selectedBottomNavItem by rememberSaveable(stateSaver = TopLevelBackStack.Saver) {
        mutableStateOf<TopLevelBackStack>(TopLevelBackStack.Home)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(title = "Screeny", isHome = true, onClick = { onNavigate(MainNavigationAction.ToSearch) })
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selectedBottomNavItem,
                onTabClick = { key ->
                    bottomNavBackStack.add(key)
                    selectedBottomNavItem = key
                }
            )
        }
    ) { innerPadding ->

        NavDisplay(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            backStack = bottomNavBackStack,
            onBack = { bottomNavBackStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {

                entry<TopLevelBackStack.Home> {
                    HomeScreen(
                        wallpapers,
                        onWallpaperClick = { wallpaper ->
                            onNavigate(MainNavigationAction.ToWallpaperDetail(wallpaper.id, wallpaper.portrait))
                        }
                    )
                }

                entry<TopLevelBackStack.Categories> {
                    CategoryScreen(onCategoryClick = { onNavigate(MainNavigationAction.ToCategoryDetail(it)) })
                }

                entry<TopLevelBackStack.Favourite> {
                    FavouriteScreen(
                        onExplore = {
                            bottomNavBackStack.add(TopLevelBackStack.Home)
                        },
                        onWallpaperClick = { id, url ->
                            onNavigate(MainNavigationAction.ToWallpaperDetail(id, url))


                        }
                    )
                }

                entry<TopLevelBackStack.Settings> {
                    SettingsScreen(navigateToLanguage = {
                        onNavigate(MainNavigationAction.ToLanguage)
                    })
                }

            }
        )

    }

}