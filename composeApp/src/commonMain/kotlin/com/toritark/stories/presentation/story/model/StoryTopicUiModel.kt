package com.toritark.stories.presentation.story.model

import com.toritark.stories.data.story.model.topic.StoryTopic
import org.jetbrains.compose.resources.StringResource

internal data class StoryTopicUiModel(
    val storyTopic: StoryTopic,
    val nameResource: StringResource,
)
