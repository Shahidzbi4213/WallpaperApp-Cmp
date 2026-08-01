package com.google.wallpaperapp.ui.screens.meshgradient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.wallpaperapp.core.platform.WallpaperManager
import com.google.wallpaperapp.utils.WallpaperType
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import androidx.compose.ui.graphics.Canvas as ComposeCanvas
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope

import kotlin.random.Random

data class MeshPreset(
    val name: String,
    val colors: List<Color>,
    val positions: List<Offset>
)

fun generateRandomMeshPreset(index: Int): MeshPreset {
    val numColors = Random.nextInt(4, 7)
    val colors = List(numColors) {
        Color(
            red = Random.nextFloat(),
            green = Random.nextFloat(),
            blue = Random.nextFloat(),
            alpha = 1f
        )
    }
    val positions = List(numColors) {
        Offset(Random.nextFloat(), Random.nextFloat())
    }
    return MeshPreset("Mesh #${index + 1}", colors, positions)
}

val randomPresets = List(100) { generateRandomMeshPreset(it) }

fun DrawScope.drawMeshGradient(preset: MeshPreset) {
    drawRect(color = preset.colors.first())
    preset.colors.drop(1).forEachIndexed { index, color ->
        val position = preset.positions.getOrNull(index + 1) ?: Offset(0.5f, 0.5f)
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(color, Color.Transparent),
                center = Offset(size.width * position.x, size.height * position.y),
                radius = size.width * 0.8f
            ),
            radius = size.width * 0.8f,
            center = Offset(size.width * position.x, size.height * position.y)
        )
    }
}

@Composable
fun MeshGradientScreen(onPresetClick: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(randomPresets.size) { index ->
                val preset = randomPresets[index]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { onPresetClick(index) }
                        .padding(8.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.7f)
                            .clip(RoundedCornerShape(16.dp))
                            .blur(30.dp)
                    ) {
                        drawMeshGradient(preset)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = preset.name, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
