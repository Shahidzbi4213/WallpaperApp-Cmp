package com.google.wallpaperapp

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.wallpaperapp.ui.routs.Routs
import com.google.wallpaperapp.ui.screens.splash.SplashScreen
import com.google.wallpaperapp.ui.theme.ScreenyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.app_logo

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
@Preview
fun App() {

    val navController = rememberNavController()


    ScreenyTheme(dynamicColor = true, darkTheme = isSystemInDarkTheme()) {

        Scaffold { innerPadding ->

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
                                .navigate(Routs.Home){
                                    popUpTo(Routs.Splash){
                                        inclusive = true
                                    }
                                }
                        })
                    }

                    composable<Routs.Home> {
                        Box {
                            Image(painter = painterResource(Res.drawable.app_logo), contentDescription = null)
                            Text("Welcome Home")
                        }
                    }

                }
            }
        }
    }

}