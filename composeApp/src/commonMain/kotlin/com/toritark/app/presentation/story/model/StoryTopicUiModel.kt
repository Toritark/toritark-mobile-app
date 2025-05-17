package com.toritark.app.presentation.story.model

import com.toritark.app.data.story.model.topic.StoryTopic
import org.jetbrains.compose.resources.StringResource

internal data class StoryTopicUiModel(
    val storyTopic: StoryTopic,
    val nameResource: StringResource,
)
