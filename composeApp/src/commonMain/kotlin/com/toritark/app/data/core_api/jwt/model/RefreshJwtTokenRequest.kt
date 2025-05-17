package com.toritark.app.data.core_api.jwt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshJwtTokenRequest(
    @SerialName("refresh")
    val refreshToken: String,
)
