package com.google.wallpaperapp.ui.screens.favourite

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.wallpaperapp.core.platform.BackHandler
import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.ToastDurationType.SHORT
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.WallpaperDownloader
import com.google.wallpaperapp.core.platform.WallpaperManager
import com.google.wallpaperapp.core.platform.getPlatformType
import com.google.wallpaperapp.domain.models.FavouriteWallpaper
import com.google.wallpaperapp.ui.components.ActionButtons
import com.google.wallpaperapp.ui.dialogs.WallpaperApplyDialog
import com.google.wallpaperapp.utils.WallpaperType
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
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


@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalTime::class)
@Composable
fun SharedTransitionScope.FavouriteDetailScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    wallpaper: ()-> FavouriteWallpaper,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    onBack: () -> Unit
) {


    val scope = rememberCoroutineScope()
    val downloadStarted = stringResource(Res.string.download_started)
    val downloadCompleted = stringResource(Res.string.download_completed)
    val downloadFailed = stringResource(Res.string.download_failed)
    val toastManager = remember { ToastManager() }
    var currentlyLoadedWallpaper by remember { mutableStateOf<ImageBitmap?>(null) }
    var canShowDialog by remember { mutableStateOf(false) }
    var isFavourite by remember { mutableStateOf(true) }


    BackHandler(true,onBack = onBack)


    Box(contentAlignment = Alignment.BottomCenter, modifier = modifier.fillMaxSize()) {



        CoilImage(
            imageModel = {wallpaper().wallpaper},
            imageOptions = ImageOptions(
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
            ),
            modifier = Modifier
                .sharedElement(
                    rememberSharedContentState(
                        key = "image-${wallpaper().wallpaper}"
                    ),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .fillMaxSize()
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null, modifier = Modifier
                .padding(start = 10.dp, top = 60.dp)
                .size(30.dp)
                .padding(5.dp)
                .clip(CircleShape)
                .align(Alignment.TopStart)
                .clickable { onBack() }
            , tint = Color.White)

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
                    val url = wallpaper().wallpaper
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
                favouriteViewModel.addOrRemoveFavourite(wallpaper())
                isFavourite = !isFavourite
            })
    }


    if (canShowDialog){
        WallpaperApplyDialog(wallpaper = currentlyLoadedWallpaper) {
            canShowDialog = false
        }
    }



}
