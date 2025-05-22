package com.toritark.app.data.story.model.story.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StoryTopicApiModel {
    @SerialName("daily_routine")
    DAILY_ROUTINE,

    @SerialName("store_dialog")
    STORE_DIALOG,

    @SerialName("favorite_animal")
    FAVORITE_ANIMAL,

    @SerialName("walk")
    WALK,

    @SerialName("meeting_new_friend")
    MEETING_NEW_FRIEND,

    @SerialName("special_day")
    SPECIAL_DAY,

    @SerialName("my_dream")
    MY_DREAM,

    @SerialName("room")
    ROOM,

    @SerialName("my_family")
    MY_FAMILY,

    @SerialName("custom")
    CUSTOM,
}