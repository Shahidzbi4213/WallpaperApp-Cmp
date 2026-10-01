package com.google.wallpaperapp.ui.desktop

import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.WallpaperApplyResult
import com.google.wallpaperapp.ui.screens.meshgradient.MeshPreset

actual suspend fun applyMeshPreset(preset: MeshPreset): WallpaperApplyResult {
    return WallpaperApplyResult.Success
}

actual suspend fun downloadMeshPreset(preset: MeshPreset): DownloadResult {
    return DownloadResult.Success("screeny-gradient-${preset.name}.png")
}
