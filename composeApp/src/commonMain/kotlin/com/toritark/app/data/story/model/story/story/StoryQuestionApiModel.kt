package com.toritark.app.data.story.model.story.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryQuestionApiModel(
    @SerialName("question")
    val question: String,
    @SerialName("answers")
    val answers: List<StoryQuestionAnswerApiModel>,
)
