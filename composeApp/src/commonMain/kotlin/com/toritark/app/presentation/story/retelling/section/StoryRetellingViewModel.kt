package com.toritark.app.presentation.story.retelling.section

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.ads.model.rewarded.RewardedVideoKind
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.data.story.exception.QuotaExceededException
import com.toritark.app.data.story.model.retelling.request.StoryRetellingReviewRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.domain.story.interactor.StoriesInteractor
import com.toritark.app.presentation.billing.nav.BillingNavDestination
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.learning_words.component.dialog.model.RewardedAdWaitingDialogState
import com.toritark.app.presentation.story.model.QuotaExceededMessage
import com.toritark.app.presentation.story.nav.StoryNavDestination
import com.toritark.app.presentation.story.retelling.section.model.StoryRetellingScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_monetization_retelling_check_quota_exceeded
import toritark.composeapp.generated.resources.desc_monetization_retelling_check_quota_exceeded_no_ads
import toritark.composeapp.generated.resources.title_monetization_retelling_check_quota_exceeded

internal class StoryRetellingViewModel(
    private val storiesInteractor: StoriesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val adsInteractor: AdsInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryRetellingScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryRetellingScreenState()
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _showGenerationQuotaExceededDialog = MutableSharedFlow<QuotaExceededMessage>()
    val showRetellingCheckQuotaExceededDialog = _showGenerationQuotaExceededDialog.asSharedFlow()

    private var storyRequestId: Long = 0

    fun setStory(storyRequestId: Long, story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        if (contentValue.story == story) return

        this.storyRequestId = storyRequestId

        updateAndShowContent {
            StoryRetellingScreenState(
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

        checkRetelling()
    }

    private fun checkRetelling() {
        logger.d { "checkRetelling" }

        val retelling = contentValue.retellingText.takeIf { it.isNotBlank() } ?: return

        updateAndShowContent {
            copy(reviewResult = StoryRetellingScreenState.ReviewResult.InProgress)
        }

        viewModelScope.launch {
            storiesInteractor
                .createStoryRetellingReview(storyRequestId, retelling)
                .catch { t ->
                    if (t is QuotaExceededException) {
                        onRetellingCheckQuotaExceeded()
                    } else {
                        throw t
                    }
                }
                .onErrorShowMessage()
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
                    null -> StoryRetellingScreenState.ReviewResult.None
                    else -> StoryRetellingScreenState.ReviewResult.Ready(
                        result = request.review,
                    )
                },
            )
        }
    }

    private fun onRetellingCheckQuotaExceeded() {
        logger.w { "onRetellingCheckQuotaExceeded" }

        updateAndShowContent {
            copy(reviewResult = StoryRetellingScreenState.ReviewResult.None)
        }

        viewModelScope.launch {
            val plan = profileInteractor
                .profileState
                .filterIsInstance<ProfileState.Present>()
                .map { profileState -> profileState.profile.plan }
                .first()

            val canShowRewardedAd = adsInteractor.canShowRewardedAd(AdPlacement.Rewarded.RetellingCheck)
            val text = if (canShowRewardedAd) {
                getPluralString(
                    Res.plurals.desc_monetization_retelling_check_quota_exceeded,
                    plan.storiesPerDay,
                    plan.storiesPerDay,
                    plan.name,
                )
            } else {
                getPluralString(
                    Res.plurals.desc_monetization_retelling_check_quota_exceeded_no_ads,
                    plan.storiesPerDay,
                    plan.storiesPerDay,
                    plan.name,
                )
            }

            val message = QuotaExceededMessage(
                title = getString(Res.string.title_monetization_retelling_check_quota_exceeded),
                text = text,
                canShowRewardedAd = canShowRewardedAd,
            )

            logger.d { "onStoryGenerationQuotaExceeded: message=$message" }

            _showGenerationQuotaExceededDialog.emit(message)
        }
    }

    fun onQuotaExceededDialogClosed() {
        logger.d { "onQuotaExceededDialogClosed" }

        // TODO: Report to analytics
    }

    fun onWatchAdToUnlockClick() {
        logger.d { "onWatchAdToUnlockClick" }

        updateAndShowContent {
            copy(
                rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Waiting,
            )
        }

        viewModelScope.launch {
            adsInteractor
                .showRewardedAd(RewardedVideoKind.RetellingCheck)
                .catch { t ->
                    logger.w(t) { "onWatchAdToUnlockClick: error=${t.message}" }

                    updateAndShowContent {
                        copy(
                            rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Fail,
                        )
                    }
                }
                .collect {
                    logger.d { "onWatchAdToUnlockClick: result=$it" }

                    updateAndShowContent {
                        copy(
                            rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Success,
                        )
                    }
                }
        }
    }

    fun onUpgradePlanToUnlockClick() {
        logger.d { "onUpgradePlanToUnlockClick" }

        // TODO: Report to analytics

        onNavigateTo(BillingNavDestination.Paywall(source = PaywallSource.StoryRetellingQuota)) {}
    }

    fun onRewardedAdWaitingDialogOkClick() {
        logger.d { "onRewardedAdWaitingDialogOkClick" }

        if (contentValue.rewardedAdWaitingDialogState == RewardedAdWaitingDialogState.Success) {
            logger.d { "onRewardedAdWaitingDialogOkClick: generating story again." }
            checkRetelling()
        }

        updateAndShowContent {
            copy(
                rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.None,
            )
        }
    }

    private fun onReviewFailed(request: StoryRetellingReviewRequestApiModel) {
        logger.d { "onReviewFailed: request=$request" }

        updateAndShowContent {
            copy(reviewResult = StoryRetellingScreenState.ReviewResult.None)
        }

        // TODO: Show error
    }

    fun onDetailsClick() {
        logger.d { "onDetailsClick" }

        val review = (contentValue.reviewResult as? StoryRetellingScreenState.ReviewResult.Ready)?.result ?: return

        onNavigateTo(StoryNavDestination.RetellingReviewDetail(review = review)) {}
    }

    private companion object {
        private const val LOG_TAG = "StoryRetellingViewModel"

        private const val WATCH_REVIEW_CHECK_INTERVAL_MS = 300L

    }
}