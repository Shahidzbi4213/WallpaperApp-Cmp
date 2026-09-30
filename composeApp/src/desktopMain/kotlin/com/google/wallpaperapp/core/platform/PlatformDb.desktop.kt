package com.google.wallpaperapp.core.platform

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.di.dbModule
import org.koin.core.module.Module
import org.koin.dsl.module
import java.io.File

actual fun platformDbModule(): Module {
    return module {
        includes(dbModule)
        single<RoomDatabase.Builder<ScreenyDatabase>> {
            val dbFile = File(appDataDir(), ScreenyDatabase.SCREENY_DATABASE)
            Room.databaseBuilder<ScreenyDatabase>(name = dbFile.absolutePath)
        }
    }
}
