package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun DesktopGridScrollbar(state: LazyGridState, modifier: Modifier = Modifier)

@Composable
expect fun DesktopListScrollbar(state: LazyListState, modifier: Modifier = Modifier)

@Composable
expect fun DesktopScrollStateScrollbar(state: ScrollState, modifier: Modifier = Modifier)
