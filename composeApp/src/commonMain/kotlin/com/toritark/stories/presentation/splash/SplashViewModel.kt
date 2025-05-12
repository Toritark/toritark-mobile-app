package com.toritark.stories.presentation.splash

import co.touchlab.kermit.Logger
import com.toritark.stories.data.onboarding.repository.OnboardingRepository
import com.toritark.stories.domain.auth.interactor.AuthInteractor
import com.toritark.stories.domain.auth.model.AuthState
import com.toritark.stories.presentation.core_ui.screen.BaseViewModel
import com.toritark.stories.presentation.main.nav.MainScreenDestination
import com.toritark.stories.presentation.onboarding.nav.OnboardingMainScreenDestination
import com.toritark.stories.presentation.splash.nav.SplashScreenDestination
import kotlinx.coroutines.*

class SplashViewModel(
    private val authInteractor: AuthInteractor,
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

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    init {
        initialize()
    }

    private fun initialize() {
        coroutineScope.launch {
            authInteractor.authState.collect { state ->
                logger.d { "initialize: authState=$state" }

                when (state) {
                    is AuthState.Authenticated -> {
                        navigateAuthenticated()
                    }

                    else -> {}
                }
            }
        }
    }

    private suspend fun navigateAuthenticated() {
        if (onboardingRepository.isOnboardingCompleted()) {
            logger.d { "navigateAuthenticated: navigate to main screen" }

            withContext(mainDispatcher) {
                onNavigateTo(MainScreenDestination) {
                    popUpTo(SplashScreenDestination) { inclusive = true }
                }
            }
        } else {
            logger.d { "navigateAuthenticated: navigate to onboarding" }

            withContext(mainDispatcher) {
                onNavigateTo(OnboardingMainScreenDestination) {
                    popUpTo(SplashScreenDestination) { inclusive = true }
                }
            }
        }
    }


    private companion object {
        private const val LOG_TAG = "SplashViewModel"
    }
}