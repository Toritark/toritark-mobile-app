package com.toritark.app.data.learning_words.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LearningStatsApiModel(
    @SerialName("words")
    val words: Words,
) {
    val isEmpty: Boolean
        get() = words.isEmpty

    @Serializable
    data class Words(
        @SerialName("learned")
        val learned: Int,
        @SerialName("to_learn")
        val toLearn: Int,
        @SerialName("total")
        val total: Int,
    ) {
        val isEmpty: Boolean
            get() = learned == 0 && toLearn == 0 && total == 0
    }
}
