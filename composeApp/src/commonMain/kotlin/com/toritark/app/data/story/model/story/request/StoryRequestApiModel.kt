package com.toritark.app.data.story.model.story.request

import com.toritark.app.data.story.model.story.story.StoryApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRequestApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("status")
    val status: Status,
    @SerialName("story")
    val story: StoryApiModel? = null,
    @SerialName("rating")
    val rating: Rating? = null,
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

    @Serializable
    enum class Rating {
        @SerialName("good")
        GOOD,

        @SerialName("bad")
        BAD,
    }
}
