package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.composables.shimmerBrush
import com.google.wallpaperapp.ui.desktop.theme.DesktopDimens
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.TextMid
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.desktop_error_title
import wallpaperapp.composeapp.generated.resources.desktop_retry

/** Grid-shaped skeleton. Desktop windows show many cards at once, so a single spinner reads as a hang. */
@Composable
fun WallpaperCardSkeleton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(DesktopDimens.CardAspectRatio)
            .clip(RoundedCornerShape(DesktopDimens.CardCorner))
            .background(shimmerBrush())
    )
}

@Composable
fun DesktopEmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(title, style = MaterialTheme.typography.headlineSmall, color = TextHi)
            Text(
                subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMid,
                textAlign = TextAlign.Center
            )
            action?.invoke()
        }
    }
}

/** Inline retry row -- a desktop app shows the failure in place, it does not toast and forget. */
@Composable
fun DesktopErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .glass(RoundedCornerShape(12.dp))
                .padding(24.dp)
        ) {
            Text(stringResource(Res.string.desktop_error_title), style = MaterialTheme.typography.titleMedium, color = TextHi)
            Text(message, style = MaterialTheme.typography.bodySmall, color = TextMid, textAlign = TextAlign.Center)
            DesktopTextButton(text = stringResource(Res.string.desktop_retry), onClick = onRetry)
        }
    }
}

@Composable
fun DesktopTextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .glass(RoundedCornerShape(8.dp), strong = true)
            .desktopClickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 9.dp)
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge, color = TextHi)
    }
}
