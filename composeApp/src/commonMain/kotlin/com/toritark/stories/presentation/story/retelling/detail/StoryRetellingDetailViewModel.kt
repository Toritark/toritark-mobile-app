package com.toritark.stories.presentation.story.retelling.detail

import co.touchlab.kermit.Logger
import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.story.retelling.detail.model.StoryRetellingDetailScreenContent
import kotlinx.coroutines.CoroutineDispatcher

internal class StoryRetellingDetailViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryRetellingDetailScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryRetellingDetailScreenContent()
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun setReview(review: StoryRetellingReviewApiModel) {
        logger.d { "setReview: review=$review" }

        if (contentValue.review == review) return

        updateAndShowContent {
            copy(review = review)
        }
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        onPopBackStack()
    }

    private companion object {
        private const val LOG_TAG = "StoryRetellingDetailViewModel"
    }
}