package com.toritark.stories.presentation.story.detail.model

import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.presentation.story.model.StoryTopicUiModel

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
            val story: StoryApiModel,
        ) : StoryState
    }
}
