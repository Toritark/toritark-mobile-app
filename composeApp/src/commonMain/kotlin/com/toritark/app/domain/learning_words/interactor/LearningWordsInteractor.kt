package com.toritark.app.domain.learning_words.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.learning_words.data.model.LearningStats
import com.toritark.app.data.learning_words.data.model.SentenceToLearn
import com.toritark.app.data.learning_words.db.model.SentenceToLearnWithWords
import com.toritark.app.data.learning_words.db.model.WordToLearnDbModel
import com.toritark.app.data.learning_words.repository.LearningWordsRepository
import com.toritark.app.util.core.extension.flow.errorFlow
import com.toritark.app.util.core.extension.flow.unitFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock

interface LearningWordsInteractor {
    fun addWords(words: Set<String>, sentences: Set<SentenceToLearn>): Flow<Unit>

    fun getNextSentenceToLearn(): Flow<SentenceToLearnWithWords>
    fun getLearningStats(): Flow<LearningStats>

    fun updateSentenceResults(
        sentence: SentenceToLearnWithWords,
        correctWords: Set<WordToLearnDbModel>,
        incorrectWords: Set<WordToLearnDbModel>,
    ): Flow<Unit>

    fun setSentenceLearned(sentence: SentenceToLearnWithWords): Flow<Unit>
}

internal class LearningWordsInteractorImpl(
    private val languagesRepository: LanguagesRepository,
    private val learningWordsRepository: LearningWordsRepository,
) : LearningWordsInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    override fun addWords(
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit> {
        logger.d { "addWords: words=$words, sentences=${sentences.size}" }

        val languageCode = languagesRepository.learningLanguage.value?.isoCode
        if (languageCode == null) {
            logger.w { "addWords: languageCode is null" }
            return errorFlow(IllegalStateException("Language code is null"))
        }

        return learningWordsRepository.addWords(
            languageCode = languageCode,
            words = words,
            sentences = sentences,
        )
    }

    override fun getNextSentenceToLearn(): Flow<SentenceToLearnWithWords> {
        logger.d { "getNextSentenceToLearn" }

        val languageCode = languagesRepository.learningLanguage.value?.isoCode
        if (languageCode == null) {
            logger.w { "getNextSentenceToLearn: languageCode is null" }
            return errorFlow(IllegalStateException("Language code is null"))
        }

        return learningWordsRepository.getNextSentenceToLearn(languageCode = languageCode)
    }

    override fun getLearningStats(): Flow<LearningStats> {
        logger.d { "getLearningStats" }

        val languageCode = languagesRepository.learningLanguage.value?.isoCode
        if (languageCode == null) {
            logger.w { "getLearningStats: languageCode is null" }
            return errorFlow(IllegalStateException("Language code is null"))
        }

        return learningWordsRepository.getLearningStats(languageCode = languageCode)
    }

    override fun updateSentenceResults(
        sentence: SentenceToLearnWithWords,
        correctWords: Set<WordToLearnDbModel>,
        incorrectWords: Set<WordToLearnDbModel>,
    ): Flow<Unit> {
        return unitFlow {
            // Saving words that are in both correct and incorrect categories at the same time separately
            val correctAndIncorrectAtTheSameTimeWords = correctWords.intersect(incorrectWords)
            val correctWords = correctWords - correctAndIncorrectAtTheSameTimeWords
            val incorrectWords = incorrectWords - correctAndIncorrectAtTheSameTimeWords

            val updatedCorrectWords = correctWords.map { word ->
                word.copy(
                    correctAttempts = word.correctAttempts + 1,
                    lastAttempt = Clock.System.now(),
                )
            }

            val updatedIncorrectWords = incorrectWords.map { word ->
                word.copy(
                    incorrectAttempts = word.incorrectAttempts + 1,
                    lastAttempt = Clock.System.now(),
                )
            }

            val updatedCorrectAndIncorrectAtTheSameTimeWords = correctAndIncorrectAtTheSameTimeWords.map { word ->
                word.copy(
                    correctAttempts = word.correctAttempts + 1,
                    incorrectAttempts = word.incorrectAttempts + 1,
                    lastAttempt = Clock.System.now(),
                )
            }

            val allWords = updatedCorrectWords + updatedIncorrectWords + updatedCorrectAndIncorrectAtTheSameTimeWords

            val updatedSentence = if (incorrectWords.isEmpty() && correctAndIncorrectAtTheSameTimeWords.isEmpty()) {
                sentence.sentence.copy(
                    correctAttempts = sentence.sentence.correctAttempts + 1,
                    lastAttempt = Clock.System.now(),
                )
            } else {
                sentence.sentence.copy(
                    incorrectAttempts = sentence.sentence.incorrectAttempts + 1,
                    lastAttempt = Clock.System.now(),
                )
            }

            learningWordsRepository.updateWords(allWords)
            learningWordsRepository.updateSentence(updatedSentence)
        }
    }

    override fun setSentenceLearned(sentence: SentenceToLearnWithWords): Flow<Unit> {
        return unitFlow {
            val updatedWords = sentence.words.map { word ->
                word.copy(isLearned = true)
            }

            val updatedSentence = sentence.sentence.copy(isLearned = true)

            learningWordsRepository.updateWords(updatedWords)
            learningWordsRepository.updateSentence(updatedSentence)

            // Not really optimal to do two queries, but leaving as is for now
            val wordsIds = sentence.words.map { word -> word.id }.toSet()
            val wordsWithSentences = learningWordsRepository.getWordsWithSentences(wordsIds)
            val sentencesIds = wordsWithSentences
                .map { wordWithSentence ->
                    wordWithSentence.sentences.map { sentenceToLearn -> sentenceToLearn.id }
                }
                .flatten()
            val sentencesToCheck = learningWordsRepository.getSentencesWithWords(sentencesIds)

            val sentencesToUpdate = sentencesToCheck.mapNotNull { sentenceToCheck ->
                val sentenceWordsIds = sentenceToCheck.words.map { word -> word.id }.toSet()
                if (sentenceWordsIds.containsAll(wordsIds) && !sentenceToCheck.sentence.isLearned) {
                    sentenceToCheck.sentence.copy(isLearned = true)
                } else {
                    null
                }
            }

            if (sentencesToUpdate.isNotEmpty()) {
                learningWordsRepository.updateSentences(sentencesToUpdate)
            }
        }
    }

    private companion object {
        private const val LOG_TAG = "LearningWordsInteractor"
    }
}