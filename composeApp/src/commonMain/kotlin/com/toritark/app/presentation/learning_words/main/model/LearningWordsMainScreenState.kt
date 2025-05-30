package com.toritark.app.presentation.learning_words.main.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.learning_words.api.model.LearningStatsApiModel
import com.toritark.app.presentation.learning_words.main.model.sentence.SentenceWithParts

@Immutable
internal data class LearningWordsMainScreenState(
    val currentSentence: CurrentSentence = CurrentSentence.Loading,
    val learningStatsState: LearningStatsState = LearningStatsState.Empty,
    val showLearnedDialog: Boolean = false,
) {

    @Immutable
    sealed interface LearningStatsState {
        data object Empty : LearningStatsState

        @Immutable
        data class Present(
            val stats: LearningStatsApiModel,
        ) : LearningStatsState
    }

    @Immutable
    sealed interface CurrentSentence {
        data object Loading : CurrentSentence
        data object Empty : CurrentSentence

        @Immutable
        sealed interface Present : CurrentSentence {
            val sentence: SentenceWithParts
            val isComplete: Boolean

            @Immutable
            data class Todo(
                override val sentence: SentenceWithParts,
                override val isComplete: Boolean = false,
            ) : Present

            @Immutable
            data class Checking(
                override val sentence: SentenceWithParts,
                override val isComplete: Boolean = false,
            ) : Present
        }


    }
}
