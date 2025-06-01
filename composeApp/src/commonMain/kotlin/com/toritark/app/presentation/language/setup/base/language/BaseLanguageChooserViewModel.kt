package com.toritark.app.presentation.language.setup.base.language

import androidx.lifecycle.viewModelScope
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.model.LanguageUiModel
import com.toritark.app.presentation.language.setup.base.BaseLanguageSetupViewModel
import com.toritark.app.presentation.language.setup.base.language.model.LanguageChooserScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal abstract class BaseLanguageChooserViewModel(
    protected val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel<LanguageChooserScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = LanguageChooserScreenState(),
) {
    protected var selectedLanguage: LanguageUiModel? = null

    override fun initialize() {
        logger.d { "initialize" }

        viewModelScope.launch(defaultDispatcher) {
            val languages = getLanguages()

            updateAndShowContent {
                copy(
                    languages = languages,
                )
            }
        }
    }

    protected abstract suspend fun getLanguages(): List<LanguageUiModel>

    open fun onLanguageSelected(language: LanguageUiModel) {
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

        viewModelScope.launch(defaultDispatcher) {
            saveLanguage(selectedLanguage.language)

            onPopBackStack()
        }
    }

    protected abstract suspend fun saveLanguage(language: Language)
}