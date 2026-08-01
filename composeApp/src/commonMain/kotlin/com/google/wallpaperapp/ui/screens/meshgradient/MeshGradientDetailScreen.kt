package com.google.wallpaperapp.ui.screens.meshgradient

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.wallpaperapp.core.platform.PlatformType
import com.google.wallpaperapp.core.platform.ToastDurationType.SHORT
import com.google.wallpaperapp.core.platform.ToastManager
import com.google.wallpaperapp.core.platform.WallpaperManager
import com.google.wallpaperapp.core.platform.getPlatformType
import com.google.wallpaperapp.ui.components.ActionButtons
import com.google.wallpaperapp.ui.dialogs.WallpaperApplyDialog
import com.google.wallpaperapp.ui.theme.glass
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope

@Composable
fun MeshGradientDetailScreen(
    startIndex: Int,
    onBack: () -> Unit
) {
    var canShowDialog by remember { mutableStateOf(false) }
    var currentlyLoadedWallpaper by remember { mutableStateOf<ImageBitmap?>(null) }
    
    val pagerState = rememberPagerState(initialPage = startIndex) { randomPresets.size }
    val scope = rememberCoroutineScope()
    val toastManager = remember { ToastManager() }
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val wallpaperManager = remember { WallpaperManager() }

    Box(modifier = Modifier.fillMaxSize()) {
        // Main Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            key = { it }
        ) { page ->
            val preset = randomPresets[page]
            Canvas(modifier = Modifier.fillMaxSize().blur(50.dp)) {
                drawMeshGradient(preset)
            }
        }
        
        // Back Button
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
        
        // Action Buttons at bottom
        Box(modifier = Modifier.align(Alignment.BottomCenter).zIndex(90f)) {
            ActionButtons(
                isFavourite = false,
                onDownload = {
                    scope.launch {
                        toastManager.showToast("Cannot download generated gradient", SHORT)
                    }
                },
                onApply = {
                    scope.launch {
                        // Generate bitmap of current page
                        val preset = randomPresets[pagerState.currentPage]
                        val width = 1080
                        val height = 1920
                        val imageBitmap = ImageBitmap(width, height)
                        val canvas = ComposeCanvas(imageBitmap)
                        val drawScope = CanvasDrawScope()
                        
                        drawScope.draw(
                            density = density,
                            layoutDirection = layoutDirection,
                            canvas = canvas,
                            size = Size(width.toFloat(), height.toFloat())
                        ) {
                            drawMeshGradient(preset)
                        }
                        
                        currentlyLoadedWallpaper = imageBitmap
                        
                        if (getPlatformType() == PlatformType.IOS) {
                            wallpaperManager.applyWallpaper(imageBitmap, WallpaperType.SET_AS_BOTH)
                        } else {
                            canShowDialog = true
                        }
                    }
                },
                onFavourite = {
                    scope.launch {
                        toastManager.showToast("Cannot favorite generated gradient", SHORT)
                    }
                }
            )
        }
    }

    if (canShowDialog) {
        WallpaperApplyDialog(wallpaper = currentlyLoadedWallpaper) {
            canShowDialog = false
        }
    }
}
