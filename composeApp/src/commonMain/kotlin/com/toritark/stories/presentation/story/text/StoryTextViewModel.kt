package com.toritark.stories.presentation.story.text

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.story.text.model.StoryTextScreenContent
import kotlinx.coroutines.CoroutineDispatcher

internal class StoryTextViewModel(
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

        // TODO
    }

    private companion object {
        private const val LOG_TAG = "StoryTextViewModel"
    }
}