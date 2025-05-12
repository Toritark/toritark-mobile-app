package com.toritark.stories.data.story.model.retelling.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CreateStoryRetellingReviewRequestApiModel(
    @SerialName("story_request_id")
    val storyRequestId: Long,
    @SerialName("retelling")
    val retelling: String,
)
