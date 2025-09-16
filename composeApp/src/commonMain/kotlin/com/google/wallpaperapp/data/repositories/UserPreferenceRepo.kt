package com.google.wallpaperapp.data.repositories

import com.google.wallpaperapp.core.platform.AppLogger
import com.google.wallpaperapp.data.local.dao.UserPreferenceDao
import com.google.wallpaperapp.domain.mappers.toUserPreference
import com.google.wallpaperapp.domain.models.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.withContext

class UserPreferenceRepo(private val dao: UserPreferenceDao) {

    val uerPreference
        get() = dao.getUserPreference()
            .map {
                it?.toUserPreference()
                    ?: UserPreferences("en", 0, true)
            }


    suspend fun updateAppLanguage(languageCode: String) = withContext(Dispatchers.IO) {
        dao.updateLanguage(languageCode)
    }

    suspend fun updateAppMode(appMode: Int) = withContext(Dispatchers.IO) {
        dao.updateAppMode(appMode)
    }

    suspend fun updateDynamicColor(isDynamicColor: Boolean) = withContext(Dispatchers.IO) {
        dao.updateDynamicColor(isDynamicColor)
    }

}