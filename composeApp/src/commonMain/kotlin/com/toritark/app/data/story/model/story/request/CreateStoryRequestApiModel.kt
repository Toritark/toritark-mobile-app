package com.toritark.app.data.story.model.story.request

import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.data.story.model.story.story.StoryTopicApiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateStoryRequestApiModel(
    @SerialName("learning_language_code")
    val learningLanguageCode: String,
    @SerialName("native_language_code")
    val nativeLanguageCode: String,
    @SerialName("language_level")
    val languageLevel: LanguageLevel,
    @SerialName("topic")
    val topic: StoryTopicApiModel,
    @SerialName("prompt")
    val prompt: String?,
)
