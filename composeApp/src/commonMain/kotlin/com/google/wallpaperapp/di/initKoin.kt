package com.google.wallpaperapp.di


import com.google.wallpaperapp.core.platform.platformDbModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.modules


fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            NetworkModule::class,
            AppModule::class,
            DbModule::class,
            FavouriteModule::class
        )
        modules(
            platformDbModule(),
        )
    }
}
