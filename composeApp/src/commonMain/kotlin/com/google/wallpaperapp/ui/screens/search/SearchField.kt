package com.google.wallpaperapp.ui.screens.search

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults.InputField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import org.jetbrains.compose.resources.stringResource
import wallpaperapp.composeapp.generated.resources.Res
import wallpaperapp.composeapp.generated.resources.search_wallpaper


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchQuery: String,
    isExpanded: Boolean,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    onQueryChange: (String) -> Unit,
    clearQuery: () -> Unit,
    onExpandChange: (Boolean) -> Unit,
    saveSearchQuery: (String) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val modifierConfig = modifier
        .searchFieldModifier(isExpanded, focusRequester)

    InputField(
        query = searchQuery,
        onQueryChange = onQueryChange,
        onSearch = {
            if (searchQuery.isNotEmpty() && searchQuery.length >= 3) {
                onExpandChange(false)
                saveSearchQuery(searchQuery)
            }
        },
        expanded = isExpanded,
        onExpandedChange = onExpandChange,
        placeholder = {
            Text(stringResource(Res.string.search_wallpaper))
        },
        leadingIcon = {
            SearchFieldLeadingIcon(
                searchQuery = searchQuery,
                isExpanded = isExpanded,
                onClear = {
                    clearQuery()
                    onExpandChange(false)
                    onNavigateBack()
                }
            )
        },
        trailingIcon = {
           if (isExpanded){
               SearchFieldTrailingIcon(
                   searchQuery = searchQuery,
                   isExpanded = isExpanded,
                   onClear = {
                       clearQuery()
                   }
               )
           }
        },
        modifier = modifierConfig,
        colors = TextFieldDefaults.colors(
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
private fun SearchFieldLeadingIcon(
    searchQuery: String,
    isExpanded: Boolean,
    onClear: () -> Unit
) {
    if (searchQuery.isEmpty() && !isExpanded) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    } else {
        Icon(
            Icons.AutoMirrored.Default.ArrowBack,
            contentDescription = null,
            modifier = Modifier.clickable { onClear() },
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SearchFieldTrailingIcon(
    searchQuery: String,
    isExpanded: Boolean,
    onClear: () -> Unit
) {
    if (searchQuery.isNotEmpty()) {
        Icon(
            Icons.Default.Clear,
            contentDescription = null,
            modifier = Modifier.clickable { onClear() },
            tint = if (isExpanded) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            }
        )
    }
}


private fun Modifier.searchFieldModifier(
    isExpanded: Boolean,
    focusRequester: FocusRequester
): Modifier {
    val animation = Modifier.animateContentSize(spring(stiffness = Spring.StiffnessHigh))
    return if (isExpanded) {
        this
            .then(animation)
            .focusRequester(focusRequester)
    } else {
        this
            .fillMaxWidth()
            .then(animation)
            .focusRequester(focusRequester)
    }
}
