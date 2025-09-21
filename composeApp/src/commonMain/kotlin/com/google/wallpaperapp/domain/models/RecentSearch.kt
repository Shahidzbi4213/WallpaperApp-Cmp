package com.google.wallpaperapp.domain.models

import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class RecentSearch(
    val query: String,
    val date: Long = Clock.System.now().toEpochMilliseconds()
)
