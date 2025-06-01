package com.toritark.app.presentation.language.setup.learning

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.language.BaseLanguageChooserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

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

        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)

            val isInitialSetup = languagesRepository.learningLanguage.value == null
            val event = if (isInitialSetup) {
                AnalyticsEvent("show_choose_learning_lang_initial")
            } else {
                AnalyticsEvent("show_choose_learning_lang")
            }

            Analytics.logEvent(event)
        }
    }

    override suspend fun getLanguages(): List<LanguageUiModel> {
        return languagesRepository.getLearningLanguages().map { language ->
            LanguageUiModel.fromLanguage(language)
        }
    }

    override fun onLanguageSelected(language: LanguageUiModel) {
        super.onLanguageSelected(language)

        Analytics.logEvent(
            AnalyticsEvent(
                name = "select_learning_lang",
                parameters = mapOf(
                    "language" to language.language.isoCode,
                )
            )
        )
    }

    override fun onNextButtonClick() {
        Analytics.logEvent(
            AnalyticsEvent(
                name = "choose_learning_lang_next_click",
                parameters = mapOf(
                    "language" to selectedLanguage?.language?.isoCode,
                )
            )
        )

        super.onNextButtonClick()
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setLearningLanguage(language)
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"

        private const val SCREEN_NAME = "LearningLanguageChooserScreen"
    }
}