@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.ads.provider

import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AdsProvider {
    val isBannerAvailable: StateFlow<Boolean>
    val isInterstitialAvailable: StateFlow<Boolean>
    val isRewardedAvailable: StateFlow<Boolean>

    val rewardedAdFinishedEvents: SharedFlow<RewardedVideoResult>

    suspend fun initialize(userId: Long)
    suspend fun checkConsent()

    suspend fun canShowBanner(placement: AdPlacement): Boolean
    suspend fun canShowInterstitial(placement: AdPlacement): Boolean
    suspend fun canShowRewarded(placement: AdPlacement): Boolean

    suspend fun showBanner(placement: AdPlacement): Boolean
    suspend fun showInterstitial(placement: AdPlacement): Boolean
    suspend fun showRewarded(placement: AdPlacement): Boolean

    suspend fun hideBanner(): Boolean
}

internal expect class AdsProviderImpl : AdsProvider {
    override val isBannerAvailable: StateFlow<Boolean>
    override val isInterstitialAvailable: StateFlow<Boolean>
    override val isRewardedAvailable: StateFlow<Boolean>

    override val rewardedAdFinishedEvents: SharedFlow<RewardedVideoResult>

    override suspend fun initialize(userId: Long)

    override suspend fun checkConsent()

    override suspend fun canShowBanner(placement: AdPlacement): Boolean
    override suspend fun canShowInterstitial(placement: AdPlacement): Boolean
    override suspend fun canShowRewarded(placement: AdPlacement): Boolean

    override suspend fun showBanner(placement: AdPlacement): Boolean
    override suspend fun showInterstitial(placement: AdPlacement): Boolean
    override suspend fun showRewarded(placement: AdPlacement): Boolean

    override suspend fun hideBanner(): Boolean
}