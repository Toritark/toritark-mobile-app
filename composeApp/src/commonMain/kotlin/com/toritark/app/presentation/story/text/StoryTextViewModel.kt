package com.toritark.app.presentation.story.text

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.learning_words.model.SentenceToLearn
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.presentation.billing.nav.BillingNavDestination
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.story.text.model.StoryTextScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.getPluralString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.message_added_selected_words_to_learning

internal class StoryTextViewModel(
    private val learningWordsInteractor: LearningWordsInteractor,
    private val adsInteractor: AdsInteractor,
    private val billingInteractor: BillingInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<StoryTextScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = StoryTextScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    init {
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)

            Analytics.logEvent(
                AnalyticsEvent(
                    name = "show_generated_story",
                )
            )
        }
    }

    fun setStory(story: StoryApiModel) {
        logger.d { "setStory: story=$story" }

        updateAndShowContent {
            copy(story = story)
        }
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        Analytics.logEvent(
            AnalyticsEvent(
                name = "show_generated_story_close",
            )
        )

        viewModelScope.launch(defaultDispatcher) {
            if (billingInteractor.shouldShowPaywall()) {
                withContext(mainDispatcher) {
                    onNavigateTo(BillingNavDestination.Paywall(PaywallSource.StoryOffering)) {}
                }
            } else {
                withContext(mainDispatcher) {
                    onPopBackStack()
                }
            }
        }
    }

    fun onAddWordsToLearningSetClick(words: Set<String>) {
        logger.d { "onAddWordsToLearningSetClick: words=$words" }

        val story = contentValue.story ?: return

        viewModelScope.launch(defaultDispatcher) {
            val sentences = story.learningLanguageText.mapIndexed { index, learningLanguageText ->
                SentenceToLearn(
                    learningLanguageText = learningLanguageText,
                    nativeLanguageText = story.nativeLanguageText[index],
                )
            }.toSet()

            Analytics.logEvent(
                AnalyticsEvent(
                    name = "add_words_to_learning_set",
                    parameters = mapOf(
                        "words_count" to words.size,
                        "sentences_count" to sentences.size,
                    )
                )
            )

            learningWordsInteractor
                .addWords(
                    words = words,
                    sentences = sentences,
                )
                .onErrorShowMessage()
                .collect {
                    logger.d { "onAddWordsToLearningSetClick: added" }

                    showSnackBarMessage(
                        getPluralString(
                            Res.plurals.message_added_selected_words_to_learning,
                            words.size,
                            words.size,
                        )
                    )
                }
        }
    }

    fun onBannerViewReady() {
        logger.d { "onBannerViewReady" }

        adsInteractor.showBannerAd(AdPlacement.Banner.Story.Text)
    }

    private companion object {
        private const val LOG_TAG = "StoryTextViewModel"

        private const val SCREEN_NAME = "StoryText"
    }
}