package com.toritark.app.data.ads.api.repository

import com.toritark.app.data.ads.api.model.RewardedAdBonusesCountApiModel
import com.toritark.app.data.ads.api.resource.AdsApiResources
import com.toritark.app.data.core_api.base.repository.ApiRepository
import com.toritark.app.data.core_api.base.repository.BaseApiRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.resources.*
import kotlinx.coroutines.CoroutineDispatcher

internal interface AdsApiRepository : ApiRepository {
    suspend fun getNotConsumedRewardedAdBonusesCount(): RewardedAdBonusesCountApiModel
}

internal class AdsApiRepositoryImpl(
    httpClient: HttpClient,
    ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher,
) : BaseApiRepository(
    httpClient = httpClient,
    ioDispatcher = ioDispatcher,
    defaultDispatcher = defaultDispatcher,
), AdsApiRepository {

    override suspend fun getNotConsumedRewardedAdBonusesCount(): RewardedAdBonusesCountApiModel {
        return httpClient.get(AdsApiResources.Bonuses.NotConsumed.Count()) {
            expectSuccess = true
        }.body()
    }
}