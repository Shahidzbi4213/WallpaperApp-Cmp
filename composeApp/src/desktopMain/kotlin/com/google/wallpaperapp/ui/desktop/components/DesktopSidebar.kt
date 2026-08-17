package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.routs.TopLevelBackStack
import com.google.wallpaperapp.ui.routs.bottomNavigationItems
import com.google.wallpaperapp.ui.theme.EmberGradient
import com.google.wallpaperapp.ui.theme.GlassBorder
import com.google.wallpaperapp.ui.theme.Ink950
import com.google.wallpaperapp.ui.theme.ScreenyGradient
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.eyebrowStyle
import org.jetbrains.compose.resources.stringResource

import org.jetbrains.compose.resources.painterResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.app_logo

/**
 * The persistent desktop navigation: a fixed left rail, not a floating bottom bar.
 * Reuses [bottomNavigationItems] verbatim -- it is plain data, so the same five entries drive
 * both the phone's bottom bar and this.
 */
@Composable
fun DesktopSidebar(
    selected: TopLevelBackStack,
    collapsed: Boolean,
    onSelect: (TopLevelBackStack) -> Unit,
    modifier: Modifier = Modifier
) {
    val width by animateDpAsState(
        if (collapsed) DesktopDimens.SidebarCollapsedWidth else DesktopDimens.SidebarWidth,
        tween(180),
        label = "sidebarWidth"
    )

    Column(
        modifier = modifier
            .width(width)
            .fillMaxHeight()
            .background(Ink950.copy(alpha = 0.55f))
            .padding(horizontal = 12.dp, vertical = 16.dp)
    ) {
        Brandmark(collapsed)

        Spacer(Modifier.height(28.dp))

        bottomNavigationItems.forEach { item ->
            SidebarItem(
                label = stringResource(item.label),
                selected = item.key == selected,
                collapsed = collapsed,
                icon = { tint ->
                    Icon(
                        imageVector = if (item.key == selected) item.selectedIcon else item.icon,
                        contentDescription = stringResource(item.label),
                        tint = tint,
                        modifier = Modifier.size(20.dp)
                    )
                },
                onClick = { onSelect(item.key) }
            )
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun Brandmark(collapsed: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.app_logo),
            contentDescription = "Screeny Logo",
            tint = Color.Unspecified,
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        if (!collapsed) {
            Spacer(Modifier.width(10.dp))
            Column {
                Text(
                    text = "Screeny",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextHi
                )
                Text(
                    text = "WALLPAPERS",
                    style = eyebrowStyle(),
                    color = TextLow
                )
            }
        }
    }
}

@Composable
private fun SidebarItem(
    label: String,
    selected: Boolean,
    collapsed: Boolean,
    icon: @Composable (Color) -> Unit,
    onClick: () -> Unit
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    val contentColor by animateColorAsState(
        when {
            selected -> Color.White
            hovered -> TextHi
            else -> TextMid
        },
        label = "sidebarItemColor"
    )

    val shape = RoundedCornerShape(10.dp)
    val base = Modifier
        .fillMaxWidth()
        .height(DesktopDimens.RowHeight)
        .clip(shape)

    Box(
        modifier = if (selected) {
            base.background(EmberGradient)
        } else if (hovered) {
            base.background(Color.White.copy(alpha = 0.06f))
        } else {
            base
        }.desktopClickable(interaction = interaction, onClick = onClick),
        contentAlignment = if (collapsed) Alignment.Center else Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(horizontal = if (collapsed) 0.dp else 12.dp)
        ) {
            icon(contentColor)
            if (!collapsed) {
                Spacer(Modifier.width(12.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    color = contentColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        if (selected && collapsed) {
            // Collapsed rail still needs an unambiguous active marker.
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(3.dp)
                    .height(20.dp)
                    .background(GlassBorder)
            )
        }
    }
}
