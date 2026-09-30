package com.google.wallpaperapp.core.platform

import com.google.wallpaperapp.data.local.WebScreenyDatabase
import com.google.wallpaperapp.data.local.dao.*
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDbModule(): Module {
    return module {
        val webDb = WebScreenyDatabase()
        single<FavouriteWallpaperDao> { webDb.favouriteWallpaperDao() }
        single<PexelWallpaperDao> { webDb.wallpaperDao() }
        single<PexelWallpaperRemoteKeysDao> { webDb.remoteKeysDao() }
        single<CommonDao> { webDb.commonDao() }
        single<UserPreferenceDao> { webDb.userPreferenceDao() }
        single<RecentSearchDao> { webDb.recentSearchDao() }
    }
}
