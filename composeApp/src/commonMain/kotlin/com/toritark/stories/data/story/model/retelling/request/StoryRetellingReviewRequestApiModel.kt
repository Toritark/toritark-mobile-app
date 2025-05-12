package com.toritark.stories.data.story.model.retelling.request

import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRetellingReviewRequestApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("status")
    val status: Status,
    @SerialName("review")
    val review: StoryRetellingReviewApiModel? = null,
) {
    @Serializable
    enum class Status {
        @SerialName("pending")
        PENDING,

        @SerialName("creating")
        CREATING,

        @SerialName("created")
        CREATED,

        @SerialName("failed")
        FAILED,
    }
}
