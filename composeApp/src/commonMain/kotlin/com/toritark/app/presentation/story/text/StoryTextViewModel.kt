package com.toritark.app.presentation.story.text

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.learning_words.data.model.SentenceToLearn
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.text.model.StoryTextScreenContent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getPluralString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.message_added_selected_words_to_learning

internal class StoryTextViewModel(
    private val learningWordsInteractor: LearningWordsInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryTextScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryTextScreenContent(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        updateAndShowContent {
            copy(story = story)
        }
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        onPopBackStack()
    }

    fun onAddWordsToLearningSetClick(words: Set<String>) {
        logger.d { "onAddWordsToLearningSetClick: words=$words" }

        val story = contentValue.story ?: return

        viewModelScope.launch {
            val sentences = story.learningLanguageText.mapIndexed { index, learningLanguageText ->
                SentenceToLearn(
                    learningLanguageText = learningLanguageText,
                    nativeLanguageText = story.nativeLanguageText[index],
                )
            }.toSet()

            learningWordsInteractor
                .addWords(
                    words = words,
                    sentences = sentences,
                )
                .onErrorShowMessage()
                .collect {
                    logger.d { "onAddWordsToLearningSetClick: added" }

                    showSnackBarMessage(
                        getPluralString(
                            Res.plurals.message_added_selected_words_to_learning,
                            words.size,
                            words.size,
                        )
                    )
                }
        }
    }

    private companion object {
        private const val LOG_TAG = "StoryTextViewModel"
    }
}