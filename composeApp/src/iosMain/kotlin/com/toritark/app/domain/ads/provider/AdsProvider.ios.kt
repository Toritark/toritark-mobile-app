@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.ads.provider

import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import com.toritark.app.domain.core.debug.IsDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

internal actual class AdsProviderImpl(
    private val isDebug: IsDebug,
    private val defaultDispatcher: CoroutineDispatcher,
) : AdsProvider {

    private val logger = Logger.withTag(LOG_TAG)

    private val _isBannerAvailable = MutableStateFlow(false)
    actual override val isBannerAvailable = _isBannerAvailable.asStateFlow()

    private val _isInterstitialAvailable = MutableStateFlow(false)
    actual override val isInterstitialAvailable = _isInterstitialAvailable.asStateFlow()

    private val _isRewardedAvailable = MutableStateFlow(false)
    actual override val isRewardedAvailable = _isRewardedAvailable.asStateFlow()

    private val _rewardedAdFinishedEvents = MutableSharedFlow<RewardedVideoResult>()
    actual override val rewardedAdFinishedEvents = _rewardedAdFinishedEvents.asSharedFlow()

    private val coroutineScope by lazy { CoroutineScope(defaultDispatcher + SupervisorJob()) }

    actual override suspend fun initialize(userId: Long) {
    }

    actual override suspend fun checkConsent() {
    }

    actual override suspend fun canShowBanner(placementName: String?): Boolean {
        logger.e { "canShowBanner: not implemented" }
        return false
    }

    actual override suspend fun canShowInterstitial(placementName: String?): Boolean {
        logger.e { "canShowInterstitial: not implemented" }
        return false
    }

    actual override suspend fun canShowRewarded(placementName: String?): Boolean {
        logger.e { "canShowRewarded: not implemented" }
        return false
    }

    actual override suspend fun showBanner(placementName: String?): Boolean {
        logger.e { "showBanner: not implemented" }
        return false
    }

    actual override suspend fun showInterstitial(placementName: String?): Boolean {
        logger.e { "showInterstitial: not implemented" }
        return false
    }

    actual override suspend fun showRewarded(placementName: String?): Boolean {
        logger.e { "showRewarded: not implemented" }
        return false
    }

    actual override suspend fun hideBanner(): Boolean {
        logger.e { "hideBanner: not implemented" }
        return false
    }

    private companion object {
        private const val LOG_TAG = "AdsProvider"
    }
}