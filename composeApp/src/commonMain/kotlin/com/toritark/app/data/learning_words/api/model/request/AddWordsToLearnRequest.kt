package com.toritark.app.data.learning_words.api.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AddWordsToLearnRequest(
    @SerialName("learning_language")
    val learningLanguageCode: String,
    @SerialName("native_language")
    val nativeLanguageCode: String,
    @SerialName("words")
    val words: Set<String>,
    @SerialName("sentences")
    val sentences: List<SentenceToLearnCreateApiModel>,
)
