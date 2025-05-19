package com.toritark.app.data.ads.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class RewardedAdBonusesCountApiModel(
    @SerialName("count")
    val count: Int,
)
