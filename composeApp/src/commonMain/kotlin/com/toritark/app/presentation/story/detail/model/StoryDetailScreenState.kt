package com.toritark.app.presentation.story.detail.model

import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.learning_words.component.dialog.model.RewardedAdWaitingDialogState
import com.toritark.app.presentation.story.model.StoryTopicUiModel

internal data class StoryDetailScreenState(
    val topics: List<StoryTopicUiModel> = emptyList(),
    val selectedTopic: StoryTopicUiModel? = null,
    val promptText: String = "",
    val isPromptInputVisible: Boolean = false,
    val isGenerateButtonEnabled: Boolean = false,
    val storyState: StoryState = StoryState.Empty,
    val rewardedAdWaitingDialogState: RewardedAdWaitingDialogState = RewardedAdWaitingDialogState.None,
) {

    sealed interface StoryState {
        data object Empty : StoryState
        data object Creating : StoryState
        data class Created(
            val storyRequestId: Long,
            val story: StoryApiModel,
        ) : StoryState
    }
}
