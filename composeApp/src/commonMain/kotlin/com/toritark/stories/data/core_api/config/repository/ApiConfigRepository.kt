package com.toritark.stories.data.core_api.config.repository

import com.toritark.stories.data.core_api.config.model.ApiConfig


interface ApiConfigRepository {
    suspend fun getApiConfig(): ApiConfig
}

internal class ApiConfigRepositoryImpl : ApiConfigRepository {

    override suspend fun getApiConfig(): ApiConfig {
        return ApiConfig(
            host = "192.168.1.3",
            port = 8000,
            isHttps = false,
//            host = "api.staging.actinis.io",
//            port = 443,
//            isHttps = true,
        )
    }
}