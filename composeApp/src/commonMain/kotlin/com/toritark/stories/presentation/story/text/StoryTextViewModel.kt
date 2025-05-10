package com.toritark.stories.presentation.story.text

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class StoryTextViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _story = MutableStateFlow<StoryApiModel?>(null)
    val story = _story.asStateFlow()

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        _story.value = story
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        onPopBackStack()
    }

    private companion object {
        private const val LOG_TAG = "StoryTextViewModel"
    }
}