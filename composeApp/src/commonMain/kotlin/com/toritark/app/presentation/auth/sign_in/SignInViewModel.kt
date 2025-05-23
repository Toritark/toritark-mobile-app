@file:OptIn(ExperimentalCoroutinesApi::class)

package com.toritark.app.presentation.auth.sign_in

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.domain.auth.interactor.AuthInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.auth.sign_in.exception.SignInException
import com.toritark.app.presentation.auth.sign_in.exception.UserCancelledSignInException
import com.toritark.app.presentation.auth.sign_in.model.SignInScreenState
import com.toritark.app.presentation.auth.sign_in.model.provider.AuthProviderUiModel
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import com.toritark.app.presentation.main.nav.MainScreenDestination
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_auth_sign_in_failed

internal class SignInViewModel(
    private val authInteractor: AuthInteractor,
    private val profileInteractor: ProfileInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<SignInScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = SignInScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    private val _signInRequestEvents = MutableSharedFlow<AuthProvider>()
    val signInRequestEvents = _signInRequestEvents.asSharedFlow()

    init {
        initialize()

        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch {
            Analytics.logScreenView(SCREEN_NAME)
        }
    }

    fun onSignInClick(authProviderUiModel: AuthProviderUiModel) {
        logger.d { "onSignInClick: authProviderUiModel=$authProviderUiModel" }

        viewModelScope.launch {
            _signInRequestEvents.emit(authProviderUiModel.provider)
        }
    }

    private fun initialize() {
        logger.d { "initialize" }

        loadAuthProviders()
    }

    private fun loadAuthProviders() {
        logger.d { "loadAuthProviders" }

        viewModelScope.launch {
            val authProviders = authInteractor
                .getAuthProviders()
                .map { authProvider -> authProvider.toUiModel() }

            updateAndShowContent {
                copy(
                    authProviders = authProviders,
                )
            }
        }
    }

    private fun AuthProvider.toUiModel(): AuthProviderUiModel {
        return when (this) {
            AuthProvider.GOOGLE -> AuthProviderUiModel.google
            AuthProvider.APPLE -> AuthProviderUiModel.apple
            AuthProvider.FACEBOOK -> AuthProviderUiModel.facebook
        }
    }

    fun setIsSigningIn(isSigningIn: Boolean) {
        logger.d { "setIsSigningIn: isSigningIn=$isSigningIn" }

        updateAndShowContent {
            copy(
                authProgressState = if (isSigningIn) {
                    SignInScreenState.AuthProgressState.InProgress
                } else {
                    SignInScreenState.AuthProgressState.Idle
                }
            )
        }
    }

    fun handleSignInToken(token: String) {
        logger.d { "handleSignInToken: token=$token" }

        viewModelScope.launch {
            authInteractor
                .authenticateWithFirebase(token = token)
                .catch { e ->
                    setIsSigningIn(false)

                    throw e
                }
                .onErrorShowMessage()
                .flatMapConcat {
                    profileInteractor.updateProfile()
                }
                .catch { t ->
                    logger.w(t) { "Failed to update profile" }
                }
                .collect {
                    logger.d { "handleSignInToken: authenticated" }

                    openNextScreen()
                }
        }
    }

    fun handleSignInException(exception: SignInException) {
        logger.w(exception) { "handleSignInException: exception=$exception" }

        setIsSigningIn(false)

        if (exception is UserCancelledSignInException) {
            logger.d { "handleSignInException: Cancelled by user, not an error" }
            return
        }

        viewModelScope.launch {
            val errorMessage = getString(Res.string.title_auth_sign_in_failed, exception.message.orEmpty())
            showErrorMessage(errorMessage)
        }
    }

    private fun openNextScreen() {
        onNavigateTo(MainScreenDestination.Story) {}
    }

    private companion object {
        private const val LOG_TAG = "SignInViewModel"

        private const val SCREEN_NAME = "SignInScreen"
    }
}