package com.toritark.app.presentation.learning_words.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.learning_words.api.model.SentenceToLearnApiModel
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.presentation.billing.nav.BillingNavDestination
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.learning_words.main.model.LearningWordsMainScreenState
import com.toritark.app.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.app.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.app.presentation.main.nav.MainScreenDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.min

internal class LearningWordsMainViewModel(
    private val learningWordsInteractor: LearningWordsInteractor,
    private val adsInteractor: AdsInteractor,
    private val billingInteractor: BillingInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<LearningWordsMainScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = LearningWordsMainScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private var currentSentenceApiModel: SentenceToLearnApiModel? = null

    init {
        getNextSentence()
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    fun getNextSentence() {
        updateAndShowContent {
            copy(
                currentSentence = when (currentSentence) {
                    is LearningWordsMainScreenState.CurrentSentence.Present -> {
                        LearningWordsMainScreenState.CurrentSentence.Present.Checking(
                            sentence = currentSentence.sentence,
                            isComplete = currentSentence.sentence.areAllPartsComplete,
                        )
                    }

                    else -> LearningWordsMainScreenState.CurrentSentence.Loading
                }
            )
        }

        this.currentSentenceApiModel = null

        viewModelScope.launch(defaultDispatcher) {
            learningWordsInteractor
                .getNextSentenceToLearn()
                .onErrorShowMessage()
                .onEmpty {
                    showEmptyState()
                    updateLearningStats()
                }
                .collect { sentence ->
                    showSentenceToLearn(sentenceApiModel = sentence)
                    updateLearningStats()
                }
        }

        viewModelScope.launch(defaultDispatcher) {
            if (billingInteractor.shouldShowPaywall()) {
                withContext(mainDispatcher) {
                    onNavigateTo(BillingNavDestination.Paywall(PaywallSource.LearningWordsOffering)) {}
                }
            }
        }
    }

    private fun showEmptyState() {
        this.currentSentenceApiModel = null

        updateAndShowContent {
            copy(currentSentence = LearningWordsMainScreenState.CurrentSentence.Empty)
        }
    }

    private fun showSentenceToLearn(sentenceApiModel: SentenceToLearnApiModel) {
        logger.d { "showSentenceToLearn: sentenceApiModel=$sentenceApiModel" }

        val sentenceWithWords = getSentenceWithWords(sentenceApiModel = sentenceApiModel)
        this.currentSentenceApiModel = sentenceApiModel

        updateAndShowContent {
            copy(currentSentence = LearningWordsMainScreenState.CurrentSentence.Present.Todo(sentenceWithWords))
        }

        adsInteractor.showInterstitialAd(AdPlacement.Interstitial.LearningWords.Main)

        Analytics.logEvent(
            AnalyticsEvent(
                name = "learning_words_show_sentence",
                parameters = mapOf(
                    "learning_language" to sentenceApiModel.learningLanguage.isoCode,
                    "native_language" to sentenceApiModel.nativeLanguage.isoCode,
                    "correct_attempts" to sentenceApiModel.correctAttemptsCount,
                    "incorrect_attempts" to sentenceApiModel.incorrectAttemptsCount,
                    "total_attempts" to sentenceApiModel.correctAttemptsCount + sentenceApiModel.incorrectAttemptsCount,
                    "total_words_count" to sentenceApiModel.words.size,
                    "total_words_to_learn_count" to sentenceApiModel.words.count { !it.isLearned },
                )
            )
        )
    }

    private fun getSentenceWithWords(sentenceApiModel: SentenceToLearnApiModel): SentenceWithParts {
        val text = sentenceApiModel.learningLanguageText
        val words = sentenceApiModel
            .words
            .filterNot { it.isLearned }
            .map { word -> word.text }
            .filter { it.isNotBlank() }
            .toSet()

        if (words.isEmpty()) {
            logger.e { "getSentenceWithWords: words is empty" }
            return SentenceWithParts(
                id = sentenceApiModel.id,
                parts = listOf(SentencePart.Text(text)),
                nativeLanguageText = sentenceApiModel.nativeLanguageText,
            )
        }

        val parts = mutableListOf<SentencePart>()
        var currentIndex = 0

        val sortedEscapedWordsPattern = words
            .sortedByDescending { it.length }
            .joinToString("|") { Regex.escape(it) }

        val regex = Regex("\\b($sortedEscapedWordsPattern)\\b", RegexOption.IGNORE_CASE)
        val matches = regex.findAll(text)

        for (matchResult in matches) {
            val matchStart = matchResult.range.first
            val matchEnd = matchResult.range.last + 1 // matchResult.range.last is inclusive

            // Add the text part before the current match, if any.
            // This part includes original spacing, punctuation, etc., from the input `text`.
            if (matchStart > currentIndex) {
                val segment = text.substring(currentIndex, matchStart)
                parts.addAll(tokenizeTextSegment(segment))
            }

            SentencePart.Input(
                state = SentencePart.Input.State.EMPTY,
                correctText = matchResult.value
            ).let(parts::add)

            currentIndex = matchEnd
        }

        // Add any remaining text after the last match (or if no matches were found at all, this will add the entire text).
        if (currentIndex < text.length) {
            val segment = text.substring(currentIndex)
            parts.addAll(tokenizeTextSegment(segment))
        }

        return SentenceWithParts(
            id = sentenceApiModel.id,
            parts = parts,
            nativeLanguageText = sentenceApiModel.nativeLanguageText,
        ).also {
            logger.i { "getSentenceWithWords: $it" }
        }
    }

    private fun tokenizeTextSegment(segment: String): List<SentencePart.Text> {
        if (segment.isEmpty()) {
            return emptyList()
        }

        return tokenizerRegex
            .findAll(segment)
            .map { matchResult -> SentencePart.Text(matchResult.value) }
            .toList()
    }

    fun onGoToStoryScreenClick() {
        logger.d { "onGoToStoryScreenClick" }

        onNavigateTo(MainScreenDestination.Story) {}
    }

    fun onInputChange(partIndex: Int, text: String) {
        logger.d { "onInputChange: partIndex=$partIndex, text=$text" }

        val currentSentence =
            contentValue.currentSentence as? LearningWordsMainScreenState.CurrentSentence.Present.Todo
        if (currentSentence == null) {
            logger.e { "onInputChange: Failed to get current sentence: ${contentValue.currentSentence}" }
            return
        }

        val sentence = currentSentence.sentence

        val part = sentence.parts.getOrNull(partIndex)

        if (part == null) {
            logger.e { "Failed to get part with index $partIndex: $sentence" }
            return
        }

        if (part !is SentencePart.Input) {
            logger.e { "Part is not input: $part" }
            return
        }

        val textToCheck = text.trim().lowercase()

        val state = when {
            textToCheck == part.correctText.lowercase() -> SentencePart.Input.State.CORRECT
            textToCheck.length == part.correctText.length -> SentencePart.Input.State.INCORRECT
            else -> SentencePart.Input.State.EMPTY
        }

        val updatedPart = part.copy(
            state = state,
            currentText = text,
        )

        val updatedSentence = sentence.copy(
            parts = sentence.parts.toMutableList().apply {
                set(partIndex, updatedPart)
            },
        )

        updateAndShowContent {
            copy(
                currentSentence = currentSentence.copy(
                    sentence = updatedSentence,
                    isComplete = updatedSentence.areAllPartsComplete,
                )
            )
        }

        when (state) {
            SentencePart.Input.State.EMPTY -> {}
            SentencePart.Input.State.INCORRECT -> {
                logLearningWordsEvent("learning_words_input_incorrect")
            }

            SentencePart.Input.State.CORRECT -> {
                logLearningWordsEvent("learning_words_input_correct")
            }
        }
    }

    fun logLearningWordsEvent(eventName: String, parameters: Map<String, Any> = emptyMap()) {
        val correctAttempts = currentSentenceApiModel?.correctAttemptsCount
        val incorrectAttempts = currentSentenceApiModel?.incorrectAttemptsCount
        val totalAttempts = if (correctAttempts != null && incorrectAttempts != null) {
            correctAttempts + incorrectAttempts
        } else {
            null
        }

        Analytics.logEvent(
            AnalyticsEvent(
                name = eventName,
                parameters = mapOf(
                    "learning_language" to currentSentenceApiModel?.learningLanguage?.isoCode,
                    "native_language" to currentSentenceApiModel?.nativeLanguage?.isoCode,
                    "correct_attempts" to correctAttempts,
                    "incorrect_attempts" to incorrectAttempts,
                    "total_attempts" to totalAttempts,
                    "total_words_count" to currentSentenceApiModel?.words?.size,
                    "total_words_to_learn_count" to currentSentenceApiModel?.words?.count { !it.isLearned },
                ) + parameters
            )
        )
    }

    fun onNextClick() {
        logger.d { "onNextClick" }

        val currentSentence =
            contentValue.currentSentence as? LearningWordsMainScreenState.CurrentSentence.Present.Todo

        if (currentSentence == null) {
            logger.e { "onNextClick: Failed to get current sentence: ${contentValue.currentSentence}" }
            return
        }

        val currentSentenceApiModel = this.currentSentenceApiModel
        if (currentSentenceApiModel == null) {
            logger.e { "onNextClick: Current sentence db model is null!" }
            return
        }

        updateAndShowContent {
            copy(
                currentSentence = LearningWordsMainScreenState.CurrentSentence.Present.Checking(
                    sentence = currentSentence.sentence,
                    isComplete = currentSentence.sentence.areAllPartsComplete,
                )
            )
        }

        viewModelScope.launch(defaultDispatcher) {
            val sentence = currentSentence.sentence
            val showLearnedDialog = sentence.incorrectPartsCount == 0

            val correctWordsTexts = sentence
                .inputParts
                .asSequence()
                .filter { it.state == SentencePart.Input.State.CORRECT }
                .map { it.correctText.lowercase() }
                .toSet()

            val incorrectWordsTexts = sentence
                .inputParts
                .asSequence()
                .filter { it.state == SentencePart.Input.State.INCORRECT }
                .map { it.correctText.lowercase() }
                .toSet()

            val correctWords = currentSentenceApiModel
                .words
                .asSequence()
                .filter { word -> correctWordsTexts.contains(word.text) }
                .map { it.id }
                .toSet()

            val incorrectWords = currentSentenceApiModel
                .words
                .asSequence()
                .filter { word -> incorrectWordsTexts.contains(word.text) }
                .map { it.id }
                .toSet()

            learningWordsInteractor
                .updateSentenceResults(
                    sentenceId = currentSentenceApiModel.id,
                    correctWordsIds = correctWords,
                    incorrectWordsIds = incorrectWords,
                )
                .onErrorShowMessageAnd {
                    updateAndShowContent {
                        copy(
                            currentSentence = LearningWordsMainScreenState.CurrentSentence.Present.Todo(
                                sentence = currentSentence.sentence,
                                isComplete = currentSentence.sentence.areAllPartsComplete,
                            )
                        )
                    }
                }
                .collect {
                    logger.d { "onNextClick: updated sentence results" }

                    if (showLearnedDialog) {
                        updateAndShowContent {
                            copy(showLearnedDialog = true)
                        }
                    } else {
                        getNextSentence()
                    }
                }

            logLearningWordsEvent(eventName = "learning_words_next_click")
        }
    }

    fun onLearnedClick() {
        logger.d { "onLearnedClick" }

        updateAndShowContent {
            copy(showLearnedDialog = false)
        }

        val currentSentenceApiModel = this.currentSentenceApiModel
        if (currentSentenceApiModel == null) {
            logger.e { "onLearnedClick: Current sentence db model is null!" }
            return
        }

        viewModelScope.launch(defaultDispatcher) {
            logLearningWordsEvent(eventName = "learning_words_learned_click")

            learningWordsInteractor
                .setSentenceLearned(sentenceId = currentSentenceApiModel.id)
                .onErrorShowMessage()
                .collect {
                    logger.d { "onLearnedClick: set sentence learned" }

                    getNextSentence()
                }
        }
    }

    fun onNotLearnedClick() {
        logger.d { "onNotLearnedClick" }

        updateAndShowContent {
            copy(showLearnedDialog = false)
        }

        logLearningWordsEvent(eventName = "learning_words_not_learned_click")

        getNextSentence()
    }

    fun onHelpClick(partIndex: Int?) {
        logger.d { "onHelpClick: partIndex=$partIndex" }

        val currentSentence =
            contentValue.currentSentence as? LearningWordsMainScreenState.CurrentSentence.Present.Todo

        if (currentSentence == null) {
            logger.e { "onHelpClick: Failed to get current sentence: ${contentValue.currentSentence}" }
            return
        }

        val sentence = currentSentence.sentence

        val part = if (partIndex != null) {
            sentence.parts.getOrNull(partIndex) as? SentencePart.Input
        } else {
            sentence.inputParts.firstOrNull { part ->
                part.currentText.length < part.correctText.length
            }
        }

        if (part == null) {
            logger.d { "onHelpClick: Failed to get part with index $partIndex: $sentence" }
            return
        }

        logger.d { "onHelpClick: part=$part" }

        val currentPartTextLength = part.currentText.length
        val currentPartText = part.correctText.substring(0, min(currentPartTextLength + 1, part.correctText.length))

        val updatedPart = part.copy(
            currentText = currentPartText,
        )

        logger.d { "onHelpClick: updatedPart=$updatedPart" }

        val partIndex = sentence.parts.indexOf(part)
        val updatedSentence = sentence.copy(
            parts = sentence.parts.toMutableList().apply {
                set(partIndex, updatedPart)
            },
        )

        updateAndShowContent {
            copy(
                currentSentence = currentSentence.copy(
                    sentence = updatedSentence,
                    isComplete = updatedSentence.areAllPartsComplete,
                )
            )
        }

        logLearningWordsEvent(eventName = "learning_words_help_click")
    }

    private fun updateLearningStats() {
        logger.d { "updateLearningStats" }

        viewModelScope.launch(defaultDispatcher) {
            learningWordsInteractor
                .getLearningStats()
                .onErrorShowMessage()
                .collect { learningStats ->
                    logger.d { "updateLearningStats: learningStats=$learningStats" }

                    val newState = when {
                        learningStats.isEmpty -> LearningWordsMainScreenState.LearningStatsState.Empty
                        else -> LearningWordsMainScreenState.LearningStatsState.Present(stats = learningStats)
                    }

                    updateAndShowContent {
                        copy(
                            learningStatsState = newState,
                        )
                    }
                }
        }
    }

    fun onBannerViewReady() {
        logger.d { "onBannerViewReady" }

        adsInteractor.showBannerAd(AdPlacement.Banner.LearningWords.Main)
    }

    private companion object {
        private const val LOG_TAG = "LearningWordsMainViewModel"

        private const val SCREEN_NAME = "LearningWordsMainScreen"

        private val tokenizerRegex = Regex("""\w+|[^\w\s]+|\s+""")
    }
}