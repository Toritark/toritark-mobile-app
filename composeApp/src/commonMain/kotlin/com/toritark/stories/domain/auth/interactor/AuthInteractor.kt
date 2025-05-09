package com.toritark.stories.domain.auth.interactor

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.stories.data.auth.repository.AuthApiRepository
import com.toritark.stories.data.core_api.jwt.model.AccessTokenState
import com.toritark.stories.data.core_api.jwt.repository.JwtTokensRepository
import com.toritark.stories.domain.auth.model.AuthState
import com.toritark.stories.domain.core.language.GetDeviceLanguageCode
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

interface AuthInteractor {
    val authState: StateFlow<AuthState>
}

internal class AuthInteractorImpl(
    private val authApiRepository: AuthApiRepository,
    private val jwtTokensRepository: JwtTokensRepository,
    private val getDeviceLanguageCode: GetDeviceLanguageCode,
    private val authSettings: Settings,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher,
) : AuthInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unknown)
    override val authState = _authState.asStateFlow()

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    init {
        startListeningToJwtTokensState()
    }

    private fun startListeningToJwtTokensState() {
        coroutineScope.launch {
            jwtTokensRepository.accessTokenState.collect { state ->
                when (state) {
                    AccessTokenState.UNKNOWN -> {
                        _authState.value = AuthState.Unknown
                    }

                    AccessTokenState.PRESENT -> {
                        _authState.value = if (isAnonymousAuthentication()) {
                            AuthState.Authenticated.Anonymous
                        } else {
                            AuthState.Authenticated.Normal
                        }
                    }

                    AccessTokenState.MISSING -> {
                        _authState.value = AuthState.NotAuthenticated

                        authenticateAnonymously()
                    }
                }
            }
        }
    }

    private fun authenticateAnonymously() {
        logger.d { "authenticateAnonymously" }

        coroutineScope.launch {
            authApiRepository
                .anonymousAuth(language = getDeviceLanguageCode())
                .catch { t ->
                    logger.w(t) { "authenticateAnonymously: failed, ${t.message}" }
                }
                .collect { jwtResponse ->
                    logger.d { "authenticateAnonymously: success" }

                    setAnonymousAuthentication(true)

                    jwtTokensRepository.setAccessToken(jwtResponse.accessToken)
                    jwtTokensRepository.setRefreshToken(jwtResponse.refreshToken)
                }
        }
    }

    private suspend fun isAnonymousAuthentication(): Boolean {
        return withContext(ioDispatcher) {
            authSettings.getBoolean(KEY_IS_ANONYMOUS_AUTH, true)
        }
    }

    private suspend fun setAnonymousAuthentication(isAnonymous: Boolean) {
        withContext(ioDispatcher) {
            authSettings.putBoolean(KEY_IS_ANONYMOUS_AUTH, isAnonymous)
        }
    }

    private companion object {
        private const val LOG_TAG = "AuthInteractor"

        private const val KEY_IS_ANONYMOUS_AUTH = "is_anonymous_auth"
    }
}