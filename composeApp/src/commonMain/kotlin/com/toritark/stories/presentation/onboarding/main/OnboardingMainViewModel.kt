package com.toritark.stories.presentation.onboarding.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.onboarding.repository.OnboardingRepository
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.language.nav.LanguageSetupScreenDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

internal class OnboardingMainViewModel(
    private val languagesRepository: LanguagesRepository,
    private val onboardingRepository: OnboardingRepository,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun update() {
        logger.d { "update" }

        viewModelScope.launch {
            val areAllParametersSet = languagesRepository
                .areAllParametersSet
                .filterNotNull()
                .first()

            if (areAllParametersSet) {
                // TODO: Main screen
                // TODO: Set onboarding completed
            } else {
                showLanguageSetupStep()
            }
        }
    }

    private fun showLanguageSetupStep() {
        when {
            languagesRepository.learningLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: learningLanguage is null" }

                onNavigate(LanguageSetupScreenDestination.LearningLanguageChooser) {}
            }

            languagesRepository.languageLevel.value == null -> {
                logger.d { "showLanguageSetupStep: languageLevel is null" }

                onNavigate(LanguageSetupScreenDestination.LanguageLevelChooser) {}
            }

            languagesRepository.nativeLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: nativeLanguage is null" }

                onNavigate(LanguageSetupScreenDestination.NativeLanguageChooser) {}
            }
        }
    }

    private companion object {
        private const val LOG_TAG = "OnboardingViewModel"
    }
}