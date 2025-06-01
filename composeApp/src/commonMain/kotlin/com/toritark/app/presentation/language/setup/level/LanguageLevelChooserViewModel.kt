package com.toritark.app.presentation.language.setup.level

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
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
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)

            val isInitialSetup = languagesRepository.languageLevel.value == null
            val event = if (isInitialSetup) {
                AnalyticsEvent("show_choose_lang_lvl_initial")
            } else {
                AnalyticsEvent("show_choose_lang_lvl")
            }

            Analytics.logEvent(event)
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

        Analytics.logEvent(
            AnalyticsEvent(
                name = "select_lang_lvl",
                parameters = mapOf(
                    "level" to languageLevel.languageLevel.value.lowercase(),
                )
            )
        )
    }

    override fun onNextButtonClick() {
        logger.d { "onNextButtonClick" }

        val languageLevel = languageLevel ?: return

        setLoadingScreenState()

        viewModelScope.launch(defaultDispatcher) {
            languagesRepository.setLanguageLevel(languageLevel)

            Analytics.logEvent(
                AnalyticsEvent(
                    name = "choose_lang_lvl_next_click",
                    parameters = mapOf(
                        "level" to languageLevel.value.lowercase(),
                    )
                )
            )

            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "LanguageLevelChooserViewModel"

        private const val SCREEN_NAME = "LanguageLevelChooserScreen"
    }
}