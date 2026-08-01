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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.google.wallpaperapp.ui.components.WallpaperItem

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
import com.google.wallpaperapp.ui.composables.collectAsLazyPagingItems
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

import com.google.wallpaperapp.ui.composables.LazyPagingItems

@OptIn(ExperimentalTime::class)
@Composable
fun WallpaperDetailScreen(
    pagedWallpapers: LazyPagingItems<Wallpaper>? = null,
    staticWallpapers: List<Wallpaper>? = null,
    clickedWallpaperId: Long,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    similarWallpapersViewModel: SimilarWallpapersViewModel = koinViewModel(),
    onBack: () -> Unit
) {

    val favouriteList by favouriteViewModel.getAllFavourites.collectAsStateWithLifecycle()
    var canShowDialog by remember { mutableStateOf(false) }

    val index by remember {
        mutableStateOf(
            pagedWallpapers?.itemSnapshotList?.items?.indexOfFirst {
                it.id == clickedWallpaperId
            } ?: staticWallpapers?.indexOfFirst {
                it.id == clickedWallpaperId
            } ?: -1
        )
    }

    val pagerState = rememberPagerState(initialPage = if (index != -1) index else 0) { 
        pagedWallpapers?.itemCount ?: staticWallpapers?.size ?: 0
    }


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

    val currentWallpaperObj = if (pagedWallpapers != null) {
        if (pagedWallpapers.itemCount > 0) pagedWallpapers[pagerState.currentPage] else null
    } else {
        staticWallpapers?.getOrNull(pagerState.currentPage)
    }

    LaunchedEffect(key1 = currentWallpaperObj, favouriteList) {
        if (currentWallpaperObj != null) {
            isFavourite = favouriteList.fastAny { it.wallpaper == currentWallpaperObj.portrait }
        }
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

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val screenHeight = maxHeight
            
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    Box(
                        contentAlignment = Alignment.BottomCenter, 
                        modifier = Modifier.fillMaxWidth().height(screenHeight)
                    ) {

                        val activeUrl = currentWallpaperObj?.portrait ?: ""
                        if (activeUrl.isNotEmpty()) {
                            BlurBg(activeUrl, currentlyLoaded = {imageBitmap ->
                                currentlyLoadedWallpaper = imageBitmap
                            })
                        }

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
                key = { 
                    pagedWallpapers?.peek(it)?.id ?: staticWallpapers?.getOrNull(it)?.id ?: it
                },
            ) { page ->

                val pageWallpaper = if (pagedWallpapers != null) {
                    pagedWallpapers[page]?.portrait ?: ""
                } else {
                    staticWallpapers?.getOrNull(page)?.portrait ?: ""
                }
                
                if (pageWallpaper.isNotEmpty()) {
                    SinglePageContent(
                        wallpaperUrl = pageWallpaper,
                        pagerState = pagerState,
                        page = page
                    )
                }

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
                        val url = currentWallpaperObj?.portrait ?: return@launch
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
                    val wallpaper = currentWallpaperObj
                    if (wallpaper != null) {
                        favouriteViewModel.addOrRemoveFavourite(wallpaper = wallpaper)
                    }
                })
                    }
                } // End of main Box item
                
                // Similar Wallpapers Section
                if (currentWallpaperObj != null) {
                    item {
                        Text(
                            text = "Similar Wallpapers",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp),
                            color = Color.White
                        )
                    }
                    
                    item {
                        
                        val similarWallpapers = similarWallpapersViewModel.similarWallpapers.collectAsLazyPagingItems()
                        
                        LaunchedEffect(currentWallpaperObj.alt) {
                            if (currentWallpaperObj.alt.isNotEmpty()) {
                                similarWallpapersViewModel.fetchSimilar(currentWallpaperObj.alt)
                            }
                        }
                        
                        val columns = 3
                        val itemCount = similarWallpapers.itemCount
                        val rowCount = if (itemCount == 0) 0 else (itemCount + columns - 1) / columns
                        
                        if (itemCount > 0) {
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                                for (rowIndex in 0 until rowCount) {
                                    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                        for (colIndex in 0 until columns) {
                                            val itemIndex = rowIndex * columns + colIndex
                                            Box(modifier = Modifier.weight(1f).padding(4.dp)) {
                                                if (itemIndex < itemCount) {
                                                    val wallpaper = similarWallpapers[itemIndex]
                                                    if (wallpaper != null) {
                                                        WallpaperItem(
                                                            modifier = Modifier.height(200.dp),
                                                            wallpaper = wallpaper.portrait,
                                                            onWallpaperClick = { 
                                                                // Navigate or expand functionality can go here
                                                            }
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (similarWallpapers.loadState.refresh is androidx.paging.LoadState.Loading) {
                            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color.White)
                            }
                        }
                    }
                }
            } // End of LazyColumn
        } // End of BoxWithConstraints
    }

    if (canShowDialog){
        WallpaperApplyDialog(wallpaper = currentlyLoadedWallpaper) {
            canShowDialog = false
        }
    }

}




