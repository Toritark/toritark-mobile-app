package com.toritark.app.data.learning_words.api.model

import com.toritark.app.data.language.model.Language
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WordToLearnApiModel(
    @SerialName("id")
    val id: Long,
    @SerialName("language")
    val language: Language,
    @SerialName("text")
    val text: String,
    @SerialName("is_learned")
    val isLearned: Boolean,
    @SerialName("correct_attempts_count")
    val correctAttemptsCount: Int,
    @SerialName("incorrect_attempts_count")
    val incorrectAttemptsCount: Int,
)
