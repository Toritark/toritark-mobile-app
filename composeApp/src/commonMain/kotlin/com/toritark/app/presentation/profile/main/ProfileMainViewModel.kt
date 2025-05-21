package com.toritark.app.presentation.profile.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.language.nav.LanguageSetupScreenDestination
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel
import com.toritark.app.presentation.profile.main.model.ProfileMainScreenState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ProfileMainViewModel(
    private val profileInteractor: ProfileInteractor,
    private val billingInteractor: BillingInteractor,
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

    private val profileState: StateFlow<ProfileMainScreenState.ProfileUiState> = profileInteractor
        .profileState
        .map { profileState: ProfileState ->
            when (profileState) {
                ProfileState.Unknown, ProfileState.Missing -> ProfileMainScreenState.ProfileUiState.Loading
                is ProfileState.Present -> ProfileMainScreenState.ProfileUiState.Present(
                    profile = profileState.profile,
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileMainScreenState.ProfileUiState.Loading,
        )

    init {
        listenToStates()
    }

    fun loadProfile() {
        logger.d { "loadProfile" }
    }

    private fun listenToStates() {
        logger.d { "listenToStates" }

        viewModelScope.launch {
            combine(
                languageSettingsState,
                profileState
            ) { languageSettingsState, profileState ->
                contentValue.copy(
                    profileState = profileState,
                    languageSettingsState = languageSettingsState,
                )
            }.collect { newState ->
                updateAndShowContent {
                    newState
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

    fun onProfileImageTripleClick() {
        logger.d { "onProfileImageTripleClick" }

        viewModelScope.launch {
            billingInteractor.triggerPlanCheck()
        }
    }

    private companion object {
        private const val LOG_TAG = "ProfileMainViewModel"
    }
}