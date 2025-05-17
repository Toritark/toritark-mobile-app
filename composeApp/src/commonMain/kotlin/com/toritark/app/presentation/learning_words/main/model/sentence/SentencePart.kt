package com.toritark.app.presentation.learning_words.main.model.sentence

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SentencePart {

    @Immutable
    data class Text(
        val text: String,
    ) : SentencePart

    @Immutable
    data class Input(
        val state: State = State.EMPTY,
        val currentText: String = "",
        val correctText: String,
    ) : SentencePart {

        @Immutable
        enum class State {
            EMPTY,
            INCORRECT,
            CORRECT,
        }
    }
}
