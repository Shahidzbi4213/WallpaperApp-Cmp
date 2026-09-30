package com.google.wallpaperapp.data.local

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.wallpaperapp.data.local.dao.*
import com.google.wallpaperapp.data.local.entities.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WebScreenyDatabase {
    private val favDao = WebFavouriteWallpaperDao()
    private val wallDao = WebPexelWallpaperDao()
    private val remDao = WebPexelWallpaperRemoteKeysDao()
    private val comDao = WebCommonDao(wallDao, remDao)
    private val prefDao = WebUserPreferenceDao()
    private val secDao = WebRecentSearchDao()

    fun favouriteWallpaperDao(): FavouriteWallpaperDao = favDao
    fun wallpaperDao(): PexelWallpaperDao = wallDao
    fun remoteKeysDao(): PexelWallpaperRemoteKeysDao = remDao
    fun commonDao(): CommonDao = comDao
    fun userPreferenceDao(): UserPreferenceDao = prefDao
    fun recentSearchDao(): RecentSearchDao = secDao
}

class WebFavouriteWallpaperDao : FavouriteWallpaperDao {
    private val _favourites = MutableStateFlow<List<FavouriteWallpaperEntity>>(emptyList())

    override suspend fun addToFavourite(favouriteWallpaper: FavouriteWallpaperEntity) {
        _favourites.value = listOf(favouriteWallpaper) + _favourites.value.filter { it.id != favouriteWallpaper.id }
    }

    override suspend fun removeFromFavourite(wallpaper: FavouriteWallpaperEntity) {
        _favourites.value = _favourites.value.filter { it.id != wallpaper.id }
    }

    override suspend fun getFavouriteById(id: Long): FavouriteWallpaperEntity? {
        return _favourites.value.find { it.id == id }
    }

    override suspend fun deleteViaUrl(url: String) {
        _favourites.value = _favourites.value.filter { it.wallpaper != url }
    }

    override fun getAllFavourites(): Flow<List<FavouriteWallpaperEntity>> = _favourites.asStateFlow()
}

class WebPexelWallpaperDao : PexelWallpaperDao {
    private val wallpapersList = mutableListOf<WallpaperEntity>()

    override fun getAllWallpapers(): PagingSource<Int, WallpaperEntity> {
        return object : PagingSource<Int, WallpaperEntity>() {
            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, WallpaperEntity> {
                return LoadResult.Page(
                    data = wallpapersList.toList(),
                    prevKey = null,
                    nextKey = null
                )
            }
            override fun getRefreshKey(state: PagingState<Int, WallpaperEntity>): Int? = null
        }
    }

    override suspend fun getWallpaperCount(): Int = wallpapersList.size

    override suspend fun addWallpapers(wallpapers: List<WallpaperEntity>) {
        wallpapersList.addAll(wallpapers)
    }

    fun clear() {
        wallpapersList.clear()
    }
}

class WebPexelWallpaperRemoteKeysDao : PexelWallpaperRemoteKeysDao {
    private val keysMap = mutableMapOf<Long, WallpaperRemoteKeyEntity>()

    override suspend fun getRemoteKeyByWallpaperId(id: Long): WallpaperRemoteKeyEntity? = keysMap[id]

    override suspend fun addAllRemoteKeys(remoteKeys: List<WallpaperRemoteKeyEntity>) {
        remoteKeys.forEach { keysMap[it.wallpaperId] = it }
    }

    fun clear() {
        keysMap.clear()
    }
}

class WebCommonDao(
    private val wallDao: WebPexelWallpaperDao,
    private val remDao: WebPexelWallpaperRemoteKeysDao
) : CommonDao {
    override suspend fun deleteAllWallpapers() {
        wallDao.clear()
    }

    override suspend fun deleteAllRemoteKeys() {
        remDao.clear()
    }
}

class WebUserPreferenceDao : UserPreferenceDao {
    private val _prefs = MutableStateFlow<UserPreferenceEntity?>(
        UserPreferenceEntity(
            id = 1,
            languageCode = "en",
            appMode = 0,
            shouldShowDynamicColor = true
        )
    )

    override suspend fun addUserPreference(userPreference: UserPreferenceEntity) {
        _prefs.value = userPreference
    }

    override suspend fun updateLanguage(code: String) {
        _prefs.value = _prefs.value?.copy(languageCode = code)
    }

    override suspend fun updateAppMode(mode: Int) {
        _prefs.value = _prefs.value?.copy(appMode = mode)
    }

    override suspend fun updateDynamicColor(enable: Boolean) {
        _prefs.value = _prefs.value?.copy(shouldShowDynamicColor = enable)
    }

    override fun getUserPreference(): Flow<UserPreferenceEntity?> = _prefs.asStateFlow()
}

class WebRecentSearchDao : RecentSearchDao {
    private val _recent = MutableStateFlow<List<RecentSearchEntity>>(emptyList())

    override suspend fun saveRecent(recentSearch: RecentSearchEntity) {
        _recent.value = listOf(recentSearch) + _recent.value.filter { it.query != recentSearch.query }
    }

    override suspend fun removeRecent(recentSearch: RecentSearchEntity) {
        _recent.value = _recent.value.filter { it.query != recentSearch.query }
    }

    override suspend fun clearAllRecent() {
        _recent.value = emptyList()
    }

    override fun getRecentSearches(): Flow<List<RecentSearchEntity>> = _recent.asStateFlow()
}
