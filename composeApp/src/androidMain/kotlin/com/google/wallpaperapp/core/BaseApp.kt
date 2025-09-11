package com.google.wallpaperapp.core

import android.app.Application
import com.google.wallpaperapp.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin() {
            androidContext(this@BaseApp)
        }
    }
}