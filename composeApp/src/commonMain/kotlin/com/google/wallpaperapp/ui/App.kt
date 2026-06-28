package com.google.wallpaperapp.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.google.wallpaperapp.core.platform.exitApp
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.composables.ManageBarVisibility
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.screens.category.CategoryDetailScreen
import com.google.wallpaperapp.ui.screens.category.CategoryViewModel
import com.google.wallpaperapp.ui.screens.detail.WallpaperDetailScreen
import com.google.wallpaperapp.ui.screens.favourite.FavouriteDetailScreen
import com.google.wallpaperapp.ui.screens.home.HomeScreen
import com.google.wallpaperapp.ui.screens.home.HomeScreenViewModel
import com.google.wallpaperapp.ui.screens.languages.LanguageScreen
import com.google.wallpaperapp.ui.screens.main.MainNavigationAction
import com.google.wallpaperapp.ui.screens.main.MainScreen
import com.google.wallpaperapp.ui.screens.search.SearchedWallpaperScreen
import com.google.wallpaperapp.ui.screens.settings.SettingViewModel
import com.google.wallpaperapp.ui.screens.splash.SplashScreen
import com.google.wallpaperapp.ui.theme.ScreenyTheme
import com.google.wallpaperapp.utils.AppMode
import com.google.wallpaperapp.utils.isDarkMode
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    modifier: Modifier = Modifier,
    homeScreenViewModel: HomeScreenViewModel = koinViewModel(),
    settingViewModel: SettingViewModel = koinViewModel(),
    categoryViewModel: CategoryViewModel = koinViewModel()
) {
    val configuration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                subclass(Routs.Splash::class, Routs.Splash.serializer())
                subclass(Routs.MainScreen::class, Routs.MainScreen.serializer())
                subclass(Routs.CategoryDetail::class, Routs.CategoryDetail.serializer())
                subclass(Routs.SearchedWallpaper::class, Routs.SearchedWallpaper.serializer())
                subclass(Routs.WallpaperDetail::class, Routs.WallpaperDetail.serializer())
                subclass(Routs.FavouriteDetail::class, Routs.FavouriteDetail.serializer())
                subclass(Routs.Language::class, Routs.Language.serializer())
            }
        }
    }
    val backStack = rememberNavBackStack(configuration = configuration, Routs.Splash)

    val wallpapersByCategory = categoryViewModel.wallpapers.collectAsLazyPagingItems()
    var isCategory by rememberSaveable { mutableStateOf(false) }
    var isSearch by rememberSaveable { mutableStateOf(false) }
    var searchedWallpapers by remember { mutableStateOf(emptyList<Wallpaper>()) }

    val wallpapers = homeScreenViewModel.wallpapers.collectAsLazyPagingItems()
    val userPreferences by settingViewModel.userPreference.collectAsStateWithLifecycle()


    ScreenyTheme(
        dynamicColor = userPreferences.shouldShowDynamicColor,
        darkTheme = isDarkMode(appMode = AppMode.getModeById(userPreferences.appMode))
    ) {

        SharedTransitionLayout {

            NavDisplay(
                backStack = backStack,
                sharedTransitionScope = this,
                modifier = modifier.fillMaxSize(),
                onBack = { backStack.removeLastOrNull() },
                entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
                ),
                entryProvider = entryProvider {

                    entry<Routs.Splash> {
                        SplashScreen(onProgressFinish = {
                            backStack.add(Routs.MainScreen)
                            backStack.remove(Routs.Splash)
                        })
                    }

                    entry<Routs.MainScreen> {
                        MainScreen(
                            wallpapers = wallpapers,
                            onNavigate = { action ->
                                when (action) {
                                    is MainNavigationAction.ToCategoryDetail -> backStack.add(Routs.CategoryDetail(action.category))
                                    MainNavigationAction.ToLanguage -> backStack.add(Routs.Language)
                                    MainNavigationAction.ToSearch -> backStack.add(Routs.SearchedWallpaper)
                                    is MainNavigationAction.ToFavouriteDetail -> backStack.add(Routs.FavouriteDetail(action.id, action.url))
                                    is MainNavigationAction.ToWallpaperDetail -> backStack.add(Routs.WallpaperDetail(action.id))
                                }
                            })
                    }

                    entry<Routs.WallpaperDetail> {
                        WallpaperDetailScreen(
                            wallpapers = if (isSearch) {
                                searchedWallpapers
                            } else {
                                if (isCategory) wallpapersByCategory.itemSnapshotList.items else
                                    wallpapers.itemSnapshotList.items
                            },
                            clickedWallpaperId = it.wallpaperId,
                            onBack = {
                                isSearch = false
                                searchedWallpapers = emptyList()
                                isCategory = false
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<Routs.CategoryDetail> {
                        val query = it.categoryName
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

                    entry<Routs.FavouriteDetail> {
                        FavouriteDetailScreen(
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                            wallpaper = { FavouriteWallpaper(id = it.wallpaperId, it.wallpaperUrl) },
                            onBack = {
                                backStack.removeLastOrNull()
                            },
                        )
                    }

                    entry<Routs.Language> {
                        LanguageScreen(goBack = {
                            backStack.removeLastOrNull()
                        })
                    }

                    entry<Routs.SearchedWallpaper> {
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
                }
            )
        }
    }
}