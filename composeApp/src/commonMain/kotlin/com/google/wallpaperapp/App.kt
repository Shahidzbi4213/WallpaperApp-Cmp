package com.google.wallpaperapp

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.wallpaperapp.core.platform.exitApp
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.BottomNavigationBar
import com.google.wallpaperapp.ui.components.TopBar
import com.google.wallpaperapp.ui.composables.LazyPagingItems
import com.google.wallpaperapp.ui.composables.ManageBarVisibility
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
import com.google.wallpaperapp.ui.composables.titleMapper
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.screens.category.CategoryDetailScreen
import com.google.wallpaperapp.ui.screens.category.CategoryScreen
import com.google.wallpaperapp.ui.screens.category.CategoryViewModel
import com.google.wallpaperapp.ui.screens.home.HomeScreen
import com.google.wallpaperapp.ui.screens.splash.SplashScreen
import com.google.wallpaperapp.ui.theme.ScreenyTheme
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun App(
    wallpapers: LazyPagingItems<Wallpaper>,
    modifier: Modifier = Modifier,
) {

    val navController = rememberNavController()
    var canShowBottomBar by rememberSaveable { mutableStateOf(false) }
    var canShowTopBar by rememberSaveable { mutableStateOf(false) }
    val stackEntry by navController.currentBackStackEntryAsState()

    val categoryViewModel = koinViewModel<CategoryViewModel>()
    val wallpapersByCategory = categoryViewModel.wallpapers.collectAsLazyPagingItems()



    ManageBarVisibility(
        currentEntry = { stackEntry },
        showTopBar = { canShowTopBar = it },
        showBottomBar = { canShowBottomBar = it },
    )
    ScreenyTheme(dynamicColor = true, darkTheme = isSystemInDarkTheme()) {

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
                            onWallpaperClick = {}
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

                            },
                            onBackClick = {
                                navController.navigateUp()
                                categoryViewModel.updateQuery(null)
                            })
                    }

                }
            }
        }
    }

}