package com.toritark.app.presentation.story.retelling.detail

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.retelling.detail.model.StoryRetellingDetailScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class StoryRetellingDetailViewModel(
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryRetellingDetailScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryRetellingDetailScreenState()
) {
    override val logger = Logger.withTag(LOG_TAG)

    init {
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    fun setReview(review: StoryRetellingReviewApiModel) {
        logger.d { "setReview: review=$review" }

        if (contentValue.review == review) return

        updateAndShowContent {
            copy(review = review)
        }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "show_retelling_review_detail",
                parameters = mapOf(
                    "review_sentences_count" to review.sentences.size,
                    "review_score_overall" to review.scores.overall,
                    "review_score_completeness" to review.scores.completeness,
                    "review_score_grammar" to review.scores.grammar,
                    "review_score_vocabulary" to review.scores.vocabulary,
                    "review_score_spelling" to review.scores.spelling,
                    "review_score_punctuation" to review.scores.punctuation,
                )
            )
        )
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "retelling_review_detail_close",
            )
        )

        onPopBackStack()
    }

    private companion object {
        private const val LOG_TAG = "StoryRetellingDetailViewModel"

        private const val SCREEN_NAME = "StoryRetellingDetail"
    }
}