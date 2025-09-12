package com.google.wallpaperapp.ui.screens.favourite

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.wallpaperapp.ui.composables.FavouriteWallpaperItem
import com.google.wallpaperapp.ui.composables.Pulsating
import com.google.wallpaperapp.ui.composables.mirror
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.explore_wallpapers
import wallpaperapp.composeapp.generated.resources.no_favourite_found
import wallpaperapp.composeapp.generated.resources.your_favorite_wallpapers_will_appear_here_start_exploring_and_add_some_to_your_favorites

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.FavouriteScreen(
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope,
    favouriteViewModel: FavouriteViewModel = koinViewModel(),
    onExplore: () -> Unit,
    onWallpaperClick: (Long, String) -> Unit
) {

    val favourites by favouriteViewModel.getAllFavourites.collectAsStateWithLifecycle()


    if (favourites.isEmpty()) {
        NoFavouritePlaceholder(onExplore = onExplore)
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier
                .fillMaxSize()

        ) {

            items(
                favourites,
                key = { favourite -> favourite.id })
            { favourite ->
                FavouriteWallpaperItem(
                    wallpaper = favourite.wallpaper,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onWallpaperClick = { wallpaper -> onWallpaperClick(favourite.id, wallpaper) },
                    onRemoveFromFavClick = { wallpaper -> favouriteViewModel.removeFromFavourite(wallpaper) }
                )
            }

        }
    }


}

@Composable
fun NoFavouritePlaceholder(onExplore: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.no_favourite_found),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .mirror()
                .size(80.dp),

            )

        Text(
            text = stringResource(Res.string.your_favorite_wallpapers_will_appear_here_start_exploring_and_add_some_to_your_favorites),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 20.dp)
        )

        Pulsating {
            Button(
                onClick = onExplore,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text(
                    text = stringResource(Res.string.explore_wallpapers),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}


