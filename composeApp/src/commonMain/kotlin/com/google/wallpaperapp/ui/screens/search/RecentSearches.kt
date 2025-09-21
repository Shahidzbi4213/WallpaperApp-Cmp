package com.google.wallpaperapp.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.wallpaperapp.domain.models.RecentSearch
import com.google.wallpaperapp.ui.theme.getScreenyFontFamily
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.clear_all
import wallpaperapp.composeapp.generated.resources.recent_searchs

@Composable
 fun RecentSearches(
    recentSearches: List<RecentSearch>, onRecentItemClick: (String) -> Unit, clearAll: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {

        if (recentSearches.isNotEmpty()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(Res.string.recent_searchs),
                    fontFamily = getScreenyFontFamily(),
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    modifier = Modifier.weight(1f)
                )

                TextButton(onClick = clearAll) {
                    Text(text = stringResource(Res.string.clear_all))
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {

            items(recentSearches) {
                SingleRecentItem(recentSearch = it) { onRecentItemClick(it.query) }
            }
        }
    }
}