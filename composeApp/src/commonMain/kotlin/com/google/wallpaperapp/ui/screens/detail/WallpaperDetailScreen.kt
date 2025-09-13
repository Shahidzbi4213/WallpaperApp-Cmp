package com.google.wallpaperapp.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.ImageLoader
import coil3.request.ImageRequest
import com.google.wallpaperapp.core.platform.AppLogger
import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.ToastDurationType
import com.google.wallpaperapp.core.platform.ToastDurationType.SHORT
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.WallpaperDownloader
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.ActionButtons
import com.google.wallpaperapp.ui.components.BlurBg
import com.google.wallpaperapp.ui.components.SinglePageContent
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.download_completed
import wallpaperapp.composeapp.generated.resources.download_failed
import wallpaperapp.composeapp.generated.resources.download_started
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun WallpaperDetailScreen(
    wallpapers: List<Wallpaper>,
    clickedWallpaperId: Long,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    onBack: () -> Unit
) {

    val favouriteList by favouriteViewModel.getAllFavourites.collectAsStateWithLifecycle()
    var canShowDialog by remember { mutableStateOf(false) }

    val index by remember {
        mutableStateOf(
            wallpapers.indexOfFirst {
                it.id == clickedWallpaperId
            }
        )
    }

    val pagerState = rememberPagerState(initialPage = if (index != -1) index else 0) { wallpapers.size }


    var canShowList by remember { mutableStateOf(false) }
    var isFavourite by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val downloadStarted = stringResource(Res.string.download_started)
    val downloadCompleted = stringResource(Res.string.download_completed)
    val downloadFailed = stringResource(Res.string.download_failed)
    val toastManager = remember { ToastManager() }

    LaunchedEffect(key1 = canShowList) {
        delay(100)
        canShowList = true
    }

    LaunchedEffect(key1 = pagerState.currentPage, favouriteList) {
        isFavourite = favouriteList.fastAny { it.wallpaper == wallpapers[pagerState.currentPage].portrait }
    }


    if (!canShowList) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                modifier = Modifier.size(40.dp),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
        }
    }

    AnimatedVisibility(
        visible = canShowList, modifier = Modifier.fillMaxSize()
    ) {

        Box(
            contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()

        ) {

            BlurBg(wallpapers[pagerState.currentPage].portrait)

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null, modifier = Modifier
                    .padding(start = 10.dp, top = 60.dp)
                    .size(30.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .align(Alignment.TopStart)
                    .clickable { onBack() }
                    .zIndex(90f), tint = Color.White)


            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                beyondViewportPageCount = 0,
                key = { wallpapers[it].id },
            ) { page ->

                val currentWallpaper = wallpapers[page].portrait
                SinglePageContent(
                    wallpaperUrl = currentWallpaper,
                    pagerState = pagerState,
                    page = page
                )

            }

            ActionButtons(
                isFavourite = isFavourite,
                onDownload = {
                    scope.launch(Dispatchers.IO) {

                        withContext(Dispatchers.Main) {
                            toastManager.showToast(
                                downloadStarted,
                                SHORT
                            )
                        }
                        val url = wallpapers[pagerState.currentPage].portrait
                        val fileName = "${Clock.System.now().toEpochMilliseconds()}.jpeg"
                        val result = WallpaperDownloader().downloadWallpaper(url, fileName)
                        when (result) {
                            is DownloadResult.Failure -> {
                                withContext(Dispatchers.Main) {
                                    toastManager.showToast(
                                        downloadFailed,
                                        SHORT
                                    )
                                }
                            }

                            is DownloadResult.Success -> {
                                withContext(Dispatchers.Main) {
                                    toastManager.showToast(
                                        downloadCompleted,
                                        SHORT
                                    )
                                }
                            }
                        }
                    }
                },
                onApply = {
                    canShowDialog = true
                }, onFavourite = {
                    val wallpaper = wallpapers[pagerState.currentPage]
                    favouriteViewModel.addOrRemoveFavourite(wallpaper = wallpaper)
                })
        }
    }

}




