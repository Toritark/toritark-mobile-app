package com.toritark.app.presentation.language.setup.learning

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.language.BaseLanguageChooserViewModel
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

    override suspend fun getLanguages(): List<LanguageUiModel> {
        return languagesRepository.getLearningLanguages().map { language ->
            LanguageUiModel.fromLanguage(language)
        }
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setLearningLanguage(language)
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"
    }
}