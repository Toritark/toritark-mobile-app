package com.toritark.app.presentation.story.retelling.section

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.story.model.retelling.request.StoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.domain.story.interactor.StoriesInteractor
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.nav.StoryNavDestination
import com.toritark.app.presentation.story.retelling.section.model.StoryRetellingScreenContent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

internal class StoryRetellingViewModel(
    private val storiesInteractor: StoriesInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryRetellingScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryRetellingScreenContent()
) {
    override val logger = Logger.withTag(LOG_TAG)

    private var storyRequestId: Long = 0

    fun setStory(storyRequestId: Long, story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        if (contentValue.story == story) return

        this.storyRequestId = storyRequestId

        updateAndShowContent {
            StoryRetellingScreenContent(
                story = story,
            )
        }
    }

    fun onTextChange(text: String) {
        logger.d { "onTextChanged: text=$text" }

        updateAndShowContent {
            copy(
                retellingText = text,
                isSubmitButtonEnabled = text.isNotBlank()
            )
        }
    }

    fun onSubmitButtonClick() {
        logger.d { "onSubmitButtonClick" }

        val retelling = contentValue.retellingText.takeIf { it.isNotBlank() } ?: return

        updateAndShowContent {
            copy(reviewResult = StoryRetellingScreenContent.ReviewResult.InProgress)
        }

        viewModelScope.launch {
            storiesInteractor
                .createStoryRetellingReview(storyRequestId, retelling)
                .onErrorShowMessage()  // FIXME: Not displayed currently!
                // FIXME: Handle quota exceeded
                .collect { request ->
                    logger.d { "onSubmitButtonClick: request=$request" }

                    startWatchingReview(requestId = request.id)
                }
        }
    }

    private fun startWatchingReview(requestId: Long) {
        viewModelScope.launch(defaultDispatcher) {
            var isGenerating = true

            while (isGenerating) {
                delay(WATCH_REVIEW_CHECK_INTERVAL_MS)

                storiesInteractor
                    .getStoryRetellingReview(requestId = requestId)
                    .catch { t ->
                        logger.w(t) { "startWatchingReview: error=${t.message}" }
                    }
                    .collect { request ->
                        when (request.status) {
                            StoryRetellingReviewRequestApiModel.Status.PENDING,
                            StoryRetellingReviewRequestApiModel.Status.CREATING,
                                -> {
                                logger.d { "startWatchingReview: creating" }

                            }

                            StoryRetellingReviewRequestApiModel.Status.CREATED -> {
                                isGenerating = false

                                onReviewReady(request)
                            }

                            StoryRetellingReviewRequestApiModel.Status.FAILED -> {
                                isGenerating = false

                                onReviewFailed(request)
                            }
                        }
                    }
            }
        }
    }

    private fun onReviewReady(request: StoryRetellingReviewRequestApiModel) {
        logger.d { "onReviewReady: request=$request" }

        updateAndShowContent {
            copy(
                reviewResult = when (request.review) {
                    null -> StoryRetellingScreenContent.ReviewResult.None
                    else -> StoryRetellingScreenContent.ReviewResult.Ready(
                        result = request.review,
                    )
                },
            )
        }
    }

    private fun onReviewFailed(request: StoryRetellingReviewRequestApiModel) {
        logger.d { "onReviewFailed: request=$request" }

        updateAndShowContent {
            copy(reviewResult = StoryRetellingScreenContent.ReviewResult.None)
        }

        // TODO: Show error
    }

    fun onDetailsClick() {
        logger.d { "onDetailsClick" }

        val review = (contentValue.reviewResult as? StoryRetellingScreenContent.ReviewResult.Ready)?.result ?: return

        onNavigateTo(StoryNavDestination.RetellingReviewDetail(review = review)) {}
    }

    private companion object {
        private const val LOG_TAG = "StoryRetellingViewModel"

        private const val WATCH_REVIEW_CHECK_INTERVAL_MS = 300L

    }
}