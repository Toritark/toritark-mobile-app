package com.toritark.stories.data.story.model.retelling.retelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRetellingSentenceReviewApiModel(
    @SerialName("sentence")
    val sentence: String,
    @SerialName("status")
    val status: Status,
    @SerialName("review")
    val review: String? = null,
) {

    @Serializable
    enum class Status {
        @SerialName("correct")
        CORRECT,

        @SerialName("insignificant_mistakes")
        INSIGNIFICANT_MISTAKES,

        @SerialName("significant_mistakes")
        SIGNIFICANT_MISTAKES,
    }
}