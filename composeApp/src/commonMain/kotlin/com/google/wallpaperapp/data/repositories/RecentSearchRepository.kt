package com.google.wallpaperapp.data.repositories

import com.google.wallpaperapp.data.local.dao.RecentSearchDao
import com.google.wallpaperapp.domain.mappers.toRecentSearch
import com.google.wallpaperapp.domain.mappers.toRecentSearchEntity
import com.google.wallpaperapp.domain.models.RecentSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RecentSearchRepository(private val dao: RecentSearchDao) {

    val recentSearches = dao.getRecentSearches()
        .map { recentSearchEntities -> recentSearchEntities.map { recentSearchEntity -> recentSearchEntity.toRecentSearch() } }


    suspend fun saveRecent(recentSearch: RecentSearch) = withContext(Dispatchers.IO) {

        val searchList = dao.getRecentSearches().firstOrNull()

        if (searchList != null && searchList.size >= 15) {
            dao.removeRecent(searchList.last())
            dao.saveRecent(recentSearch.toRecentSearchEntity())
        } else {
            dao.saveRecent(recentSearch.toRecentSearchEntity())
        }
    }

    suspend fun clearAllRecent() = withContext(Dispatchers.IO) {
        dao.clearAllRecent()
    }

    suspend fun removeRecent(recentSearch: RecentSearch) = withContext(Dispatchers.IO) {
        dao.removeRecent(recentSearch.toRecentSearchEntity())
    }

}