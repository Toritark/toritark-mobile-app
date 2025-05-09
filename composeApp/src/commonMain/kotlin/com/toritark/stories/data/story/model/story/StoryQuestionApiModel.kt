package com.toritark.stories.data.story.model.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryQuestionApiModel(
    @SerialName("question")
    val question: String,
    @SerialName("answers")
    val answers: List<StoryQuestionAnswerApiModel>,
)
