package com.toritark.app.presentation.language.setup.native

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.domain.core.language.GetDeviceLanguageCode
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.language.BaseLanguageChooserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class NativeLanguageChooserViewModel(
    private val getDeviceLanguageCode: GetDeviceLanguageCode,
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

    override fun initialize() {
        super.initialize()

        logScreenView()

        initializeNativeLanguage()
    }

    private fun logScreenView() {
        viewModelScope.launch {
            Analytics.logScreenView(SCREEN_NAME)

            val isInitialSetup = languagesRepository.nativeLanguage.value == null
            val event = if (isInitialSetup) {
                AnalyticsEvent("show_choose_native_lang_initial")
            } else {
                AnalyticsEvent("show_choose_native_lang")
            }

            Analytics.logEvent(event)
        }
    }

    private fun initializeNativeLanguage() {
        viewModelScope.launch {
            val deviceLanguageCode = getDeviceLanguageCode().takeIf { it.isNotBlank() } ?: return@launch

            val allLanguages = languagesRepository.getAllLanguages()
            selectedLanguage = allLanguages.find { it.isoCode == deviceLanguageCode }?.toLanguageUiModel()

            updateAndShowContent {
                copy(
                    preSelectedLanguage = selectedLanguage,
                    isNextButtonEnabled = true
                )
            }
        }
    }

    override suspend fun getLanguages(): List<LanguageUiModel> {
        return languagesRepository.getAllLanguages().map { language ->
            language.toLanguageUiModel()
        }
    }

    private fun Language.toLanguageUiModel(): LanguageUiModel {
        return LanguageUiModel(
            language = this,
            displayName = this.name,
        )
    }

    override fun onLanguageSelected(language: LanguageUiModel) {
        super.onLanguageSelected(language)

        Analytics.logEvent(
            AnalyticsEvent(
                name = "select_native_lang",
                parameters = mapOf(
                    "language" to language.language.isoCode,
                )
            )
        )
    }

    override fun onNextButtonClick() {
        Analytics.logEvent(
            AnalyticsEvent(
                name = "choose_native_lang_next_click",
                parameters = mapOf(
                    "language" to selectedLanguage?.language?.isoCode,
                )
            )
        )

        super.onNextButtonClick()
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setNativeLanguage(language)
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"

        private const val SCREEN_NAME = "NativeLanguageChooserScreen"
    }
}