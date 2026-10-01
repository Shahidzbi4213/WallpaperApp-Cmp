package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.theme.Ember
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextLow
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_back
import wallpaperapp.composeapp.generated.resources.desktop_clear_search
import wallpaperapp.composeapp.generated.resources.desktop_search_placeholder

/**
 * The desktop chrome above the content: where you are, an always-visible search field, and
 * window-level actions. The phone puts search behind an icon; a desktop toolbar has the room to
 * keep it open, so it stays open.
 */
@Composable
fun DesktopToolBar(
    title: String,
    searchQuery: String,
    searchFocusRequester: FocusRequester,
    canGoBack: Boolean,
    onBack: () -> Unit,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(DesktopDimens.ToolbarHeight)
            .padding(horizontal = DesktopDimens.Gutter),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (canGoBack) {
            DesktopIconButton(
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = stringResource(Res.string.desktop_back),
                onClick = onBack
            )
            Spacer(Modifier.width(12.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = TextHi,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            // Takes all the slack so the search field pins to the right edge; a long
            // category title ellipsizes rather than pushing search off-screen.
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(16.dp))

        SearchField(
            query = searchQuery,
            focusRequester = searchFocusRequester,
            onQueryChange = onSearchChange
        )
    }
}

@Composable
private fun SearchField(
    query: String,
    focusRequester: FocusRequester,
    onQueryChange: (String) -> Unit
) {
    val shape = RoundedCornerShape(9.dp)

    Row(
        modifier = Modifier
            .width(320.dp)
            .height(38.dp)
            .glass(shape)
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Icon(
            imageVector = Icons.Outlined.Search,
            contentDescription = null,
            tint = TextLow,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))

        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(Res.string.desktop_search_placeholder),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLow
                )
            }
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                cursorBrush = SolidColor(Ember),
                textStyle = LocalTextStyle.current.merge(
                    MaterialTheme.typography.bodyMedium.copy(color = TextHi)
                ),
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
            )
        }

        if (query.isNotEmpty()) {
            Spacer(Modifier.width(6.dp))
            DesktopIconButton(
                icon = Icons.Outlined.Close,
                contentDescription = stringResource(Res.string.desktop_clear_search),
                size = 22.dp,
                iconSize = 14.dp,
                onClick = { onQueryChange("") }
            )
        }
    }
}

@Composable
fun DesktopIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = DesktopDimens.IconButtonSize,
    iconSize: androidx.compose.ui.unit.Dp = 18.dp
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered by interaction.collectIsHoveredAsState()

    DesktopTooltip(text = contentDescription) {
        Box(
            modifier = modifier
                .size(size)
                .desktopClickable(interaction = interaction, onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = if (hovered) TextHi else TextMid,
                modifier = Modifier.size(iconSize)
            )
        }
    }
}
