package com.toritark.app.data.story.model.story.story

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StoryTopicApiModel(
    val value: String,
) {
    @SerialName("daily_routine")
    DAILY_ROUTINE("daily_routine"),

    @SerialName("store_dialog")
    STORE_DIALOG("store_dialog"),

    @SerialName("favorite_animal")
    FAVORITE_ANIMAL("favorite_animal"),

    @SerialName("walk")
    WALK("walk"),

    @SerialName("meeting_new_friend")
    MEETING_NEW_FRIEND("meeting_new_friend"),

    @SerialName("special_day")
    SPECIAL_DAY("special_day"),

    @SerialName("my_dream")
    MY_DREAM("my_dream"),

    @SerialName("room")
    ROOM("room"),

    @SerialName("my_family")
    MY_FAMILY("my_family"),

    @SerialName("custom")
    CUSTOM("custom"),
}