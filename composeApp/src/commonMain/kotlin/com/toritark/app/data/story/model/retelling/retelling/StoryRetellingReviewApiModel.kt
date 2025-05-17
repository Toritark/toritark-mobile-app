package com.toritark.app.data.story.model.retelling.retelling

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StoryRetellingReviewApiModel(
    @SerialName("overall_review")
    val overallReview: String,
    @SerialName("scores")
    val scores: StoryRetellingScoresApiModel,
    @SerialName("sentences")
    val sentences: List<StoryRetellingSentenceReviewApiModel>,
)
