package com.google.wallpaperapp.ui.screens.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastAny
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.ToastDurationType.SHORT
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.WallpaperDownloader
import com.google.wallpaperapp.core.platform.WallpaperManager
import com.google.wallpaperapp.core.platform.getPlatformType
import com.google.wallpaperapp.domain.models.Wallpaper
import com.google.wallpaperapp.ui.components.ActionButtons
import com.google.wallpaperapp.ui.components.BlurBg
import com.google.wallpaperapp.ui.components.SinglePageContent
import com.google.wallpaperapp.ui.dialogs.WallpaperApplyDialog
import com.google.wallpaperapp.ui.screens.favourite.FavouriteViewModel
import com.google.wallpaperapp.ui.theme.glass
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.download_completed
import wallpaperapp.composeapp.generated.resources.download_failed
import wallpaperapp.composeapp.generated.resources.download_started
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems

@OptIn(ExperimentalTime::class)
@Composable
fun WallpaperDetailScreen(
    clickedWallpaper: Wallpaper,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    similarWallpapersViewModel: SimilarWallpapersViewModel = koinViewModel(),
    onBack: () -> Unit
) {

    val favouriteList by favouriteViewModel.getAllFavourites.collectAsStateWithLifecycle()
    var canShowDialog by remember { mutableStateOf(false) }

    val similarWallpapers = similarWallpapersViewModel.similarWallpapers.collectAsLazyPagingItems()

    LaunchedEffect(clickedWallpaper.alt) {
        similarWallpapersViewModel.fetchSimilar(clickedWallpaper.alt)
    }

    val pagerState = rememberPagerState(initialPage = 0) { similarWallpapers.itemCount + 1 }


    var canShowList by remember { mutableStateOf(false) }
    var isFavourite by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val downloadStarted = stringResource(Res.string.download_started)
    val downloadCompleted = stringResource(Res.string.download_completed)
    val downloadFailed = stringResource(Res.string.download_failed)
    val toastManager = remember { ToastManager() }
    var currentlyLoadedWallpaper by remember { mutableStateOf<ImageBitmap?>(null) }

    LaunchedEffect(key1 = canShowList) {
        delay(100)
        canShowList = true
    }

    val currentActiveWallpaper = if (pagerState.currentPage == 0) clickedWallpaper else {
        similarWallpapers[pagerState.currentPage - 1] ?: clickedWallpaper
    }

    LaunchedEffect(key1 = currentActiveWallpaper, favouriteList) {
        isFavourite = favouriteList.fastAny { it.wallpaper == currentActiveWallpaper.portrait }
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

            BlurBg(currentActiveWallpaper.portrait, currentlyLoaded = {imageBitmap ->
                currentlyLoadedWallpaper = imageBitmap
            })

            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 56.dp)
                    .size(44.dp)
                    .clip(CircleShape)
                    .glass(CircleShape, strong = true)
                    .clickable { onBack() }
                    .zIndex(90f),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }


            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp),
                beyondViewportPageCount = 0,
                key = { if (it == 0) clickedWallpaper.id else similarWallpapers.peek(it - 1)?.id ?: it },
            ) { page ->

                val pageWallpaper = if (page == 0) clickedWallpaper else {
                    similarWallpapers[page - 1] ?: clickedWallpaper
                }
                SinglePageContent(
                    wallpaperUrl = pageWallpaper.portrait,
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
                        val url = currentActiveWallpaper.portrait
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
                    scope.launch {
                        if (currentlyLoadedWallpaper == null) scope.cancel()


                        if (getPlatformType() == PlatformType.IOS){
                            WallpaperManager()
                                .applyWallpaper(currentlyLoadedWallpaper!!, WallpaperType.SET_AS_BOTH)
                        }else{
                            canShowDialog = true

                        }
                    }
                }, onFavourite = {
                    val wallpaper = currentActiveWallpaper
                    favouriteViewModel.addOrRemoveFavourite(wallpaper = wallpaper)
                })
        }
    }

    if (canShowDialog){
        WallpaperApplyDialog(wallpaper = currentlyLoadedWallpaper) {
            canShowDialog = false
        }
    }

}




