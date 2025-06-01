package com.toritark.app.presentation.story.detail

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.ads.model.rewarded.RewardedVideoKind
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.data.story.exception.QuotaExceededException
import com.toritark.app.data.story.model.story.request.StoryRequestApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.data.story.model.story.story.StoryTopicApiModel
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.domain.story.interactor.StoriesInteractor
import com.toritark.app.presentation.billing.nav.BillingNavDestination
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.learning_words.component.dialog.model.RewardedAdWaitingDialogState
import com.toritark.app.presentation.story.detail.model.StoryDetailScreenState
import com.toritark.app.presentation.story.model.QuotaExceededMessage
import com.toritark.app.presentation.story.model.StoryTopicUiModel
import com.toritark.app.presentation.story.model.storyTopics
import com.toritark.app.presentation.story.nav.StoryNavDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_monetization_generation_quota_exceeded
import toritark.composeapp.generated.resources.desc_monetization_generation_quota_exceeded_no_ads
import toritark.composeapp.generated.resources.title_monetization_generation_quota_exceeded

internal class StoryDetailViewModel(
    private val storiesInteractor: StoriesInteractor,
    private val profileInteractor: ProfileInteractor,
    private val adsInteractor: AdsInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryDetailScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryDetailScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _showGenerationQuotaExceededDialog = MutableSharedFlow<QuotaExceededMessage>()
    val showGenerationQuotaExceededDialog = _showGenerationQuotaExceededDialog.asSharedFlow()

    init {
        initialize()

        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    private fun initialize() {
        initializeStoryTopics()
    }

    private fun initializeStoryTopics() {
        updateAndShowContent {
            copy(
                topics = storyTopics,
                selectedTopic = storyTopics.first(),
            )
        }
        onPromptChange("")
    }

    fun onStoryTopicSelected(storyTopic: StoryTopicUiModel) {
        logger.d { "onStoryTopicSelected: storyTopic=${storyTopic.storyTopic}" }

        updateAndShowContent {
            copy(
                selectedTopic = storyTopic,
                isPromptInputVisible = storyTopic.storyTopic == StoryTopicApiModel.CUSTOM,
            )
        }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "story_topic_selected",
                parameters = mapOf(
                    "topic" to storyTopic.storyTopic.value,
                ),
            )
        )
    }

    fun onPromptChange(prompt: String) {
        logger.d { "onPromptChange: newPrompt=$prompt" }

        updateAndShowContent {
            copy(
                promptText = prompt,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(promptText = prompt),
            )
        }
    }

    private fun isGeneratedButtonEnabled(
        promptText: String = contentValue.promptText,
        storyState: StoryDetailScreenState.StoryState = contentValue.storyState,
    ): Boolean {
        val isPromptOk = when (contentValue.selectedTopic?.storyTopic) {
            null -> false
            StoryTopicApiModel.CUSTOM -> promptText.isNotBlank()
            else -> true
        }
        return isPromptOk && storyState !is StoryDetailScreenState.StoryState.Creating
    }

    fun onGenerateStoryClick() {
        logger.d { "onGenerateStoryClick" }

        generateStory()

        Analytics.logEvent(
            AnalyticsEvent(
                name = "generate_story_click",
                parameters = mapOf(
                    "topic" to contentValue.selectedTopic?.storyTopic?.value,
                ),
            )
        )
    }

    private fun generateStory() {
        logger.d { "generateStory" }

        val topic = contentValue.selectedTopic?.storyTopic ?: return
        val prompt = contentValue.promptText.takeIf { topic == StoryTopicApiModel.CUSTOM && it.isNotBlank() }

        updateAndShowContent {
            copy(
                storyState = StoryDetailScreenState.StoryState.Creating,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = StoryDetailScreenState.StoryState.Creating),
            )
        }

        viewModelScope.launch(defaultDispatcher) {
            storiesInteractor
                .createStory(
                    topic = topic,
                    prompt = prompt,
                )
                .catch { t ->
                    if (t is QuotaExceededException) {
                        onStoryGenerationQuotaExceeded()
                    } else {
                        throw t
                    }
                }
                .onErrorShowMessage()
                .collect { storyRequest ->
                    logger.d { "onGenerateStoryClick: storyRequest=$storyRequest" }

                    startWatchingStory(storyId = storyRequest.id)
                }
        }
    }

    private fun startWatchingStory(storyId: Long) {
        logger.d { "startWatchingStory: storyId=$storyId" }

        viewModelScope.launch(defaultDispatcher) {
            var isGenerating = true

            while (isGenerating) {
                delay(WATCH_STORY_CHECK_INTERVAL_MS)

                storiesInteractor
                    .getStory(storyId)
                    .catch { t ->
                        if (t is QuotaExceededException) {
                            onStoryGenerationQuotaExceeded()
                        }

                        logger.w(t) { "startWatchingStory: error=${t.message}" }
                    }
                    .collect { story ->
                        when (story.status) {
                            StoryRequestApiModel.Status.PENDING,
                            StoryRequestApiModel.Status.CREATING,
                                -> {
                                logger.d { "startWatchingStory: creating" }
                            }

                            StoryRequestApiModel.Status.CREATED -> {
                                isGenerating = false

                                onStoryGenerated(storyRequest = story)
                            }

                            StoryRequestApiModel.Status.FAILED -> {
                                isGenerating = false

                                onStoryGenerationError(storyRequest = story)
                            }
                        }
                    }
            }
        }
    }

    private fun onStoryGenerated(storyRequest: StoryRequestApiModel) {
        logger.d { "onStoryGenerated: storyRequest=$storyRequest" }

        updateAndShowContent {
            val newStoryState = when (storyRequest.story) {
                null -> StoryDetailScreenState.StoryState.Empty
                else -> StoryDetailScreenState.StoryState.Created(
                    storyRequestId = storyRequest.id,
                    story = storyRequest.story
                )
            }
            copy(
                storyState = newStoryState,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = newStoryState),
            )
        }
    }

    private fun onStoryGenerationError(storyRequest: StoryRequestApiModel) {
        logger.w { "onStoryGenerationError: storyRequest=$storyRequest" }

        updateAndShowContent {
            copy(
                storyState = StoryDetailScreenState.StoryState.Empty,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = StoryDetailScreenState.StoryState.Empty),
            )
        }

        // TODO: Show error
    }

    private fun onStoryGenerationQuotaExceeded() {
        logger.w { "onStoryGenerationQuotaExceeded" }

        updateAndShowContent {
            copy(
                storyState = StoryDetailScreenState.StoryState.Empty,
                isGenerateButtonEnabled = isGeneratedButtonEnabled(storyState = StoryDetailScreenState.StoryState.Empty),
            )
        }

        profileInteractor.updateProfileInBackground()

        viewModelScope.launch(defaultDispatcher) {
            val plan = profileInteractor
                .profileState
                .filterIsInstance<ProfileState.Present>()
                .map { profileState -> profileState.profile.plan }
                .first()

            val canShowRewardedAd = adsInteractor.canShowRewardedAd(AdPlacement.Rewarded.Generation)
            val text = if (canShowRewardedAd) {
                getPluralString(
                    Res.plurals.desc_monetization_generation_quota_exceeded,
                    plan.storiesPerDay,
                    plan.storiesPerDay,
                    plan.name,
                )
            } else {
                getPluralString(
                    Res.plurals.desc_monetization_generation_quota_exceeded_no_ads,
                    plan.storiesPerDay,
                    plan.storiesPerDay,
                    plan.name,
                )
            }

            val message = QuotaExceededMessage(
                title = getString(Res.string.title_monetization_generation_quota_exceeded),
                text = text,
                canShowRewardedAd = canShowRewardedAd,
            )

            logger.d { "onStoryGenerationQuotaExceeded: message=$message" }

            _showGenerationQuotaExceededDialog.emit(message)

            Analytics.logEvent(
                AnalyticsEvent(
                    name = "generate_story_quota_exceeded",
                    parameters = mapOf(
                        "topic" to contentValue.selectedTopic?.storyTopic?.value,
                        "can_show_rewarded_ad" to canShowRewardedAd,
                        "plan_is_free" to plan.isFree,
                        "plan_name" to plan.name,
                    ),
                )
            )
        }
    }

    fun onQuotaExceededDialogClosed() {
        logger.d { "onQuotaExceededDialogClosed" }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "generate_story_quota_exceeded_dialog_closed",
                parameters = mapOf(
                    "topic" to contentValue.selectedTopic?.storyTopic?.value,
                ),
            )
        )
    }

    fun onWatchAdToUnlockClick() {
        logger.d { "onWatchAdToUnlockClick" }

        updateAndShowContent {
            copy(
                rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Waiting,
            )
        }

        viewModelScope.launch(defaultDispatcher) {
            Analytics.logEvent(
                AnalyticsEvent(
                    name = "generate_story_watch_ad_click",
                    parameters = mapOf(
                        "topic" to contentValue.selectedTopic?.storyTopic?.value,
                    ),
                )
            )

            adsInteractor
                .showRewardedAd(RewardedVideoKind.Generation)
                .catch { t ->
                    logger.w(t) { "onWatchAdToUnlockClick: error=${t.message}" }

                    updateAndShowContent {
                        copy(
                            rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Fail,
                        )
                    }

                    Analytics.logEvent(
                        AnalyticsEvent(
                            name = "generate_story_watch_ad_failed",
                            parameters = mapOf(
                                "topic" to contentValue.selectedTopic?.storyTopic?.value,
                            ),
                        )
                    )
                }
                .collect {
                    logger.d { "onWatchAdToUnlockClick: result=$it" }

                    updateAndShowContent {
                        copy(
                            rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.Success,
                        )
                    }

                    Analytics.logEvent(
                        AnalyticsEvent(
                            name = "generate_story_watch_ad_success",
                            parameters = mapOf(
                                "topic" to contentValue.selectedTopic?.storyTopic?.value,
                            ),
                        )
                    )
                }
        }
    }

    fun onUpgradePlanToUnlockClick() {
        logger.d { "onUpgradePlanToUnlockClick" }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "generate_story_upgrade_click",
                parameters = mapOf(
                    "topic" to contentValue.selectedTopic?.storyTopic?.value,
                ),
            )
        )

        onNavigateTo(BillingNavDestination.Paywall(source = PaywallSource.StoryGenerationQuota)) {}
    }

    fun onRewardedAdWaitingDialogOkClick() {
        logger.d { "onRewardedAdWaitingDialogOkClick" }

        if (contentValue.rewardedAdWaitingDialogState == RewardedAdWaitingDialogState.Success) {
            logger.d { "onRewardedAdWaitingDialogOkClick: generating story again." }
            generateStory()
        }

        updateAndShowContent {
            copy(
                rewardedAdWaitingDialogState = RewardedAdWaitingDialogState.None,
            )
        }
    }

    fun onStoryClick() {
        logger.d { "onStoryClick" }

        val story = story ?: return

        Analytics.logEvent(
            AnalyticsEvent(
                name = "generated_story_click",
                parameters = mapOf(
                    "topic" to contentValue.selectedTopic?.storyTopic?.value,
                ),
            )
        )

        onNavigateTo(
            StoryNavDestination.Text(
                story = story,
            )
        ) {}
    }

    fun onStoryQuestionsClick() {
        logger.d { "onStoryQuestionsClick" }

        val story = story ?: return

        Analytics.logEvent(
            AnalyticsEvent(
                name = "generated_story_questions_click",
                parameters = mapOf(
                    "topic" to contentValue.selectedTopic?.storyTopic?.value,
                ),
            )
        )

        onNavigateTo(
            StoryNavDestination.Quiz(
                story = story,
            )
        ) {}
    }

    fun onBannerViewReady() {
        logger.d { "onBannerViewReady" }

        adsInteractor.showBannerAd(AdPlacement.Banner.Story.Main)
    }

    private val story: StoryApiModel?
        get() {
            return when (val storyState = contentValue.storyState) {
                is StoryDetailScreenState.StoryState.Created -> storyState.story
                else -> null
            }
        }

    private companion object {
        private const val LOG_TAG = "StoryDetailViewModel"

        private const val SCREEN_NAME = "StoryDetail"

        private const val WATCH_STORY_CHECK_INTERVAL_MS = 300L
    }
}
