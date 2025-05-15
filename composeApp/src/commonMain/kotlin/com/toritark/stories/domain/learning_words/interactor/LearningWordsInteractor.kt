package com.toritark.stories.domain.learning_words.interactor

import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.learning_words.data.model.SentenceToLearn
import com.toritark.stories.data.learning_words.repository.LearningWordsRepository
import com.toritark.stories.util.core.extension.flow.errorFlow
import kotlinx.coroutines.flow.Flow

interface LearningWordsInteractor {
    fun addWords(words: Set<String>, sentences: Set<SentenceToLearn>): Flow<Unit>
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

    private companion object {
        private const val LOG_TAG = "LearningWordsInteractor"
    }
}