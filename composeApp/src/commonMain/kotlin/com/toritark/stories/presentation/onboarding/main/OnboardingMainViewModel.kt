package com.toritark.stories.presentation.onboarding.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.onboarding.repository.OnboardingRepository
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.language.nav.LanguageSetupScreenDestination
import com.toritark.stories.presentation.main.nav.MainScreenDestination
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
) : BaseViewModel<Unit>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = Unit,
) {
    override val logger = Logger.withTag(LOG_TAG)

    fun update() {
        logger.d { "update" }

        viewModelScope.launch {
            if (onboardingRepository.isOnboardingCompleted()) {
                openMainScreen()
            } else {
                if (isLanguageSetupCompleted()) {
                    onboardingRepository.setOnboardingCompleted()
                    openMainScreen()
                } else {
                    showLanguageSetupStep()
                }
            }
        }
    }

    private suspend fun isLanguageSetupCompleted(): Boolean {
        return languagesRepository
            .areAllParametersSet
            .filterNotNull()
            .first()
    }

    private fun showLanguageSetupStep() {
        when {
            languagesRepository.learningLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: learningLanguage is null" }

                onNavigateTo(LanguageSetupScreenDestination.LearningLanguageChooser) {}
            }

            languagesRepository.languageLevel.value == null -> {
                logger.d { "showLanguageSetupStep: languageLevel is null" }

                onNavigateTo(LanguageSetupScreenDestination.LanguageLevelChooser) {}
            }

            languagesRepository.nativeLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: nativeLanguage is null" }

                onNavigateTo(LanguageSetupScreenDestination.NativeLanguageChooser) {}
            }
        }
    }

    private fun openMainScreen() {
        logger.d { "openMainScreen" }

        onNavigateTo(MainScreenDestination) {}
    }

    private companion object {
        private const val LOG_TAG = "OnboardingViewModel"
    }
}