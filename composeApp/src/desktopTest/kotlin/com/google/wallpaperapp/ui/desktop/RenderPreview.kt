package com.google.wallpaperapp.ui.desktop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.wallpaperapp.ui.theme.auroraBackground
import androidx.compose.ui.ImageComposeScene
import androidx.compose.ui.unit.Density
import org.jetbrains.skia.EncodedImageFormat
import java.io.File

/**
 * Renders a composable straight to a PNG with no window and no display server, so desktop layout
 * can be eyeballed and diffed without a running app.
 */
fun renderToPng(
    outputPath: String,
    width: Int = 1440,
    height: Int = 900,
    content: @Composable () -> Unit
): File {
    val scene = ImageComposeScene(
        width = width,
        height = height,
        density = Density(1f),
        // Screens are dark-on-dark; without the app backdrop a render lies about contrast.
        content = { Box(Modifier.fillMaxSize().auroraBackground()) { content() } }
    )
    return try {
        val image = scene.render()
        val data = image.encodeToData(EncodedImageFormat.PNG) ?: error("PNG encode failed")
        File(outputPath).apply {
            parentFile?.mkdirs()
            writeBytes(data.bytes)
        }
    } finally {
        scene.close()
    }
}
