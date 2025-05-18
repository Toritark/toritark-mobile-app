package com.toritark.app.data.auth.api.repository

import co.touchlab.kermit.Logger
import com.toritark.app.data.auth.api.model.AnonymousAuthApiModel
import com.toritark.app.data.auth.api.model.FirebaseAuthApiModel
import com.toritark.app.data.auth.api.resource.AuthApiResources
import com.toritark.app.data.core_api.base.repository.ApiRepository
import com.toritark.app.data.core_api.base.repository.BaseApiRepository
import com.toritark.app.data.core_api.jwt.model.JwtResponse
import com.toritark.app.util.core.extension.flow.typedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal interface AuthApiRepository : ApiRepository {
    fun anonymousAuth(language: String): Flow<JwtResponse>
    fun firebaseAuth(token: String, language: String): Flow<JwtResponse>
}

internal class AuthApiRepositoryImpl(
    httpClient: HttpClient,
    ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher,
) : BaseApiRepository(
    httpClient = httpClient,
    ioDispatcher = ioDispatcher,
    defaultDispatcher = defaultDispatcher,
), AuthApiRepository {

    private val logger = Logger.withTag(LOG_TAG)

    override fun anonymousAuth(language: String): Flow<JwtResponse> {
        logger.d { "anonymousAuth" }

        return typedFlow<JwtResponse> {
            httpClient.post(AuthApiResources.Auth.Anonymous()) {
                setBody(
                    AnonymousAuthApiModel(
                        language = language,
                    )
                )

                expectSuccess = true
            }.body()
        }.flowOn(ioDispatcher)
    }

    override fun firebaseAuth(
        token: String,
        language: String,
    ): Flow<JwtResponse> {
        logger.d { "firebaseAuth" }

        return typedFlow<JwtResponse> {
            httpClient.post(AuthApiResources.Auth.Firebase()) {
                setBody(
                    FirebaseAuthApiModel(
                        token = token,
                        language = language,
                    )
                )

                expectSuccess = true
            }.body()
        }.flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "AuthApiRepository"
    }
}