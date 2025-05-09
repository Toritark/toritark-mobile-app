package com.toritark.stories.presentation.onboarding.main

import co.touchlab.kermit.Logger
import com.toritark.stories.data.language.repository.LanguagesRepository
import com.toritark.stories.data.onboarding.repository.OnboardingRepository
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.language.nav.LearningLanguageChooserScreenDestination
import kotlinx.coroutines.CoroutineDispatcher

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

        if (languagesRepository.areAllParametersSet.value == true) {
            // TODO: Main screen
            // TODO: Set onboarding completed
        } else {
            showLanguageSetupStep()
        }
    }

    private fun showLanguageSetupStep() {
        when {
            languagesRepository.learningLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: learningLanguage is null" }

                onNavigate(LearningLanguageChooserScreenDestination) {}
            }

            languagesRepository.languageLevel.value == null -> {
                logger.d { "showLanguageSetupStep: languageLevel is null" }

                // TODO
            }

            languagesRepository.nativeLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: nativeLanguage is null" }

                // TODO
            }
        }
    }

    private companion object {
        private const val LOG_TAG = "OnboardingViewModel"
    }
}