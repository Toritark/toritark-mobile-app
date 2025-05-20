package com.toritark.app.presentation.language.setup.learning

import co.touchlab.kermit.Logger
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.language.BaseLanguageChooserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.*

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
            language.toLanguageUiModel()
        }
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setLearningLanguage(language)
    }

    private suspend fun Language.toLanguageUiModel(): LanguageUiModel {
        return LanguageUiModel(
            language = this,
            displayName = when (this.isoCode) {
                "en" -> getString(Res.string.title_language_name_english)
                "es" -> getString(Res.string.title_language_name_spanish)
                "de" -> getString(Res.string.title_language_name_german)
                "fr" -> getString(Res.string.title_language_name_french)
                "it" -> getString(Res.string.title_language_name_italian)
                "ru" -> getString(Res.string.title_language_name_russian)
                "uk" -> getString(Res.string.title_language_name_ukrainian)
                "pl" -> getString(Res.string.title_language_name_polish)
                "cs" -> getString(Res.string.title_language_name_czech)
                "sr" -> getString(Res.string.title_language_name_serbian)
                "pt" -> getString(Res.string.title_language_name_portuguese)
                "fi" -> getString(Res.string.title_language_name_finnish)
                "sv" -> getString(Res.string.title_language_name_swedish)
                "et" -> getString(Res.string.title_language_name_estonian)
                "lv" -> getString(Res.string.title_language_name_latvian)
                "lt" -> getString(Res.string.title_language_name_lithuanian)
                "lb" -> getString(Res.string.title_language_name_luxembourgish)
                else -> throw IllegalArgumentException("Unknown language code: ${this.isoCode}")
            }
        )
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"
    }
}