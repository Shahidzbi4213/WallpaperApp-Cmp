package com.google.wallpaperapp.ui.desktop.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
actual fun DesktopGridScrollbar(state: LazyGridState, modifier: Modifier) {}

@Composable
actual fun DesktopListScrollbar(state: LazyListState, modifier: Modifier) {}

@Composable
actual fun DesktopScrollStateScrollbar(state: ScrollState, modifier: Modifier) {}
