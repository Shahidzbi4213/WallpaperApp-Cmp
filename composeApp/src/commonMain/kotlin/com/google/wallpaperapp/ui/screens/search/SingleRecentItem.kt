package com.google.wallpaperapp.ui.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.domain.models.RecentSearch
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily

@Composable
fun SingleRecentItem(
    modifier: Modifier = Modifier,
    recentSearch: RecentSearch,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.History,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = recentSearch.query,
            fontFamily = getScreenyFontFamily(),
            style = MaterialTheme.typography.titleMedium
                .copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .clickable(interactionSource = MutableInteractionSource(), indication = null) { onClick() }
                .weight(1f)
                .padding(horizontal = 10.dp))

    }
}