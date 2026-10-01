package com.google.wallpaperapp.ui.desktop.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.ui.desktop.components.ScrollableGrid
import com.google.wallpaperapp.ui.desktop.components.WallpaperCard
import com.google.wallpaperapp.ui.screens.category.Category
import com.google.wallpaperapp.ui.screens.category.categories
import com.google.wallpaperapp.ui.theme.ScrimGradient

@Composable
fun DesktopCategoriesScreen(
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableGrid(modifier = modifier, minCellWidth = 340.dp) {
        items(count = categories.size) { index ->
            val category = categories[index]
            WallpaperCard(
                imageUrl = category.thumbnail,
                contentDescription = category.name,
                onClick = { onCategoryClick(category) }
            ) {
                // Categories always carry their label, unlike wallpaper tiles.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(ScrimGradient)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
