package com.google.wallpaperapp.core.platform

import androidx.room.Room
import androidx.room.RoomDatabase
import com.google.wallpaperapp.data.local.ScreenyDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun platformDbModule(): Module {
    return module {
        single<RoomDatabase.Builder<ScreenyDatabase>> {
            val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            val dbFilePath = requireNotNull(documentDirectory?.path) + "/${ScreenyDatabase.SCREENY_DATABASE}"
            Room.databaseBuilder<ScreenyDatabase>(name = dbFilePath,)
        }
    }
}

