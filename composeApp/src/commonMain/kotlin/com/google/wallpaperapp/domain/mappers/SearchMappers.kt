package com.google.wallpaperapp.domain.mappers

import com.google.wallpaperapp.data.local.entities.RecentSearchEntity
import com.google.wallpaperapp.domain.models.RecentSearch


fun RecentSearch.toRecentSearchEntity(): RecentSearchEntity {
    return RecentSearchEntity(
        query = query,
        date = date
    )
}

fun RecentSearchEntity.toRecentSearch(): RecentSearch {
    return RecentSearch(query, date)
}