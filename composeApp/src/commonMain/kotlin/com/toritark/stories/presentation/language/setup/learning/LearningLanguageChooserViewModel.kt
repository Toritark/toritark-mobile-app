package com.toritark.stories.presentation.language.setup.learning

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LearningLanguageChooserViewModel(
    private val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _languages = MutableStateFlow<List<Language>>(emptyList())
    val languages = _languages.asStateFlow()

    private val _isNextButtonEnabled = MutableStateFlow(false)
    val isNextButtonEnabled = _isNextButtonEnabled.asStateFlow()

    var selectedLanguage: Language? = null

    init {
        initialize()
    }

    private fun initialize() {
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

    fun onSaveLanguageClick() {
        logger.d { "onSaveLanguageClick" }

        setLoadingScreenState()

        val selectedLanguage = selectedLanguage ?: return

        viewModelScope.launch {
            languagesRepository.setLearningLanguage(selectedLanguage)

            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "LearningLanguageChooserViewModel"
    }
}