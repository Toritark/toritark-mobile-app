package com.toritark.app.presentation.language.setup.base.language

import androidx.lifecycle.viewModelScope
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.setup.base.BaseLanguageSetupViewModel
import com.toritark.app.presentation.language.setup.base.language.model.LanguageChooserScreenContent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal abstract class BaseLanguageChooserViewModel(
    protected val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel<LanguageChooserScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = LanguageChooserScreenContent(),
) {
    protected var selectedLanguage: Language? = null

    override fun initialize() {
        setLoadingScreenState()

        viewModelScope.launch {
            languagesRepository.getLanguages().collect {
                logger.d { "initialize: languages=${it.size}" }

                updateAndShowContent {
                    copy(languages = it)
                }

                setContentScreenState()
            }
        }
    }

    fun onLanguageSelected(language: Language) {
        logger.d { "onLanguageSelected: language=$language" }

        selectedLanguage = language

        updateAndShowContent {
            copy(isNextButtonEnabled = true)
        }
    }

    override fun onNextButtonClick() {
        logger.d { "onNextButtonClick" }

        setLoadingScreenState()

        val selectedLanguage = selectedLanguage ?: return

        viewModelScope.launch {
            saveLanguage(selectedLanguage)

            onPopBackStack()
        }
    }

    protected abstract suspend fun saveLanguage(language: Language)
}