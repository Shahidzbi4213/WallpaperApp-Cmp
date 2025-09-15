package com.google.wallpaperapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.wallpaperapp.data.local.entities.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUserPreference(userPreference: UserPreferenceEntity)

    @Query("Update user_preference set languageCode =:code")
    suspend fun updateLanguage(code: String)

    @Query("Update user_preference set appMode =:mode")
    suspend fun updateAppMode(mode: Int)

    @Query("Update user_preference set shouldShowDynamicColor =:enable")
    suspend fun updateDynamicColor(enable:Boolean)


    @Query("SELECT * FROM user_preference limit 1")
    fun getUserPreference(): Flow<UserPreferenceEntity?>
}