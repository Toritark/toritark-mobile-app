package com.toritark.app.data.billing.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
    @SerialName("stories_per_day")
    val storiesPerDay: Int,
    @SerialName("retellings_per_day")
    val retellingsPerDay: Int,
    @SerialName("audio_stories_per_day")
    val audioStoriesPerDay: Int,
    @SerialName("is_free")
    val isFree: Boolean,
    @SerialName("is_default")
    val isDefault: Boolean,
)
