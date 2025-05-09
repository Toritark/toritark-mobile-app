package com.toritark.stories.data.core_api.jwt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RefreshJwtTokenResponse(
    @SerialName("access")
    val accessToken: String,
)
