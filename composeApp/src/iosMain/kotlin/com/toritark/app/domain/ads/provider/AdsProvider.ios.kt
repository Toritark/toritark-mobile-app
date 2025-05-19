@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.ads.provider

import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

internal actual class AdsProviderImpl() : AdsProvider {
    actual override val isBannerAvailable: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual override val isInterstitialAvailable: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual override val isRewardedAvailable: StateFlow<Boolean>
        get() = TODO("Not yet implemented")
    actual override val rewardedAdFinishedEvents: SharedFlow<RewardedVideoResult>
        get() = TODO("Not yet implemented")

    actual override suspend fun initialize(userId: Long) {
    }

    actual override suspend fun checkConsent() {
    }

    actual override suspend fun canShowBanner(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun canShowInterstitial(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun canShowRewarded(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun showBanner(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun showInterstitial(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun showRewarded(placementName: String?): Boolean {
        TODO("Not yet implemented")
    }

    actual override suspend fun hideBanner(): Boolean {
        TODO("Not yet implemented")
    }
}