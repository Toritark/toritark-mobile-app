package com.toritark.stories.data.core_api.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiConfig(
    @SerialName("host")
    val host: String,
    @SerialName("port")
    val port: Int,
    @SerialName("is_https")
    val isHttps: Boolean,
)