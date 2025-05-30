package com.toritark.app.data.learning_words.api.model

import com.toritark.app.data.language.model.Language
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SentenceToLearnApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("learning_language")
    val learningLanguage: Language,
    @SerialName("native_language")
    val nativeLanguage: Language,
    @SerialName("learning_language_text")
    val learningLanguageText: String,
    @SerialName("native_language_text")
    val nativeLanguageText: String,
    @SerialName("words")
    val words: List<WordToLearnApiModel>,
    @SerialName("is_learned")
    val isLearned: Boolean,
    @SerialName("correct_attempts_count")
    val correctAttemptsCount: Int,
    @SerialName("incorrect_attempts_count")
    val incorrectAttemptsCount: Int,
)
