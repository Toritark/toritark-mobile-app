@file:OptIn(ExperimentalCoroutinesApi::class)

package com.toritark.app.domain.learning_words.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.learning_words.api.model.LearningStatsApiModel
import com.toritark.app.data.learning_words.api.model.SentenceToLearnApiModel
import com.toritark.app.data.learning_words.api.repository.LearningWordsApiRepository
import com.toritark.app.data.learning_words.model.SentenceToLearn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat

interface LearningWordsInteractor {
    fun addWords(words: Set<String>, sentences: Set<SentenceToLearn>): Flow<Unit>

    fun getNextSentenceToLearn(): Flow<SentenceToLearnApiModel>
    fun getLearningStats(): Flow<LearningStatsApiModel>

    fun updateSentenceResults(
        sentenceId: Long,
        correctWordsIds: Set<Long>,
        incorrectWordsIds: Set<Long>,
    ): Flow<Unit>

    fun setSentenceLearned(sentenceId: Long): Flow<Unit>
}

internal class LearningWordsInteractorImpl(
    private val languagesRepository: LanguagesRepository,
    private val learningWordsApiRepository: LearningWordsApiRepository,
) : LearningWordsInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override fun addWords(
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit> {
        logger.d { "addWords: words=$words, sentences=${sentences.size}" }

        return languagesRepository
            .getLanguagesWithLevel()
            .flatMapConcat { languagesWithLevel ->
                learningWordsApiRepository.addWords(
                    learningLanguageCode = languagesWithLevel.learningLanguage.isoCode,
                    nativeLanguageCode = languagesWithLevel.nativeLanguage.isoCode,
                    words = words,
                    sentences = sentences,
                )
            }
    }

    override fun getNextSentenceToLearn(): Flow<SentenceToLearnApiModel> {
        logger.d { "getNextSentenceToLearn" }

        return languagesRepository
            .getLanguagesWithLevel()
            .flatMapConcat { languagesWithLevel ->
                learningWordsApiRepository.getNextSentenceToLearn(
                    learningLanguageCode = languagesWithLevel.learningLanguage.isoCode,
                    nativeLanguageCode = languagesWithLevel.nativeLanguage.isoCode,
                )
            }

    }

    override fun getLearningStats(): Flow<LearningStatsApiModel> {
        logger.d { "getLearningStats" }

        return languagesRepository
            .getLanguagesWithLevel()
            .flatMapConcat { languagesWithLevel ->
                learningWordsApiRepository.getLearningStats(
                    learningLanguageCode = languagesWithLevel.learningLanguage.isoCode,
                    nativeLanguageCode = languagesWithLevel.nativeLanguage.isoCode,
                )
            }
    }

    override fun updateSentenceResults(
        sentenceId: Long,
        correctWordsIds: Set<Long>,
        incorrectWordsIds: Set<Long>,
    ): Flow<Unit> {
        return learningWordsApiRepository.updateSentenceResults(
            sentenceId = sentenceId,
            correctWordsIds = correctWordsIds,
            incorrectWordsIds = incorrectWordsIds,
        )
    }

    override fun setSentenceLearned(sentenceId: Long): Flow<Unit> {
        return learningWordsApiRepository.setSentenceLearned(sentenceId = sentenceId)
    }

    private companion object {
        private const val LOG_TAG = "LearningWordsInteractor"
    }
}