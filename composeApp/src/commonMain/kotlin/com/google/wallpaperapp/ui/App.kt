package com.google.wallpaperapp.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.google.wallpaperapp.core.platform.exitApp
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.BottomNavigationBar
import com.google.wallpaperapp.ui.components.TopBar
import com.google.wallpaperapp.ui.composables.ManageBarVisibility
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.screens.category.CategoryDetailScreen
import com.google.wallpaperapp.ui.screens.category.CategoryScreen
import com.google.wallpaperapp.ui.screens.category.CategoryViewModel
import com.google.wallpaperapp.ui.screens.detail.WallpaperDetailScreen
import com.google.wallpaperapp.ui.screens.favourite.FavouriteDetailScreen
import com.google.wallpaperapp.ui.screens.favourite.FavouriteScreen
import com.google.wallpaperapp.ui.screens.home.HomeScreen
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import com.google.wallpaperapp.ui.screens.languages.LanguageScreen
import com.google.wallpaperapp.ui.screens.search.SearchedWallpaperScreen
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import com.google.wallpaperapp.ui.screens.settings.SettingsScreen
import com.google.wallpaperapp.ui.screens.splash.SplashScreen
import com.google.wallpaperapp.ui.theme.ScreenyTheme
import com.google.wallpaperapp.utils.AppMode
import com.google.wallpaperapp.utils.isDarkMode
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    modifier: Modifier = Modifier,
) {
    val homeScreenViewModel = koinViewModel<HomeScreenViewModel>()
    val wallpapers = homeScreenViewModel.wallpapers.collectAsLazyPagingItems()

    // Using a single mutableStateListOf for Navigation 3 basics
    val backStack = remember { mutableStateListOf<Any>(Routs.Splash) }
    val stackEntry = backStack.lastOrNull() as? Routs

    var canShowBottomBar by rememberSaveable { mutableStateOf(false) }
    var canShowTopBar by rememberSaveable { mutableStateOf(false) }

    val categoryViewModel = koinViewModel<CategoryViewModel>()
    val wallpapersByCategory = categoryViewModel.wallpapers.collectAsLazyPagingItems()

    var isCategory by rememberSaveable { mutableStateOf(false) }
    var isSearch by rememberSaveable { mutableStateOf(false) }
    var searchedWallpapers by remember { mutableStateOf(emptyList<Wallpaper>()) }

    val settingViewModel = koinViewModel<SettingViewModel>()
    val userPreferences by settingViewModel.userPreference.collectAsStateWithLifecycle()
    val direction = remember(userPreferences.languageCode) {
        if (isRtlLanguage(userPreferences.languageCode))
            LayoutDirection.Rtl
        else LayoutDirection.Ltr
    }

    ManageBarVisibility(
        currentEntry = { stackEntry },
        showTopBar = { canShowTopBar = it },
        showBottomBar = { canShowBottomBar = it },
    )
    ScreenyTheme(dynamicColor = userPreferences.shouldShowDynamicColor, darkTheme = isDarkMode(appMode = AppMode.getModeById(userPreferences.appMode))) {

        CompositionLocalProvider(LocalLayoutDirection provides direction) {
            Scaffold(
                bottomBar = {
                    if (canShowBottomBar) {
                        BottomNavigationBar(
                            selectedRoute = stackEntry ?: Routs.Home,
                            onTabClick = { tabRoute ->
                                if (backStack.contains(tabRoute)) {
                                    val index = backStack.indexOf(tabRoute)
                                    while (backStack.size > index + 1) {
                                        backStack.removeAt(backStack.size - 1)
                                    }
                                } else {
                                    // Pop down to Home, then push the new tab
                                    while (backStack.size > 1 && backStack[0] == Routs.Home) {
                                        backStack.removeAt(backStack.size - 1)
                                    }
                                    if (backStack.isEmpty()) {
                                        backStack.add(Routs.Home)
                                    } else if (backStack.first() != Routs.Home) {
                                        backStack[0] = Routs.Home
                                        while (backStack.size > 1) {
                                            backStack.removeAt(backStack.size - 1)
                                        }
                                    }
                                    if (tabRoute != Routs.Home) {
                                        backStack.add(tabRoute)
                                    }
                                }
                            }
                        )
                    }
                },
                topBar = {
                    if (canShowTopBar) {
                        TopBar(title = "Screeny") {
                            backStack.add(Routs.SearchedWallpaper)
                        }
                    }
                },
                modifier = modifier.fillMaxSize(),
                contentWindowInsets = WindowInsets(0.dp),
            )
            { innerPadding ->

                SharedTransitionLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    NavDisplay(
                        backStack = backStack,
                        modifier = Modifier.fillMaxSize(),
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = { key ->
                            when (key) {
                                is Routs.Splash -> NavEntry(key) {
                                    SplashScreen(onProgressFinish = {
                                        backStack.add(Routs.Home)
                                        backStack.remove(Routs.Splash)
                                    })
                                }

                                is Routs.Home -> NavEntry(key) {
                                    HomeScreen(
                                        wallpapers,
                                        onBack = {
                                            exitApp()
                                        },
                                        onWallpaperClick = { wallpaper ->
                                            backStack.add(Routs.WallpaperDetail(wallpaper.id))
                                        }
                                    )
                                }

                                is Routs.WallpaperDetail -> NavEntry(key) {
                                    WallpaperDetailScreen(
                                        wallpapers = if (isSearch) {
                                            searchedWallpapers
                                        } else {
                                            if (isCategory) wallpapersByCategory.itemSnapshotList.items else
                                                wallpapers.itemSnapshotList.items
                                        },
                                        clickedWallpaperId = key.wallpaperId,
                                        onBack = {
                                            isSearch = false
                                            searchedWallpapers = emptyList()
                                            isCategory = false
                                            backStack.removeLastOrNull()
                                        }
                                    )
                                }

                                is Routs.Categories -> NavEntry(key) {
                                    CategoryScreen(onCategoryClick = { name ->
                                        backStack.add(Routs.CategoryDetail(name))
                                    })
                                }

                                is Routs.CategoryDetail -> NavEntry(key) {
                                    val query = key.query
                                    LaunchedEffect(query) {
                                        categoryViewModel.updateQuery(query)
                                    }

                                    CategoryDetailScreen(
                                        query,
                                        wallpapersByCategory,
                                        onWallpaperClick = { wallpaper ->
                                            isCategory = true
                                            backStack.add(Routs.WallpaperDetail(wallpaper.id))
                                        },
                                        onBackClick = {
                                            isCategory = false
                                            backStack.removeLastOrNull()
                                            categoryViewModel.updateQuery("")
                                        }
                                    )
                                }

                                is Routs.Favourite -> NavEntry(key) {
                                    FavouriteScreen(
                                        onExplore = {
                                            backStack.remove(Routs.Favourite)
                                            if (!backStack.contains(Routs.Home)) {
                                                backStack.add(0, Routs.Home)
                                            }
                                        },
                                        onWallpaperClick = { id, wallpaper ->
                                            backStack.add(
                                                Routs.FavouriteDetail(
                                                    wallpaperId = id,
                                                    wallpaperUrl = wallpaper
                                                )
                                            )
                                        }
                                    )
                                }

                                is Routs.FavouriteDetail -> NavEntry(key) {
                                    val id = key.wallpaperId
                                    val wallpaperUrl = key.wallpaperUrl
                                    FavouriteDetailScreen(
                                        wallpaper = {
                                            FavouriteWallpaper(
                                                id = id,
                                                wallpaper = wallpaperUrl
                                            )
                                        },
                                        onBack = {
                                            backStack.removeLastOrNull()
                                        }
                                    )
                                }

                                is Routs.Settings -> NavEntry(key) {
                                    SettingsScreen(navigateToLanguage = {
                                        backStack.add(Routs.Language)
                                    })
                                }

                                is Routs.Language -> NavEntry(key) {
                                    LanguageScreen(goBack = {
                                        backStack.removeLastOrNull()
                                    })
                                }

                                is Routs.SearchedWallpaper -> NavEntry(key) {
                                    SearchedWallpaperScreen(
                                        onNavigateBack = {
                                            backStack.removeLastOrNull()
                                        },
                                        onWallpaperClick = { wallpaper, wallpapers ->
                                            isSearch = true
                                            searchedWallpapers = wallpapers
                                            backStack.add(Routs.WallpaperDetail(wallpaper.id))
                                        }
                                    )
                                }
                                else -> error("Unknown key: $key")
                            }
                        }
                    )
                }
            }
        }
    }
}

private fun isRtlLanguage(languageCode: String): Boolean {
    return listOf("ar", "ur", "he", "fa").contains(languageCode)
}