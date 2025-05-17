package com.toritark.stories.presentation.language.setup.level

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.model.LanguageLevel
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.language.setup.base.BaseLanguageSetupViewModel
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelChooserScreenContent
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class LanguageLevelChooserViewModel(
    private val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel<LanguageLevelChooserScreenContent>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = LanguageLevelChooserScreenContent(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private var languageLevel: LanguageLevel? = null

    init {
        initialize()
    }

    override fun initialize() {
        updateAndShowContent {
            copy(levels = LanguageLevelUiModel.allLevels)
        }
    }

    fun onLanguageLevelSelected(languageLevel: LanguageLevelUiModel) {
        logger.d { "onLanguageLevelSelected: level=${languageLevel.languageLevel}" }

        this.languageLevel = languageLevel.languageLevel

        updateAndShowContent {
            copy(isNextButtonEnabled = true)
        }
    }

    override fun onNextButtonClick() {
        logger.d { "onNextButtonClick" }

        val languageLevel = languageLevel ?: return

        setLoadingScreenState()

        viewModelScope.launch {
            languagesRepository.setLanguageLevel(languageLevel)
            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "LanguageLevelChooserViewModel"
    }
}