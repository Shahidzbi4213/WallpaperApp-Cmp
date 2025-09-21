package com.google.wallpaperapp.ui

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.wallpaperapp.core.platform.exitApp
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.BottomNavigationBar
import com.google.wallpaperapp.ui.components.TopBar
import com.google.wallpaperapp.ui.composables.ManageBarVisibility
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.composables.titleMapper
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
import com.google.wallpaperapp.ui.screens.search.SearchViewModel
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
    val navController = rememberNavController()
    var canShowBottomBar by rememberSaveable { mutableStateOf(false) }
    var canShowTopBar by rememberSaveable { mutableStateOf(false) }
    val stackEntry by navController.currentBackStackEntryAsState()

    val categoryViewModel = koinViewModel<CategoryViewModel>()
    val wallpapersByCategory = categoryViewModel.wallpapers.collectAsLazyPagingItems()

    var isCategory by rememberSaveable { mutableStateOf(false) }
    var isSearch by rememberSaveable { mutableStateOf(false) }
    var searchedWallpapers by remember { mutableStateOf<List<Wallpaper>>(emptyList<Wallpaper>())}


    val settingViewModel = koinViewModel<SettingViewModel>()
    val userPreferences by settingViewModel.userPreference.collectAsStateWithLifecycle()


    ManageBarVisibility(
        currentEntry = { stackEntry },
        showTopBar = { canShowTopBar = it },
        showBottomBar = { canShowBottomBar = it },
    )
    ScreenyTheme(dynamicColor = userPreferences.shouldShowDynamicColor, darkTheme = isDarkMode(appMode = AppMode.getModeById(userPreferences.appMode))) {

        Scaffold(
            bottomBar = { if (canShowBottomBar) BottomNavigationBar(navController) },
            topBar = {
                if (canShowTopBar) {
                    val title = titleMapper(stackEntry?.destination?.route?.substringAfterLast("."))
                    TopBar(title = title) { navController.navigate(Routs.SearchedWallpaper) }
                }
            }, modifier = modifier
                .fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp)
        ) { innerPadding ->

            SharedTransitionLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController, startDestination = Routs.Splash, modifier = Modifier.fillMaxSize()
                ) {

                    composable<Routs.Splash> {
                        SplashScreen(onProgressFinish = {
                            navController
                                .navigate(Routs.Home) {
                                    popUpTo(Routs.Splash) {
                                        inclusive = true
                                    }
                                }
                        })
                    }

                    composable<Routs.Home> {
                        HomeScreen(
                            wallpapers,
                            onBack = {
                                exitApp()
                            },
                            onWallpaperClick = { wallpaper ->
                                navController.navigate(Routs.WallpaperDetail(wallpaper.id))
                            }
                        )
                    }

                    composable<Routs.WallpaperDetail> {
                        val id = it.toRoute<Routs.WallpaperDetail>().wallpaperId
                        WallpaperDetailScreen(
                            wallpapers = if (isSearch){
                                searchedWallpapers
                            }else{
                                if (isCategory) wallpapersByCategory.itemSnapshotList.items else
                                    wallpapers.itemSnapshotList.items
                            },
                            clickedWallpaperId = id,
                            onBack = {
                                isSearch = false
                                searchedWallpapers = emptyList()
                                isCategory = false
                                navController.navigateUp()
                            }

                        )
                    }

                    composable<Routs.Categories> {
                        CategoryScreen(onCategoryClick = { name ->
                            navController.navigate(Routs.CategoryDetail(name))
                        })
                    }

                    composable<Routs.CategoryDetail> { backStack ->
                        val query = backStack.toRoute<Routs.CategoryDetail>().query
                        categoryViewModel.updateQuery(query)

                        CategoryDetailScreen(
                            query,
                            wallpapersByCategory, onWallpaperClick = { wallpaper ->
                                isCategory = true
                                navController.navigate(Routs.WallpaperDetail(wallpaper.id))
                            },
                            onBackClick = {
                                isCategory = false
                                navController.navigateUp()
                                categoryViewModel.updateQuery("")
                            })
                    }

                    composable<Routs.Favourite> {
                        FavouriteScreen(
                            animatedVisibilityScope = this,
                            onExplore = {
                                navController.navigate(Routs.Home)
                            },
                            onWallpaperClick = { id, wallpaper ->
                                navController.navigate(
                                    Routs.FavouriteDetail(
                                        wallpaperId = id,
                                        wallpaperUrl = wallpaper
                                    )
                                )

                            })
                    }

                    composable<Routs.FavouriteDetail> { backstack ->
                        val id = backstack.toRoute<Routs.FavouriteDetail>().wallpaperId
                        val wallpaperUrl = backstack.toRoute<Routs.FavouriteDetail>().wallpaperUrl
                        FavouriteDetailScreen(
                            animatedVisibilityScope = this,
                            wallpaper = {
                                FavouriteWallpaper(
                                    id = id,
                                    wallpaper = wallpaperUrl
                                )
                            },
                            onBack = {
                                navController.navigateUp()
                            }
                        )

                    }

                    composable<Routs.Settings> {
                        SettingsScreen(navigateToLanguage = {
                            navController.navigate(Routs.Language)
                        })
                    }

                    composable<Routs.Language> {
                        LanguageScreen(goBack = {

                            navController.navigateUp()
                        })
                    }

                    composable<Routs.SearchedWallpaper> {
                        SearchedWallpaperScreen(
                            onNavigateBack = {
                                navController.navigate(Routs.Home) {
                                    popUpTo(Routs.SearchedWallpaper) {
                                        inclusive = true
                                    }
                                }
                            }, onWallpaperClick = { wallpaper, wallpapers ->
                                isSearch = true
                                searchedWallpapers = wallpapers
                                navController.navigate(Routs.WallpaperDetail(wallpaper.id))
                            })
                    }

                }
            }
        }
    }

}