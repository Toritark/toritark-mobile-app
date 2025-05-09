package com.toritark.stories.presentation.language.setup.learning

import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.language.setup.base.language.BaseLanguageChooserViewModel
import kotlinx.coroutines.CoroutineDispatcher

internal class LearningLanguageChooserViewModel(
    languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageChooserViewModel(
    languagesRepository = languagesRepository,
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    init {
        initialize()
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setLearningLanguage(language)
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"
    }
}