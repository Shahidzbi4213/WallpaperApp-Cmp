package com.google.wallpaperapp.ui.desktop

import com.google.wallpaperapp.core.platform.DownloadResult
import com.google.wallpaperapp.core.platform.WallpaperApplyResult
import com.google.wallpaperapp.ui.screens.meshgradient.MeshPreset

expect suspend fun applyMeshPreset(preset: MeshPreset): WallpaperApplyResult

expect suspend fun downloadMeshPreset(preset: MeshPreset): DownloadResult
