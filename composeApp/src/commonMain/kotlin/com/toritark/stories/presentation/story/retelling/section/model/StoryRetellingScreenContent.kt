package com.toritark.stories.presentation.story.retelling.section.model

import com.toritark.stories.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.stories.data.story.model.story.story.StoryApiModel

internal data class StoryRetellingScreenContent(
    val story: StoryApiModel? = null,
    val retellingText: String = "",
    val isSubmitButtonEnabled: Boolean = false,
    val reviewResult: ReviewResult = ReviewResult.None,
) {

    interface ReviewResult {
        data object None : ReviewResult
        data object InProgress : ReviewResult

        data class Ready(
            val result: StoryRetellingReviewApiModel,
        ) : ReviewResult
    }
}
