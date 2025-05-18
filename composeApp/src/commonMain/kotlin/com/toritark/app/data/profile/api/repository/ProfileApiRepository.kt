package com.toritark.app.data.profile.api.repository

import co.touchlab.kermit.Logger
import com.toritark.app.data.auth.api.model.AnonymousAuthApiModel
import com.toritark.app.data.core_api.base.repository.ApiRepository
import com.toritark.app.data.core_api.base.repository.BaseApiRepository
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.data.profile.api.resource.ProfileApiResources
import com.toritark.app.util.core.extension.flow.typedFlow
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

internal interface ProfileApiRepository : ApiRepository {
    fun getProfile(): Flow<ProfileApiModel>
}

internal class ProfileApiRepositoryImpl(
    httpClient: HttpClient,
    ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher,
) : BaseApiRepository(
    httpClient = httpClient,
    ioDispatcher = ioDispatcher,
    defaultDispatcher = defaultDispatcher,
), ProfileApiRepository {

    private val logger = Logger.withTag(LOG_TAG)

    override fun getProfile(): Flow<ProfileApiModel> {
        logger.d { "getProfile" }

        return typedFlow<ProfileApiModel> {
            httpClient.get(ProfileApiResources.Me()) {
                expectSuccess = true
            }.body()
        }.flowOn(ioDispatcher)
    }

    private companion object {
        private const val LOG_TAG = "ProfileApiRepository"
    }
}