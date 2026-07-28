package com.google.wallpaperapp.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.BackHandler
import com.google.wallpaperapp.ui.components.SplashProgressBar
import com.google.wallpaperapp.ui.theme.ScreenyGradient
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.auroraBackground
import com.google.wallpaperapp.ui.theme.eyebrowStyle
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.app_logo
import wallpaperapp.composeapp.generated.resources.app_name


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = koinViewModel(),
    onProgressFinish: () -> Unit
) {

    val progress by splashViewModel.progress.collectAsStateWithLifecycle()


    val splashTitle = remember {
        buildAnnotatedString {
            pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
            append("Screeny")
            pushStyle(SpanStyle(fontWeight = FontWeight.Light, fontSize = 24.sp))
            append(" Wallpaper")
        }
    }

    if (progress >= 1f) {
        onProgressFinish()
    }

    BackHandler(enable = false, onBack = {})

    Column(
        modifier = modifier
            .fillMaxSize()
            .auroraBackground()
            .consumeWindowInsets(WindowInsets(0.dp)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(resource = Res.drawable.app_logo),
            modifier = Modifier.size(100.dp),
            contentScale = ContentScale.Fit,
            contentDescription = stringResource(Res.string.app_name)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = splashTitle,
            style = TextStyle(brush = ScreenyGradient),
            fontFamily = getScreenyFontFamily()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "DRESS YOUR SCREEN IN LIGHT",
            style = eyebrowStyle(),
            color = TextLow
        )

        Spacer(modifier = Modifier.height(28.dp))


        SplashProgressBar(progress = { progress })


    }
}
