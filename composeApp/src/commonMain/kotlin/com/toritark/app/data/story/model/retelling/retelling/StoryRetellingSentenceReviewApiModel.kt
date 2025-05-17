package com.toritark.app.data.story.model.retelling.retelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRetellingSentenceReviewApiModel(
    @SerialName("status")
    val status: Status,
    @SerialName("sentence")
    val sentence: String,
    @SerialName("corrected_sentence")
    val correctedSentence: String? = null,
    @SerialName("explanation")
    val explanation: String? = null,
) {

    @Serializable
    enum class Status {
        @SerialName("correct")
        CORRECT,

        @SerialName("wrong")
        WRONG,
    }
}