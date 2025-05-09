package com.toritark.stories.data.core_api.jwt.repository

import co.touchlab.kermit.Logger
import com.russhwolf.settings.Settings
import com.toritark.stories.data.core_api.jwt.model.AccessTokenState
import com.toritark.stories.data.core_api.jwt.model.RefreshJwtTokenRequest
import com.toritark.stories.data.core_api.jwt.model.RefreshJwtTokenResponse
import com.toritark.stories.data.core_api.jwt.resource.JwtApiResources
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface JwtTokensRepository {
    val accessTokenState: StateFlow<AccessTokenState>

    suspend fun getRefreshToken(): String?
    suspend fun getAccessToken(): String?

    suspend fun setRefreshToken(refreshToken: String)
    suspend fun setAccessToken(accessToken: String)

    suspend fun clearTokens()

    suspend fun refreshAccessToken(): Boolean
}

internal class JwtTokensRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: Settings,
    private val ioDispatcher: CoroutineDispatcher,
) : JwtTokensRepository {

    private val logger = Logger.withTag(LOG_TAG)

    private val _accessTokenState = MutableStateFlow(AccessTokenState.UNKNOWN)
    override val accessTokenState = _accessTokenState.asStateFlow()

    private var cachedAccessToken: String? = null
    private var cachedRefreshToken: String? = null

    private val mutex = Mutex()

    init {
        CoroutineScope(ioDispatcher).launch {
            _accessTokenState.value = if (getAccessToken() != null) {
                AccessTokenState.PRESENT
            } else {
                AccessTokenState.MISSING
            }
        }
    }

    override suspend fun getRefreshToken(): String? {
        cachedRefreshToken?.let { return it }

        return mutex.withLock {
            withContext(ioDispatcher) {
                val refreshToken = settings.getStringOrNull(REFRESH_TOKEN_KEY)
                cachedRefreshToken = refreshToken

                return@withContext refreshToken
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        cachedAccessToken?.let { return it }

        return mutex.withLock {
            withContext(ioDispatcher) {
                val accessToken = settings.getStringOrNull(ACCESS_TOKEN_KEY)
                cachedAccessToken = accessToken

                return@withContext accessToken
            }
        }
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        logger.d { "setRefreshToken" }

        mutex.withLock {
            cachedRefreshToken = refreshToken

            withContext(ioDispatcher) {
                settings.putString(REFRESH_TOKEN_KEY, refreshToken)
            }
        }
    }

    override suspend fun setAccessToken(accessToken: String) {
        logger.d { "setAccessToken" }

        mutex.withLock {
            cachedAccessToken = accessToken

            withContext(ioDispatcher) {
                settings.putString(ACCESS_TOKEN_KEY, accessToken)
            }

            _accessTokenState.value = AccessTokenState.PRESENT
        }
    }

    override suspend fun clearTokens() {
        logger.d { "clearTokens" }

        mutex.withLock {
            withContext(ioDispatcher) {
                cachedAccessToken = null
                cachedRefreshToken = null

                settings.remove(ACCESS_TOKEN_KEY)
                settings.remove(REFRESH_TOKEN_KEY)
            }

            _accessTokenState.value = AccessTokenState.MISSING
        }
    }

    override suspend fun refreshAccessToken(): Boolean {
        return withContext(ioDispatcher) {
            val refreshToken = getRefreshToken() ?: return@withContext false

            try {
                val response: RefreshJwtTokenResponse = httpClient.post(JwtApiResources.Token.Refresh()) {
                    setBody(
                        RefreshJwtTokenRequest(
                            refreshToken = refreshToken,
                        )
                    )

                    expectSuccess = true
                }.body()

                setAccessToken(response.accessToken)

                return@withContext true
            } catch (t: ClientRequestException) {
                logger.w(t) { "Failed to refresh token: ${t.message}" }

                if (t.response.status == HttpStatusCode.Unauthorized) {
                    logger.w { "Got unauthorized response, will clear tokens: ${t.response.bodyAsText()}" }
                    clearTokens()
                }
            } catch (t: Throwable) {
                logger.w(t) { "Failed to refresh token: ${t.message}" }
            }

            false
        }
    }

    private companion object {
        private const val LOG_TAG = "JwtTokensRepository"

        private const val REFRESH_TOKEN_KEY = "refresh_token"
        private const val ACCESS_TOKEN_KEY = "access_token"
    }
}