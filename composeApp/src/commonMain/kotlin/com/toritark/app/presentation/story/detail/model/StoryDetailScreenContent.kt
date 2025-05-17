package com.toritark.app.presentation.story.detail.model

import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.story.model.StoryTopicUiModel

internal data class StoryDetailScreenContent(
    val topics: List<StoryTopicUiModel> = emptyList(),
    val selectedTopic: StoryTopicUiModel? = null,
    val promptText: String = "",
    val isPromptInputVisible: Boolean = false,
    val isGenerateButtonEnabled: Boolean = false,
    val storyState: StoryState = StoryState.Empty,
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
