package com.toritark.app.presentation.splash

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.onboarding.repository.OnboardingRepository
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.domain.auth.interactor.AuthInteractor
import com.toritark.app.domain.auth.model.AuthState
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.auth.nav.AuthScreenDestination
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.main.nav.MainScreenDestination
import com.toritark.app.presentation.onboarding.nav.OnboardingMainScreenDestination
import com.toritark.app.presentation.splash.nav.SplashScreenDestination
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first

class SplashViewModel(
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
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

        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch(defaultDispatcher) {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    private fun initialize() {
        coroutineScope.launch(defaultDispatcher) {
            authInteractor.authState.filterNot { it is AuthState.Unknown }.collect { state ->
                logger.d { "initialize: authState=$state" }

                when (state) {
                    is AuthState.Authenticated -> {
                        handleAuthenticated()
                    }

                    else -> {
                        navigateNotAuthenticated()
                    }
                }
            }
        }
    }

    private suspend fun handleAuthenticated() {
        logger.d { "handleAuthenticated" }

        val profileState = profileInteractor.profileState.filterNot { it is ProfileState.Unknown }.first()

        when (profileState) {
            is ProfileState.Missing -> {
                profileInteractor
                    .updateProfile()
                    .catch { t -> logger.w(t) { "Failed to update profile" } }
                    .collect {
                        logger.d { "Updated profile" }
                    }

                navigateAuthenticated()
            }

            is ProfileState.Present -> {
                profileInteractor.updateProfileInBackground()
                navigateAuthenticated()
            }

            else -> {
                logger.e { "handleAuthenticated: Profile state is $profileState, unexpected" }
                navigateAuthenticated()
            }
        }
    }

    private suspend fun navigateAuthenticated() {
        if (onboardingRepository.isOnboardingCompleted()) {
            logger.d { "navigateAuthenticated: navigate to main screen" }

            withContext(mainDispatcher) {
                onNavigateTo(MainScreenDestination.Story) {
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

    private suspend fun navigateNotAuthenticated() {
        if (onboardingRepository.isOnboardingCompleted()) {
            logger.d { "navigateNotAuthenticated: navigate to auth screen" }

            withContext(mainDispatcher) {
                onNavigateTo(AuthScreenDestination.SignIn) {
                    popUpTo(SplashScreenDestination) { inclusive = true }
                }
            }
        } else {
            logger.d { "navigateNotAuthenticated: navigate to onboarding" }

            withContext(mainDispatcher) {
                onNavigateTo(OnboardingMainScreenDestination) {
                    popUpTo(SplashScreenDestination) { inclusive = true }
                }
            }
        }
    }


    private companion object {
        private const val LOG_TAG = "SplashViewModel"

        private const val SCREEN_NAME = "SplashScreen"
    }
}
