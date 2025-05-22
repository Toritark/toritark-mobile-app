package com.toritark.app.presentation.story.model

import com.toritark.app.data.story.model.story.story.StoryTopicApiModel
import org.jetbrains.compose.resources.StringResource
import toritark.composeapp.generated.resources.*

internal data class StoryTopicUiModel(
    val storyTopic: StoryTopicApiModel,
    val nameResource: StringResource,
)

internal val storyTopics by lazy {
    listOf(
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.DAILY_ROUTINE,
            nameResource = Res.string.title_story_topic_daily_routine,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.STORE_DIALOG,
            nameResource = Res.string.title_story_topic_store_dialog,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.FAVORITE_ANIMAL,
            nameResource = Res.string.title_story_topic_favorite_animal,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.WALK,
            nameResource = Res.string.title_story_topic_walk,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.MEETING_NEW_FRIEND,
            nameResource = Res.string.title_story_topic_meeting_new_friend,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.SPECIAL_DAY,
            nameResource = Res.string.title_story_topic_special_day,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.MY_DREAM,
            nameResource = Res.string.title_story_topic_my_dream,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.ROOM,
            nameResource = Res.string.title_story_topic_room,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.MY_FAMILY,
            nameResource = Res.string.title_story_topic_family,
        ),
        StoryTopicUiModel(
            storyTopic = StoryTopicApiModel.CUSTOM,
            nameResource = Res.string.title_story_topic_custom,
        ),
    )
}