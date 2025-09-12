package com.google.wallpaperapp.core.platform

import androidx.compose.ui.graphics.ImageBitmap

// commonMain
expect fun blurImage(
    image: ImageBitmap,
    radius: Float
): ImageBitmap
