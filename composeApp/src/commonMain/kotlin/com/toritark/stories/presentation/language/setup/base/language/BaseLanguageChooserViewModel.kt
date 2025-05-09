package com.toritark.stories.presentation.language.setup.base.language

import androidx.lifecycle.viewModelScope
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.language.setup.base.BaseLanguageSetupViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

internal abstract class BaseLanguageChooserViewModel(
    protected val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages = _languages.asStateFlow()

    protected val _preSelectedLanguage = MutableStateFlow<Language?>(null)
    val preSelectedLanguage = _preSelectedLanguage.asStateFlow()

    protected var selectedLanguage: Language? = null

    override fun initialize() {
        setLoadingScreenState()

        viewModelScope.launch {
            languagesRepository.getLanguages().collect {
                logger.d { "initialize: languages=${it.size}" }

                _languages.value = it

                setContentScreenState()
            }
        }
    }

    fun onLanguageSelected(language: Language) {
        logger.d { "onLanguageSelected: language=$language" }

        selectedLanguage = language
        _isNextButtonEnabled.value = true
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