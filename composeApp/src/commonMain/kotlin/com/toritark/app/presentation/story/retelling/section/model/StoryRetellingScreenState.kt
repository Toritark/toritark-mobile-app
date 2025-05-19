package com.toritark.app.presentation.story.retelling.section.model

import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.learning_words.component.dialog.model.RewardedAdWaitingDialogState

internal data class StoryRetellingScreenState(
    val story: StoryApiModel? = null,
    val retellingText: String = "",
    val isSubmitButtonEnabled: Boolean = false,
    val reviewResult: ReviewResult = ReviewResult.None,
    val rewardedAdWaitingDialogState: RewardedAdWaitingDialogState = RewardedAdWaitingDialogState.None,
) {

    interface ReviewResult {
        data object None : ReviewResult
        data object InProgress : ReviewResult

        data class Ready(
            val result: StoryRetellingReviewApiModel,
        ) : ReviewResult
    }
}
