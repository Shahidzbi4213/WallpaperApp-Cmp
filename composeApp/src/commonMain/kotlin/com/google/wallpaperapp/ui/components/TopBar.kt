package com.google.wallpaperapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.theme.TextHi
import com.google.wallpaperapp.ui.theme.glass
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: StringResource,
    isHome: Boolean,
    onClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.headlineSmall,
                color = TextHi
            )
        },
        actions = {
            if (isHome) {
                Box(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(42.dp)
                        .clip(CircleShape)
                        .glass(CircleShape, strong = true)
                        .clickable { onClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(title),
                        tint = TextHi,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}
