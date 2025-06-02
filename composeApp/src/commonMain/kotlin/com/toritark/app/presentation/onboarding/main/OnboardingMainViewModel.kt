package com.toritark.app.presentation.onboarding.main

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.language.repository.LanguagesRepository
import com.toritark.app.data.onboarding.repository.OnboardingRepository
import com.toritark.app.presentation.auth.nav.AuthScreenDestination
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.language.nav.LanguageSetupScreenDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    init {
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    fun update() {
        logger.d { "update" }

        viewModelScope.launch(defaultDispatcher) {
            if (onboardingRepository.isOnboardingCompleted()) {
                openNextScreen()
            } else {
                if (isLanguageSetupCompleted()) {
                    onboardingRepository.setOnboardingCompleted()
                    openNextScreen()
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

    private suspend fun showLanguageSetupStep() {
        when {
            languagesRepository.learningLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: learningLanguage is null" }

                withContext(mainDispatcher) {
                    onNavigateTo(LanguageSetupScreenDestination.LearningLanguageChooser) {}
                }
            }

            languagesRepository.languageLevel.value == null -> {
                logger.d { "showLanguageSetupStep: languageLevel is null" }

                withContext(mainDispatcher) {
                    onNavigateTo(LanguageSetupScreenDestination.LanguageLevelChooser) {}
                }
            }

            languagesRepository.nativeLanguage.value == null -> {
                logger.d { "showLanguageSetupStep: nativeLanguage is null" }

                withContext(mainDispatcher) {
                    onNavigateTo(LanguageSetupScreenDestination.NativeLanguageChooser) {}
                }
            }
        }
    }

    private suspend fun openNextScreen() {
        logger.d { "openNextScreen" }

        withContext(mainDispatcher) {
            onNavigateTo(AuthScreenDestination.SignIn) {}
        }
    }

    private companion object {
        private const val LOG_TAG = "OnboardingViewModel"

        private const val SCREEN_NAME = "OnboardingScreen"
    }
}