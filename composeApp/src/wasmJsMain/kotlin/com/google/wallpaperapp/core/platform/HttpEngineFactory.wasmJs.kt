package com.google.wallpaperapp.core.platform

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual class HttpEngineFactory actual constructor() {
    actual fun getHttpClientEngine(): HttpClientEngine {
        return Js.create()
    }
}
