package com.toritark.app.presentation.learning_words.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.learning_words.db.model.SentenceToLearnWithWords
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.learning_words.interactor.LearningWordsInteractor
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.learning_words.main.model.LearningWordsMainScreenState
import com.toritark.app.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.app.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.app.presentation.main.nav.MainScreenDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.onEmpty
import kotlinx.coroutines.launch
import kotlin.math.min

internal class LearningWordsMainViewModel(
    private val learningWordsInteractor: LearningWordsInteractor,
    private val adsInteractor: AdsInteractor,
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

    private var currentSentenceDbModel: SentenceToLearnWithWords? = null

    init {
        getNextSentence()
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch {
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

        this.currentSentenceDbModel = null

        viewModelScope.launch {
            learningWordsInteractor
                .getNextSentenceToLearn()
                .onErrorShowMessage()
                .onEmpty {
                    showEmptyState()
                    updateLearningStats()
                }
                .collect { sentence ->
                    showSentenceToLearn(sentenceDbModel = sentence)
                    updateLearningStats()
                }
        }
    }

    private fun showEmptyState() {
        this.currentSentenceDbModel = null

        updateAndShowContent {
            copy(currentSentence = LearningWordsMainScreenState.CurrentSentence.Empty)
        }
    }

    private fun showSentenceToLearn(sentenceDbModel: SentenceToLearnWithWords) {
        logger.d { "showSentenceToLearn: sentenceDbModel=$sentenceDbModel" }

        val sentenceWithWords = getSentenceWithWords(sentenceDbModel = sentenceDbModel)
        this.currentSentenceDbModel = sentenceDbModel

        updateAndShowContent {
            copy(currentSentence = LearningWordsMainScreenState.CurrentSentence.Present.Todo(sentenceWithWords))
        }

        adsInteractor.showInterstitialAd(AdPlacement.Interstitial.LearningWords.Main)
    }

    private fun getSentenceWithWords(sentenceDbModel: SentenceToLearnWithWords): SentenceWithParts {
        val text = sentenceDbModel.sentence.learningLanguageText
        val words = sentenceDbModel
            .words
            .filterNot { it.isLearned }
            .map { word -> word.text }
            .filter { it.isNotBlank() }
            .toSet()

        if (words.isEmpty()) {
            logger.e { "getSentenceWithWords: words is empty" }
            return SentenceWithParts(
                id = sentenceDbModel.sentence.id,
                parts = listOf(SentencePart.Text(text)),
                nativeLanguageText = sentenceDbModel.sentence.nativeLanguageText,
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
            id = sentenceDbModel.sentence.id,
            parts = parts,
            nativeLanguageText = sentenceDbModel.sentence.nativeLanguageText,
        )
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
    }

    fun onNextClick() {
        logger.d { "onNextClick" }

        val currentSentence =
            contentValue.currentSentence as? LearningWordsMainScreenState.CurrentSentence.Present.Todo

        if (currentSentence == null) {
            logger.e { "onNextClick: Failed to get current sentence: ${contentValue.currentSentence}" }
            return
        }

        val currentSentenceDbModel = this.currentSentenceDbModel
        if (currentSentenceDbModel == null) {
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

        viewModelScope.launch {
            val sentence = currentSentence.sentence
            val showLearnedDialog = sentence.incorrectPartsCount == 0

            val correctWordsTexts = sentence
                .inputParts
                .filter { it.state == SentencePart.Input.State.CORRECT }
                .map { it.correctText.lowercase() }
                .toSet()

            val incorrectWordsTexts = sentence
                .inputParts
                .filter { it.state == SentencePart.Input.State.INCORRECT }
                .map { it.correctText.lowercase() }
                .toSet()

            val correctWords = currentSentenceDbModel
                .words
                .filter { word -> correctWordsTexts.contains(word.text) }
                .toSet()

            val incorrectWords = currentSentenceDbModel
                .words
                .filter { word -> incorrectWordsTexts.contains(word.text) }
                .toSet()

            learningWordsInteractor
                .updateSentenceResults(
                    sentence = currentSentenceDbModel,
                    correctWords = correctWords,
                    incorrectWords = incorrectWords,
                )
                .onErrorShowMessage()
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
        }
    }

    fun onLearnedClick() {
        logger.d { "onLearnedClick" }

        updateAndShowContent {
            copy(showLearnedDialog = false)
        }

        val currentSentenceDbModel = this.currentSentenceDbModel
        if (currentSentenceDbModel == null) {
            logger.e { "onLearnedClick: Current sentence db model is null!" }
            return
        }

        viewModelScope.launch {
            learningWordsInteractor
                .setSentenceLearned(sentence = currentSentenceDbModel)
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
    }

    private fun updateLearningStats() {
        logger.d { "updateLearningStats" }

        viewModelScope.launch {
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