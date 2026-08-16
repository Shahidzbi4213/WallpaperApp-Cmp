package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ContextMenuArea
import androidx.compose.foundation.ContextMenuItem
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.components.CardActionButton
import com.google.wallpaperapp.ui.desktop.components.CardHoverActions
import com.google.wallpaperapp.ui.desktop.components.ScrollableGrid
import com.google.wallpaperapp.ui.desktop.components.desktopClickable
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.screens.meshgradient.MeshPreset
import com.google.wallpaperapp.ui.screens.meshgradient.drawMeshGradient
import com.google.wallpaperapp.ui.screens.meshgradient.randomPresets
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.GlassBorderHi
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_set_as_wallpaper
import wallpaperapp.composeapp.generated.resources.download

@Composable
fun DesktopMeshGradientScreen(
    onApply: (MeshPreset) -> Unit,
    onDownload: (MeshPreset) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableGrid(modifier = modifier) {
        items(count = randomPresets.size) { index ->
            MeshCard(
                preset = randomPresets[index],
                onApply = { onApply(randomPresets[index]) },
                onDownload = { onDownload(randomPresets[index]) }
            )
        }
    }
}

@Composable
private fun MeshCard(
    preset: MeshPreset,
    onApply: () -> Unit,
    onDownload: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()
    val shape = RoundedCornerShape(DesktopDimens.CardCorner)
    val applyLabel = stringResource(Res.string.desktop_set_as_wallpaper)
    val downloadLabel = stringResource(Res.string.download)

    ContextMenuArea(items = {
        listOf(
            ContextMenuItem(applyLabel) { onApply() },
            ContextMenuItem(downloadLabel) { onDownload() },
        )
    }) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(DesktopDimens.CardAspectRatio)
                .clip(shape)
                .border(1.dp, if (hovered) GlassBorderHi else GlassBorder, shape)
                .desktopClickable(interaction = interaction, onClick = onApply)
        ) {
            Canvas(modifier = Modifier.fillMaxSize().blur(40.dp)) {
                drawMeshGradient(preset)
            }

            CardHoverActions(visible = hovered) {
                CardActionButton(
                    icon = Icons.Outlined.Wallpaper,
                    contentDescription = applyLabel,
                    onClick = onApply
                )
                CardActionButton(
                    icon = Icons.Outlined.Download,
                    contentDescription = downloadLabel,
                    onClick = onDownload
                )
            }
        }
    }
}
