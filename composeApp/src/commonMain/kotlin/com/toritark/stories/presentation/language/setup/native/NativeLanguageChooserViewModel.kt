package com.toritark.stories.presentation.language.setup.native

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.domain.core.language.GetDeviceLanguageCode
import com.toritark.stories.presentation.language.setup.base.language.BaseLanguageChooserViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.mapNotNull
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

        initializeNativeLanguage()
    }

    private fun initializeNativeLanguage() {
        viewModelScope.launch {
            val deviceLanguageCode = getDeviceLanguageCode().takeIf { it.isNotBlank() } ?: return@launch

            languagesRepository
                .getLanguages()
                .mapNotNull { languages ->
                    languages.find { it.isoCode == deviceLanguageCode }
                }
                .collect { language ->
                    logger.d { "initializeNativeLanguage: language=$language from device language '$deviceLanguageCode'" }

                    selectedLanguage = language

                    updateAndShowContent {
                        copy(
                            preSelectedLanguage = language,
                            isNextButtonEnabled = true
                        )
                    }
                }

        }
    }

    override suspend fun saveLanguage(language: Language) {
        languagesRepository.setNativeLanguage(language)
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"
    }
}