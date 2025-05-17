package com.toritark.app.presentation.learning_words.main.model.sentence

import androidx.compose.runtime.Immutable

@Immutable
data class SentenceWithParts(
    val id: Long,
    val parts: List<SentencePart>,
    val nativeLanguageText: String,
) {
    val inputParts: Sequence<SentencePart.Input>
        get() {
            return parts
                .asSequence()
                .filterIsInstance<SentencePart.Input>()
        }

    val correctPartsCount: Int
        get() = inputParts.count { it.state == SentencePart.Input.State.CORRECT }

    val incorrectPartsCount: Int
        get() = inputParts.count { it.state == SentencePart.Input.State.INCORRECT }

    val areAllPartsComplete: Boolean
        get() = inputParts.all { it.state != SentencePart.Input.State.EMPTY }
}