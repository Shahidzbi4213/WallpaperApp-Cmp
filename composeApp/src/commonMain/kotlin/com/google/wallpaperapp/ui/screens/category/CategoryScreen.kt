package com.google.wallpaperapp.ui.screens.category

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.draw.shadow
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
import com.google.wallpaperapp.ui.theme.GlassBorderHi
import com.google.wallpaperapp.ui.theme.Violet
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import com.skydoves.landscapist.components.rememberImageComponent
import com.skydoves.landscapist.crossfade.CrossfadePlugin
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds


@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    onCategoryClick: (name: String, query: String) -> Unit
) {

    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        delay(250.milliseconds)
        showContent = true
    }


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp),
        modifier = modifier.fillMaxSize()
    ) {


        if (showContent) {
            items(categories, key = { it.name }) { category ->
                CategoryItem(category = category) {
                    onCategoryClick(category.name, category.query)
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

    val shape = RoundedCornerShape(24.dp)
    
    val height = remember(category.name) { 
        val hash = kotlin.math.abs(category.name.hashCode()) % 3
        when (hash) {
            0 -> 160.dp
            1 -> 200.dp
            else -> 240.dp
        }
    }

    Box(
        modifier = Modifier
            .height(height)
            .shadow(12.dp, shape, spotColor = Violet, ambientColor = Violet)
            .clip(shape)
            .border(1.dp, GlassBorderHi, shape)
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
                    ), shape = shape
                )

        )


        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.18f), Color.Black.copy(alpha = 0.58f))
                    )
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