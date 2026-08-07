package com.google.wallpaperapp.ui.screens.meshgradient

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
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
import androidx.compose.ui.unit.min
import org.jetbrains.compose.ui.tooling.preview.Preview

import kotlin.random.Random

data class MeshPreset(
    val name: String,
    val colors: List<Color>,
    val positions: List<Offset>
)

private data class PaletteStyle(
    val saturationRange: ClosedFloatingPointRange<Float>,
    val valueRange: ClosedFloatingPointRange<Float>,
    val hueSpread: Float
)

private val premiumStyles = listOf(
    PaletteStyle(0.35f..0.60f, 0.82f..1.00f, 35f),  // soft premium
    PaletteStyle(0.55f..0.80f, 0.65f..0.90f, 55f),  // rich jewel
    PaletteStyle(0.45f..0.70f, 0.75f..1.00f, 90f),  // vibrant editorial
    PaletteStyle(0.20f..0.45f, 0.88f..1.00f, 25f)   // minimal luxury
)

fun generatePremiumMeshPreset(index: Int): MeshPreset {
    val colorCount = Random.nextInt(5, 8)
    val style = premiumStyles.random()

    val baseHue = Random.nextFloat() * 360f

    val colors = List(colorCount) { i ->
        val t = i / (colorCount - 1f)

        val hueOffset =
            (t - 0.5f) * style.hueSpread +
                    Random.nextFloat() * 16f - 8f

        Color.hsv(
            hue = (baseHue + hueOffset + 360f) % 360f,
            saturation = randomIn(style.saturationRange),
            value = randomIn(style.valueRange),
            alpha = 1f
        )
    }

    val positions = generatePremiumPositions(colorCount)

    return MeshPreset(
        name = "${index + 1}",
        colors = colors,
        positions = positions
    )
}

private fun generatePremiumPositions(count: Int): List<Offset> {
    val anchors = mutableListOf(
        Offset(0.08f, 0.12f),
        Offset(0.92f, 0.10f),
        Offset(0.10f, 0.88f),
        Offset(0.90f, 0.90f),
        Offset(0.50f, 0.48f)
    )

    while (anchors.size < count) {
        anchors += Offset(
            x = Random.nextFloat() * 0.70f + 0.15f,
            y = Random.nextFloat() * 0.70f + 0.15f
        )
    }

    return anchors
        .shuffled()
        .take(count)
        .map { point ->
            Offset(
                x = (point.x + randomJitter()).coerceIn(0f, 1f),
                y = (point.y + randomJitter()).coerceIn(0f, 1f)
            )
        }
}

private fun randomJitter(): Float =
    Random.nextFloat() * 0.12f - 0.06f

private fun randomIn(
    range: ClosedFloatingPointRange<Float>
): Float =
    range.start +
            Random.nextFloat() * (range.endInclusive - range.start)
val randomPresets = List(100) { generatePremiumMeshPreset(it) }

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
fun MeshGradientScreen(onPresetClick: (Int) -> Unit = {}) {
    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        LazyVerticalStaggeredGrid(
            columns = androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive(
                (100.dp)
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            items(randomPresets.size) { index ->
                val preset = randomPresets[index]
                val height = remember(preset) {
                    val hash = kotlin.math.abs(preset.hashCode()) % 3
                    when (hash) {
                        0 -> 250.dp
                        1 -> 200.dp
                        else -> Random.nextInt(260, 320).dp
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .height(height = height)
                        .clickable { onPresetClick(index) }
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        drawMeshGradient(preset)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = preset.name, style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }
    }
}
