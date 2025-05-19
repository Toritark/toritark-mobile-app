package com.toritark.app.presentation.billing.paywall.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class PaywallSource(
    @SerialName("name")
    val name: String,
) {

    @Serializable
    data object Unknown : PaywallSource("unknown")

    @Serializable
    data object TopBar : PaywallSource("top_bar")

    @Serializable
    data object Profile : PaywallSource("profile")

    @Serializable
    data object StoryOffering : PaywallSource("story_offering")

    @Serializable
    data object StoryGenerationQuota : PaywallSource("story_generation_quota")

    @Serializable
    data object StoryRetellingQuota : PaywallSource("story_retelling_quota")

    @Serializable
    data object QuizOffering : PaywallSource("quiz_offering")

    @Serializable
    data object LearningWordsOffering : PaywallSource("learning_words_offering")
}