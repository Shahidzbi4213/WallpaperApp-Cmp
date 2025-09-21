package com.google.wallpaperapp.data.local.entities

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Entity(tableName = "recent_search")
@Immutable
data class RecentSearchEntity(
    @PrimaryKey
    val query: String,
    val date: Long = Clock.System.now().toEpochMilliseconds(),
)
