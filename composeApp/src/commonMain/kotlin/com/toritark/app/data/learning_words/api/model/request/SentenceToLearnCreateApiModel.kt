package com.toritark.app.data.learning_words.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SentenceToLearnCreateApiModel(
    @SerialName("learning_language_text")
    val learningLanguageText: String,
    @SerialName("native_language_text")
    val nativeLanguageText: String,
)
