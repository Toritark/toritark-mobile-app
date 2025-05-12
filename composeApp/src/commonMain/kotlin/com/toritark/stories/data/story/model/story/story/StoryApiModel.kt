package com.toritark.stories.data.story.model.story.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryApiModel(
    @SerialName("learning_language_text")
    val learningLanguageText: List<String>,
    @SerialName("native_language_text")
    val nativeLanguageText: List<String>,
    @SerialName("questions")
    val questions: List<StoryQuestionApiModel>,
)
