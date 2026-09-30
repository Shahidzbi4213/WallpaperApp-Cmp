package com.google.wallpaperapp.core.platform

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.wallpaperapp.data.local.ScreenyDatabase
import com.google.wallpaperapp.di.dbModule
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformDbModule(): Module {
    return module {
        includes(dbModule)
        single<RoomDatabase.Builder<ScreenyDatabase>> {
            val dbFile = get<Context>().getDatabasePath(ScreenyDatabase.SCREENY_DATABASE)
            Room.databaseBuilder<ScreenyDatabase>(
                context = get(),
                name = dbFile.absolutePath
            )
        }
    }
}
