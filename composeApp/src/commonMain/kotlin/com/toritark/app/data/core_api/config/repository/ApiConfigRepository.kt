package com.toritark.app.data.core_api.config.repository

import com.toritark.app.BuildKonfig
import com.toritark.app.data.core_api.config.model.ApiConfig


interface ApiConfigRepository {
    fun getApiConfig(): ApiConfig
}

internal class ApiConfigRepositoryImpl : ApiConfigRepository {

    override fun getApiConfig(): ApiConfig {
        return ApiConfig(
            host = requireNotNull(BuildKonfig.API_HOST.takeIf { it.isNotBlank() }) { "API Host is not set" },
            port = BuildKonfig.API_PORT,
            isHttps = BuildKonfig.API_IS_HTTPS,
        )
    }
}