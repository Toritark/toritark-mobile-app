package com.toritark.stories.presentation.story.retelling

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.story.retelling.model.StoryRetellingScreenContent
import kotlinx.coroutines.CoroutineDispatcher

internal class StoryRetellingViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryRetellingScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryRetellingScreenContent()
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        if (contentValue.story == story) return

        updateAndShowContent {
            copy(story = story)
        }
    }

    fun onTextChange(text: String) {
        logger.d { "onTextChanged: text=$text" }

        updateAndShowContent {
            copy(
                retellingText = text,
                isSubmitButtonEnabled = text.isNotBlank()
            )
        }
    }

    fun onSubmitButtonClick() {
        logger.d { "onSubmitButtonClick" }

        // TODO
    }


    private companion object {
        private const val LOG_TAG = "StoryRetellingViewModel"
    }
}