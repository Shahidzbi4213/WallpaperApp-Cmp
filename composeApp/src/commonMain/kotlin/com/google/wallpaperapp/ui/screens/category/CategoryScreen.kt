package com.google.wallpaperapp.ui.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.wallpaperapp.ui.components.LoadingPlaceHolder
import com.google.wallpaperapp.ui.composables.shimmerBrush
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.animation.crossfade.CrossfadePlugin
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import kotlinx.coroutines.delay


@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (String) -> Unit
) {

    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(250)
        showContent = true
    }


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {


        if (showContent) {
            items(categories, key = { it.name }) { category ->
                CategoryItem(category = category) {
                    onCategoryClick(category.name)
                }
            }

        } else {
            items(categories.size) {
                LoadingPlaceHolder(modifier = Modifier.height(100.dp))
            }
        }

    }


}


@Composable
fun CategoryItem(category: Category, onClick: () -> Unit) {

    var showShimmer by remember {
        mutableStateOf(true)
    }

    Box(
        modifier = Modifier
            .height(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {

        CoilImage(
            imageModel = { category.thumbnail },
            success = { state, painter ->
                showShimmer = false

                Image(painter = painter, contentDescription = category.name,
                    contentScale = ContentScale.Crop)
            },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                contentDescription = category.name,
                requestSize = IntSize(800, 800)

            ),
            component = rememberImageComponent {
                CrossfadePlugin()
            },
            modifier = Modifier
                .matchParentSize()
                .background(
                    shimmerBrush(
                        targetValue = 1300f,
                        showShimmer = showShimmer
                    ), shape = RoundedCornerShape(10.dp)
                )

        )


        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Black, Color(0xFF29323B))
                    ), alpha = if (isSystemInDarkTheme()) 0.45f else 0.35f
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold, fontSize = 20.sp, fontFamily = getScreenyFontFamily()
                ),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}