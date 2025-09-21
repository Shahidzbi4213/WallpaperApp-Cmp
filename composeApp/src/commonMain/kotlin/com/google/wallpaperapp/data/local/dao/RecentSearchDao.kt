package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.google.wallpaperapp.data.local.entities.RecentSearchEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDao {

    @Upsert
    suspend fun saveRecent(recentSearch: RecentSearchEntity)

    @Delete
    suspend fun removeRecent(recentSearch: RecentSearchEntity)

    @Query("DELETE FROM recent_search")
    suspend fun clearAllRecent()

    @Query("SELECT * FROM recent_search order by date desc")
    fun getRecentSearches(): Flow<List<RecentSearchEntity>>
}