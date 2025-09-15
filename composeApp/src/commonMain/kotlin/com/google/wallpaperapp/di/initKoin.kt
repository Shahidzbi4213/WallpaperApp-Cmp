package com.google.wallpaperapp.di


import com.google.wallpaperapp.core.platform.platformDbModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module


fun initKoin(config: KoinAppDeclaration? = null) {

    startKoin {
        config?.invoke(this)
        modules(
            NetworkModule().module,
            platformDbModule(),
            AppModule().module,
            DbModule().module,
            FavouriteModule().module
        )
    }
}