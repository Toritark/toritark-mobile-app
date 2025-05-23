package com.toritark.app.presentation.language.setup.level

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.language.model.LanguageLevel
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.presentation.language.setup.base.BaseLanguageSetupViewModel
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelChooserScreenState
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch

internal class LanguageLevelChooserViewModel(
    private val languagesRepository: LanguagesRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseLanguageSetupViewModel<LanguageLevelChooserScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = LanguageLevelChooserScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private var languageLevel: LanguageLevel? = null

    init {
        initialize()

        logScreenView()
    }

    override fun initialize() {
        updateAndShowContent {
            copy(levels = LanguageLevelUiModel.allLevels)
        }
    }

    private fun logScreenView() {
        viewModelScope.launch {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    fun onLanguageLevelSelected(languageLevel: LanguageLevelUiModel) {
        logger.d { "onLanguageLevelSelected: level=${languageLevel.languageLevel}" }

        this.languageLevel = languageLevel.languageLevel

        updateAndShowContent {
            copy(
                selectedLevel = languageLevel,
                isNextButtonEnabled = true,
            )
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

        private const val SCREEN_NAME = "LanguageLevelChooserScreen"
    }
}