package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.Void
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.apply
import wallpaperapp.composeapp.generated.resources.download


@Composable
fun ActionButtons(
    isFavourite: Boolean = false,
    onDownload: () -> Unit,
    onApply: () -> Unit,
    onFavourite: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .safeDrawingPadding()
            .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Download — circular glass icon button
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .glass(CircleShape, strong = true)
                .clickable { onDownload() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Download,
                contentDescription = stringResource(Res.string.download),
                tint = TextHi,
                modifier = Modifier.size(22.dp)
            )
        }

        // Apply — the "light" CTA (white pill over the wallpaper)
        Button(
            onClick = onApply,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Void
            ),
        ) {
            Text(
                text = stringResource(Res.string.apply),
                style = MaterialTheme.typography.titleMedium
            )
        }

        FavouriteButton(isFavourite = isFavourite, onFavourite = onFavourite)
    }
}
