package com.toritark.app.di.module

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.darwin.Darwin

internal actual fun getHttpClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig> = Darwin