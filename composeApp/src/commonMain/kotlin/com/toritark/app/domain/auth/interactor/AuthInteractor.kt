@file:OptIn(ExperimentalCoroutinesApi::class)

package com.toritark.app.domain.auth.interactor

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.data.auth.repository.AuthProvidersRepository
import com.toritark.app.data.auth.repository.api.AuthApiRepository
import com.toritark.app.data.core_api.jwt.model.AccessTokenState
import com.toritark.app.data.core_api.jwt.repository.JwtTokensRepository
import com.toritark.app.domain.auth.model.AuthState
import com.toritark.app.domain.core.language.GetDeviceLanguageCode
import com.toritark.app.util.core.extension.flow.typedFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

interface AuthInteractor {
    val authState: StateFlow<AuthState>

    suspend fun getAuthProviders(): List<AuthProvider>

    fun authenticateWithFirebase(token: String): Flow<Unit>
}

internal class AuthInteractorImpl(
    private val authProvidersRepository: AuthProvidersRepository,
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
                    }
                }
            }
        }
    }

    private suspend fun isAnonymousAuthentication(): Boolean {
        return withContext(ioDispatcher) {
            authSettings.getBoolean(KEY_IS_ANONYMOUS_AUTH, false)
        }
    }

    private suspend fun setAnonymousAuthentication(isAnonymous: Boolean) {
        withContext(ioDispatcher) {
            authSettings.putBoolean(KEY_IS_ANONYMOUS_AUTH, isAnonymous)
        }
    }

    override suspend fun getAuthProviders(): List<AuthProvider> {
        return authProvidersRepository.getAuthProviders()
    }

    override fun authenticateWithFirebase(token: String): Flow<Unit> {
        logger.d { "authenticateWithFirebase" }

        return typedFlow { getDeviceLanguageCode() }
            .flatMapConcat { deviceLanguageCode ->
                authApiRepository
                    .firebaseAuth(
                        token = token,
                        language = getDeviceLanguageCode(),
                    )
            }
            .map { jwtResponse ->
                setAnonymousAuthentication(false)

                jwtTokensRepository.setAccessToken(jwtResponse.accessToken)
                jwtTokensRepository.setRefreshToken(jwtResponse.refreshToken)
            }
            .flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "AuthInteractor"

        private const val KEY_IS_ANONYMOUS_AUTH = "is_anonymous_auth"
    }
}