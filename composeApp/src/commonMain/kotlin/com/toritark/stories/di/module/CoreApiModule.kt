package com.toritark.stories.di.module

import com.toritark.stories.data.core_api.config.repository.ApiConfigRepository
import com.toritark.stories.data.core_api.config.repository.ApiConfigRepositoryImpl
import com.toritark.stories.data.core_api.jwt.repository.JwtTokensRepository
import com.toritark.stories.data.core_api.jwt.repository.JwtTokensRepositoryImpl
import com.toritark.stories.data.core_preferences.Preferences
import com.toritark.stories.di.name.CoreApiSettingsNames
import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.di.name.HttpClientNames
import com.toritark.stories.domain.core.debug.IsDebug
import com.toritark.stories.util.core_api.logging.KtorLogger
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.resources.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.network.*
import kotlinx.coroutines.runBlocking
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

private const val AUTH_LOG_TAG = "Auth"

private const val CONNECT_TIMEOUT_MS = 1000L * 10
private const val REQUEST_TIMEOUT_MS = 1000L * 20
private const val SOCKET_TIMEOUT_MS = 1000L * 20

private const val MAX_NETWORK_ERRORS_RETRIES = 5
private const val MAX_SERVER_ERRORS_RETRIES = 5

internal expect fun getHttpClientEngine(): HttpClientEngineFactory<HttpClientEngineConfig>

internal val coreApiModule = module {

    single { KtorLogger() } bind Logger::class

    single<ApiConfigRepository> { ApiConfigRepositoryImpl() }

    single(named(HttpClientNames.TOKEN_REFRESH)) {
        val isDebug: IsDebug = get()
        val json: Json = get()
        val apiConfigRepository: ApiConfigRepository = get()

        HttpClient(getHttpClientEngine()) {
            engine {
            }

            install(Logging) {
                val logger: Logger = get()

                this.logger = logger

                level = if (isDebug.execute()) {
                    LogLevel.ALL
                } else {
                    LogLevel.HEADERS
                }

                if (!isDebug.execute()) {
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT_MS
                requestTimeoutMillis = REQUEST_TIMEOUT_MS
                socketTimeoutMillis = SOCKET_TIMEOUT_MS
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = MAX_SERVER_ERRORS_RETRIES)
                retryOnExceptionIf(maxRetries = MAX_NETWORK_ERRORS_RETRIES) { request, cause ->
                    cause is UnresolvedAddressException || cause is IOException
                }
                exponentialDelay()
            }

            defaultRequest {
                val apiConfig = runBlocking { apiConfigRepository.getApiConfig() }

                host = apiConfig.host
                port = apiConfig.port
                url {
                    protocol = if (apiConfig.isHttps) URLProtocol.HTTPS else URLProtocol.HTTP
                }

                contentType(ContentType.Application.Json)
            }

            install(Resources)
        }
    }

    single(named(CoreApiSettingsNames.JWT_TOKENS)) {
        val preferences: Preferences = get()
        preferences.createSettings("jwt_tokens")
    }

    single {
        JwtTokensRepositoryImpl(
            httpClient = get(named(HttpClientNames.TOKEN_REFRESH)),
            settings = get(named(CoreApiSettingsNames.JWT_TOKENS)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    } bind JwtTokensRepository::class


    single(named(HttpClientNames.DEFAULT)) {
        val isDebug: IsDebug = get()
        val json: Json = get()
        val jwtTokensRepository: JwtTokensRepository = get()
        val apiConfigRepository: ApiConfigRepository = get()

        val appLogger = co.touchlab.kermit.Logger.withTag(AUTH_LOG_TAG)

        HttpClient(getHttpClientEngine()) {
            engine {
            }

            install(Logging) {
                val logger: Logger = get()

                this.logger = logger

                level = if (isDebug.execute()) {
                    LogLevel.ALL
                } else {
                    LogLevel.HEADERS
                }

                if (!isDebug.execute()) {
                    sanitizeHeader { header -> header == HttpHeaders.Authorization }
                }
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = jwtTokensRepository.getAccessToken()
                        val refreshToken = jwtTokensRepository.getRefreshToken()

                        if (accessToken != null && refreshToken != null) {
                            BearerTokens(accessToken, refreshToken)
                        } else {
                            null
                        }
                    }
                    refreshTokens {
                        appLogger.d { "refreshTokens" }

                        if (jwtTokensRepository.refreshAccessToken()) {
                            appLogger.d { "refreshTokens success" }

                            val accessToken = jwtTokensRepository.getAccessToken()
                            val refreshToken = jwtTokensRepository.getRefreshToken()

                            if (accessToken != null && refreshToken != null) {
                                BearerTokens(accessToken, refreshToken)
                            } else {
                                null
                            }
                        } else {
                            appLogger.w { "refreshTokens failed" }

                            null
                        }
                    }
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            install(HttpTimeout) {
                connectTimeoutMillis = CONNECT_TIMEOUT_MS
                requestTimeoutMillis = REQUEST_TIMEOUT_MS
                socketTimeoutMillis = SOCKET_TIMEOUT_MS
            }

            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = MAX_SERVER_ERRORS_RETRIES)
                retryOnExceptionIf(maxRetries = MAX_NETWORK_ERRORS_RETRIES) { request, cause ->
                    cause is UnresolvedAddressException || cause is IOException
                }
                exponentialDelay()
            }

            defaultRequest {
                val apiConfig = runBlocking { apiConfigRepository.getApiConfig() }

                host = apiConfig.host
                port = apiConfig.port
                url {
                    protocol = if (apiConfig.isHttps) URLProtocol.HTTPS else URLProtocol.HTTP
                }

                contentType(ContentType.Application.Json)
            }

            install(Resources)
        }
    }

}