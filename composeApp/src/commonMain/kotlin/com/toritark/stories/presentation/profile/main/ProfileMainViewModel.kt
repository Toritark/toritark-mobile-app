package com.toritark.stories.presentation.profile.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.language.nav.LanguageSetupScreenDestination
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.stories.presentation.profile.main.model.ProfileMainScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class ProfileMainViewModel(
    languagesRepository: LanguagesRepository, // TODO: Use interactor
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<ProfileMainScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = ProfileMainScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val languageSettingsState: StateFlow<ProfileMainScreenState.LanguageSettingsState> = combine(
        languagesRepository.learningLanguage,
        languagesRepository.nativeLanguage,
        languagesRepository.languageLevel,
    ) { learningLanguage, nativeLanguage, languageLevel ->
        if (learningLanguage != null && nativeLanguage != null && languageLevel != null) {
            ProfileMainScreenState.LanguageSettingsState.Present(
                learningLanguage = learningLanguage,
                nativeLanguage = nativeLanguage,
                languageLevel = LanguageLevelUiModel.getForLanguageLevel(languageLevel),
            )
        } else {
            ProfileMainScreenState.LanguageSettingsState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileMainScreenState.LanguageSettingsState.Loading,
    )

    init {
        listenToLanguageSettingsState()
    }

    fun loadProfile() {
        logger.d { "loadProfile" }
    }

    private fun listenToLanguageSettingsState() {
        logger.d { "listenToLanguageSettingsState" }

        viewModelScope.launch {
            languageSettingsState.collect { state ->
                updateAndShowContent {
                    copy(
                        languageSettingsState = state,
                    )
                }
            }
        }
    }

    fun onChooseLearningLanguageClick() {
        logger.d { "onChooseLearningLanguageClick" }

        onNavigateTo(LanguageSetupScreenDestination.LearningLanguageChooser) {}
    }

    fun onChooseLanguageLevelClick() {
        logger.d { "onChooseLanguageLevelClick" }

        onNavigateTo(LanguageSetupScreenDestination.LanguageLevelChooser) {}
    }

    fun onChooseNativeLanguageClick() {
        logger.d { "onChooseNativeLanguageClick" }

        onNavigateTo(LanguageSetupScreenDestination.NativeLanguageChooser) {}
    }

    private companion object {
        private const val LOG_TAG = "ProfileMainViewModel"
    }
}