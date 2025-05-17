package com.toritark.stories.data.learning_words.repository

import co.touchlab.kermit.Logger
import com.toritark.stories.data.learning_words.data.model.LearningStats
import com.toritark.stories.data.learning_words.data.model.SentenceToLearn
import com.toritark.stories.data.learning_words.db.dao.LearningSentencesDao
import com.toritark.stories.data.learning_words.db.dao.LearningWordsDao
import com.toritark.stories.data.learning_words.db.dao.WordsSentencesToLearnCrossRefDao
import com.toritark.stories.data.learning_words.db.model.*
import com.toritark.stories.util.core.extension.flow.optionalTypedFlow
import com.toritark.stories.util.core.extension.flow.typedFlow
import com.toritark.stories.util.core.extension.flow.unitFlow
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

internal interface LearningWordsRepository {
    fun addWords(
        languageCode: String,
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit>

    fun getNextSentenceToLearn(languageCode: String): Flow<SentenceToLearnWithWords>

    fun getLearningStats(languageCode: String): Flow<LearningStats>

    suspend fun updateWords(words: Collection<WordToLearnDbModel>)
    suspend fun updateSentence(sentence: SentenceToLearnDbModel)
    suspend fun updateSentences(sentences: Collection<SentenceToLearnDbModel>)

    suspend fun getWordsWithSentences(wordsIds: Collection<Long>): List<WordToLearnWithSentences>
    suspend fun getSentencesWithWords(sentencesIds: Collection<Long>): List<SentenceToLearnWithWords>
}

internal class LearningWordsRepositoryImpl(
    private val learningWordsDao: LearningWordsDao,
    private val learningSentencesDao: LearningSentencesDao,
    private val wordsSentencesToLearnCrossRefDao: WordsSentencesToLearnCrossRefDao,
    private val defaultDispatcher: CoroutineDispatcher,
    private val ioDispatcher: CoroutineDispatcher,
) : LearningWordsRepository {

    private val logger = Logger.withTag(LOG_TAG)

    override fun addWords(
        languageCode: String,
        words: Set<String>,
        sentences: Set<SentenceToLearn>,
    ): Flow<Unit> {
        return unitFlow {
            val filteredSentences = filterSentencesWithWords(sentences = sentences, words = words)

            val words = addWords(languageCode = languageCode, words = words)
            val sentences = addSentences(languageCode = languageCode, sentences = filteredSentences)
            linkWordsAndSentences(words = words, sentences = sentences)

        }.flowOn(defaultDispatcher)
    }

    private suspend fun addWords(
        languageCode: String,
        words: Set<String>,
    ): List<WordToLearnDbModel> {
        val lowerCaseWords = words.map { it.lowercase() }

        val existingWords = withContext(ioDispatcher) {
            learningWordsDao.getExistingWords(languageCode = languageCode, words = lowerCaseWords)
        }

        val existingWordsTexts = existingWords.map { it.text }.toSet()

        val wordsToAdd = lowerCaseWords.filter { word -> word !in existingWordsTexts }
        val wordsToUpdate = existingWords
            .filter { existingWord ->
                existingWord.isLearned && existingWord.text in lowerCaseWords
            }
            .map { existingWord ->
                existingWord.copy(isLearned = false)
            }

        val wordsToAddDbModels = wordsToAdd.map { word ->
            WordToLearnDbModel(
                text = word,
                languageCode = languageCode,
            )
        }

        if (wordsToAddDbModels.isNotEmpty()) {
            withContext(ioDispatcher) {
                learningWordsDao.insertWords(wordsToAddDbModels)
            }
        }

        if (wordsToUpdate.isNotEmpty()) {
            withContext(ioDispatcher) {
                learningWordsDao.updateWords(wordsToUpdate)
            }
        }

        return withContext(ioDispatcher) {
            learningWordsDao.getExistingWords(languageCode = languageCode, words = lowerCaseWords)
        }
    }

    private suspend fun addSentences(
        languageCode: String,
        sentences: Collection<SentenceToLearn>,
    ): List<SentenceToLearnDbModel> {
        val sentencesTexts = sentences.map { it.learningLanguageText }

        val existingSentences = withContext(ioDispatcher) {
            learningSentencesDao.getExistingSentences(
                languageCode = languageCode,
                learningLanguageSentences = sentencesTexts,
            )
        }

        val existingSentencesTexts = existingSentences
            .map { dbModel ->
                SentenceToLearn(
                    learningLanguageText = dbModel.learningLanguageText,
                    nativeLanguageText = dbModel.nativeLanguageText,
                )
            }
            .toSet()

        val sentencesToAdd = sentences.filter { sentence -> sentence !in existingSentencesTexts }
        val sentencesToUpdate = existingSentences
            .filter { existingSentence ->
                existingSentence.isLearned && sentences.any { sentence ->
                    sentence.learningLanguageText == existingSentence.learningLanguageText &&
                            sentence.nativeLanguageText == existingSentence.nativeLanguageText
                }
            }
            .map { existingSentence ->
                existingSentence.copy(isLearned = false)
            }

        val sentencesToAddDbModels = sentencesToAdd.map { sentence ->
            SentenceToLearnDbModel(
                learningLanguageText = sentence.learningLanguageText,
                nativeLanguageText = sentence.nativeLanguageText,
                languageCode = languageCode,
            )
        }

        if (sentencesToAddDbModels.isNotEmpty()) {
            withContext(ioDispatcher) {
                learningSentencesDao.insertSentences(sentencesToAddDbModels)
            }
        }

        if (sentencesToUpdate.isNotEmpty()) {
            withContext(ioDispatcher) {
                learningSentencesDao.updateSentences(sentencesToUpdate)
            }
        }

        return withContext(ioDispatcher) {
            learningSentencesDao.getExistingSentences(
                languageCode = languageCode,
                learningLanguageSentences = sentencesTexts,
            )
        }
    }

    private suspend fun linkWordsAndSentences(
        words: List<WordToLearnDbModel>,
        sentences: List<SentenceToLearnDbModel>,
    ) {
        val crossRefs = mutableListOf<WordSentenceToLearnCrossRef>()

        sentences.forEach { sentence ->
            val sentenceWords = sentenceToWords(sentence.learningLanguageText)

            val wordsInSentence = words.filter { word ->
                sentenceWords.contains(word.text)
            }

            wordsInSentence
                .takeIf { it.isNotEmpty() }
                ?.map { word ->
                    WordSentenceToLearnCrossRef(
                        wordId = word.id,
                        sentenceId = sentence.id,
                    )
                }
                ?.let(crossRefs::addAll)
        }

        if (crossRefs.isNotEmpty()) {
            withContext(ioDispatcher) {
                wordsSentencesToLearnCrossRefDao.insert(crossRefs)
            }
        }
    }

    private fun filterSentencesWithWords(
        sentences: Collection<SentenceToLearn>,
        words: Collection<String>,
    ): Collection<SentenceToLearn> {
        val lowerCaseWords = words.map { it.lowercase() }.toSet()

        return sentences.filter { sentence ->
            val sentenceWords = sentenceToWords(sentence.learningLanguageText)

            sentenceWords.any { word -> word in lowerCaseWords }
        }
    }

    private fun sentenceToWords(sentenceText: String): Set<String> {
        return sentenceText
            .split(sentenceWordsRegex)
            .asSequence()
            .map { word -> word.trim() }
            .filter { word -> word.isNotBlank() }
            .map { word -> word.lowercase() }
            .toSet()
    }

    override fun getNextSentenceToLearn(languageCode: String): Flow<SentenceToLearnWithWords> {
        return optionalTypedFlow {
            learningSentencesDao.getNextSentenceToLearn(languageCode = languageCode)
        }.flowOn(ioDispatcher)
    }

    override fun getLearningStats(languageCode: String): Flow<LearningStats> {
        return typedFlow {
            val wordsToLearnCount = learningWordsDao.getWordsToLearnCount(languageCode = languageCode)
            val totalWordsCount = learningWordsDao.getTotalWordsCount(languageCode = languageCode)

            LearningStats(
                words = LearningStats.Words(
                    learned = totalWordsCount - wordsToLearnCount,
                    toLearn = wordsToLearnCount,
                    total = totalWordsCount,
                )
            )
        }.flowOn(ioDispatcher)
    }

    override suspend fun updateWords(words: Collection<WordToLearnDbModel>) {
        withContext(ioDispatcher) {
            learningWordsDao.updateWords(words)
        }
    }

    override suspend fun updateSentence(sentence: SentenceToLearnDbModel) {
        withContext(ioDispatcher) {
            learningSentencesDao.updateSentence(sentence)
        }
    }

    override suspend fun updateSentences(sentences: Collection<SentenceToLearnDbModel>) {
        withContext(ioDispatcher) {
            learningSentencesDao.updateSentences(sentences)
        }
    }

    override suspend fun getWordsWithSentences(wordsIds: Collection<Long>): List<WordToLearnWithSentences> {
        return withContext(ioDispatcher) {
            learningWordsDao.getWordsWithSentences(wordsIds)
        }
    }

    override suspend fun getSentencesWithWords(sentencesIds: Collection<Long>): List<SentenceToLearnWithWords> {
        return withContext(ioDispatcher) {
            learningSentencesDao.getSentencesWithWords(sentencesIds = sentencesIds)
        }
    }

    private companion object {
        private const val LOG_TAG = "LearningWordsRepository"

        private val sentenceWordsRegex = "\\W+".toRegex()
    }
}