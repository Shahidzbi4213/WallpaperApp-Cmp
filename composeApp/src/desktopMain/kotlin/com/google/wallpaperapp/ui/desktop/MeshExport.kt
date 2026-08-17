package com.google.wallpaperapp.ui.desktop

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.google.wallpaperapp.core.platform.appDataDir
import com.google.wallpaperapp.ui.screens.meshgradient.MeshPreset
import com.google.wallpaperapp.ui.screens.meshgradient.drawMeshGradient
import org.jetbrains.skia.EncodedImageFormat
import org.jetbrains.skia.Image
import java.awt.GraphicsEnvironment
import java.io.File

/**
 * The mobile mesh export bakes in 1080x1920 -- a portrait phone. A desktop wallpaper should match
 * the display it is going on, so the size comes from the actual screen.
 */
fun primaryDisplaySize(): Pair<Int, Int> = runCatching {
    val mode = GraphicsEnvironment.getLocalGraphicsEnvironment()
        .defaultScreenDevice.displayMode
    mode.width to mode.height
}.getOrDefault(2560 to 1440).let { (w, h) ->
    // Guard against a headless or bogus report; never export something unusable.
    if (w < 640 || h < 480) 2560 to 1440 else w to h
}

/** Renders a preset at display resolution and writes it out as a PNG. */
fun exportMeshPreset(preset: MeshPreset, target: File): File {
    val (width, height) = primaryDisplaySize()

    val bitmap = ImageBitmap(width, height)
    CanvasDrawScope().draw(
        density = Density(1f),
        layoutDirection = LayoutDirection.Ltr,
        canvas = Canvas(bitmap),
        size = Size(width.toFloat(), height.toFloat())
    ) {
        drawMeshGradient(preset)
    }

    val data = Image.makeFromBitmap(bitmap.asSkiaBitmap())
        .encodeToData(EncodedImageFormat.PNG)
        ?: error("Could not encode the gradient as PNG")

    return target.apply {
        parentFile?.mkdirs()
        writeBytes(data.bytes)
    }
}

fun meshExportFile(preset: MeshPreset): File =
    File(appDataDir(), "gradients/screeny-gradient-${preset.name}.png")
