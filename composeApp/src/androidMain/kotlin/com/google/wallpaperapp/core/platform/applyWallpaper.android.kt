package com.google.wallpaperapp.core.platform

import android.app.WallpaperManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.service.wallpaper.WallpaperService
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.core.content.ContextCompat
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.io.IOException

actual class WallpaperManager actual constructor() : KoinComponent {

    private val context = getKoin().get<Context>()
    private val wallpaperManager by lazy {
        context.getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager
    }

    actual suspend fun applyWallpaper(image: ImageBitmap, type: WallpaperType): WallpaperApplyResult {
        return try {
            val flag = when (type) {
                WallpaperType.SET_AS_HOME_SCREEN -> WallpaperManager.FLAG_SYSTEM
                WallpaperType.SET_AS_LOCK_SCREEN -> WallpaperManager.FLAG_LOCK
                WallpaperType.SET_AS_BOTH -> WallpaperManager.FLAG_LOCK or WallpaperManager.FLAG_SYSTEM
            }

            // Convert the drawable to a bitmap and set it as wallpaper with the chosen flag
            wallpaperManager.setBitmap(image.asAndroidBitmap(), null, false, flag)

            WallpaperApplyResult.Success
        } catch (e: Exception) {
            Log.e("WallpaperManager", "applyWallpaper: ", e)
            WallpaperApplyResult.Failure(e.message)
        } catch (e: IOException) {
            Log.e("WallpaperManager", "applyWallpaper: ", e)
            WallpaperApplyResult.Failure(e.message)

        }
    }

}