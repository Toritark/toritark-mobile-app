package com.toritark.stories.data.core_api.jwt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JwtResponse(
    @SerialName("refresh")
    val refreshToken: String,
    @SerialName("access")
    val accessToken: String,
)
