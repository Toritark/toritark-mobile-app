@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.ads.provider

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AdsProvider {
    val isBannerAvailable: StateFlow<Boolean>
    val isInterstitialAvailable: StateFlow<Boolean>
    val isRewardedAvailable: StateFlow<Boolean>

    val rewardedAdFinishedEvents: SharedFlow<Unit>

    suspend fun initialize()
    suspend fun checkConsent()

    suspend fun canShowBanner(placementName: String? = null): Boolean
    suspend fun canShowInterstitial(placementName: String? = null): Boolean
    suspend fun canShowRewarded(placementName: String? = null): Boolean

    suspend fun showBanner(placementName: String? = null): Boolean
    suspend fun showInterstitial(placementName: String? = null): Boolean
    suspend fun showRewarded(placementName: String? = null): Boolean

    suspend fun hideBanner(): Boolean
}

internal expect class AdsProviderImpl : AdsProvider {
    override val isBannerAvailable: StateFlow<Boolean>
    override val isInterstitialAvailable: StateFlow<Boolean>
    override val isRewardedAvailable: StateFlow<Boolean>

    override val rewardedAdFinishedEvents: SharedFlow<Unit>

    override suspend fun initialize()

    override suspend fun checkConsent()

    override suspend fun canShowBanner(placementName: String?): Boolean
    override suspend fun canShowInterstitial(placementName: String?): Boolean
    override suspend fun canShowRewarded(placementName: String?): Boolean

    override suspend fun showBanner(placementName: String?): Boolean
    override suspend fun showInterstitial(placementName: String?): Boolean
    override suspend fun showRewarded(placementName: String?): Boolean

    override suspend fun hideBanner(): Boolean
}