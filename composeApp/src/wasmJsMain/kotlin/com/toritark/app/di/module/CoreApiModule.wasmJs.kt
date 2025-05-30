package com.toritark.app.di.module

import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

internal actual fun getHttpClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig> {
    return Js
}