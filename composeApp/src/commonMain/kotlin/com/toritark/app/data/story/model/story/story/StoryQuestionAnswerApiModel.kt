package com.toritark.app.data.story.model.story.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryQuestionAnswerApiModel(
    @SerialName("answer")
    val answer: String,
    @SerialName("is_correct")
    val isCorrect: Boolean,
)
