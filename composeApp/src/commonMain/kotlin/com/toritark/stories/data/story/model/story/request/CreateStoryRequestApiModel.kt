package com.toritark.stories.data.story.model.story.request

import com.toritark.stories.data.language.model.LanguageLevel
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
    @SerialName("prompt")
    val prompt: String,
)
