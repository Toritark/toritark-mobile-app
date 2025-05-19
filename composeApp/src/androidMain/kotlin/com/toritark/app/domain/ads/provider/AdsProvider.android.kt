@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.ads.provider

import android.app.Activity
import co.touchlab.kermit.Logger
import com.appodeal.ads.Appodeal
import com.appodeal.ads.BannerCallbacks
import com.appodeal.ads.InterstitialCallbacks
import com.appodeal.ads.RewardedVideoCallbacks
import com.appodeal.ads.utils.Log
import com.appodeal.consent.ConsentManager
import com.appodeal.consent.ConsentStatus
import com.toritark.app.R
import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import com.toritark.app.domain.core.debug.IsDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

interface AndroidAdsProvider {
    fun setActivity(activity: Activity?)
}

internal actual class AdsProviderImpl(
    private val isDebug: IsDebug,
    private val defaultDispatcher: CoroutineDispatcher,
) : AdsProvider, AndroidAdsProvider {

    private val logger = Logger.withTag(LOG_TAG)

    private val _isBannerAvailable = MutableStateFlow(false)
    actual override val isBannerAvailable = _isBannerAvailable.asStateFlow()

    private val _isInterstitialAvailable = MutableStateFlow(false)
    actual override val isInterstitialAvailable = _isInterstitialAvailable.asStateFlow()

    private val _isRewardedAvailable = MutableStateFlow(false)
    actual override val isRewardedAvailable = _isRewardedAvailable.asStateFlow()

    private val _rewardedAdFinishedEvents = MutableSharedFlow<RewardedVideoResult>()
    actual override val rewardedAdFinishedEvents = _rewardedAdFinishedEvents.asSharedFlow()

    private var activity: WeakReference<Activity>? = null

    private val coroutineScope by lazy { CoroutineScope(defaultDispatcher + SupervisorJob()) }

    actual override suspend fun initialize(userId: Long) {
        val activity = activity?.get() ?: run {
            logger.w { "initialize: no activity" }
            return
        }

        logger.d { "initialize: userId=$userId" }

        Appodeal.setTesting(isDebug())
        Appodeal.setLogLevel(if (isDebug()) Log.LogLevel.verbose else Log.LogLevel.none)
        Appodeal.muteVideosIfCallsMuted(true)

        Appodeal.setRewardedVideoCallbacks(rewardedVideoCallbacks)
        Appodeal.setBannerCallbacks(bannerCallbacks)
        Appodeal.setInterstitialCallbacks(interstitialCallbacks)

        Appodeal.setUserId(userId.toString())

        Appodeal.initialize(
            context = activity,
            appKey = activity.getString(R.string.appodeal_app_key),
            adTypes = Appodeal.INTERSTITIAL or Appodeal.REWARDED_VIDEO or Appodeal.BANNER,
            callback = { errors ->
                if (errors == null) {
                    logger.d { "Initialized appodeal" }
                } else {
                    logger.e { "Failed to initialize appodeal: ${errors.joinToString()}" }
                }
            },
        )
    }

    actual override suspend fun checkConsent() {
        when (val consentStatus = ConsentManager.status) {
            ConsentStatus.Required, ConsentStatus.Unknown -> {
                logger.d { "Consent status is required or unknown" }

                requestConsent()
            }

            else -> {
                logger.d { "ConsentStatus is not required: $consentStatus" }
            }
        }
    }

    private fun requestConsent() {
        val activity = activity?.get() ?: run {
            logger.w { "requestConsent: Activity is null" }
            return
        }

        ConsentManager.load(
            context = activity,
            successListener = { consentForm ->
                logger.d { "Loaded consent form" }

                consentForm.show(
                    activity = activity,
                    listener = { e ->
                        if (e == null) {
                            logger.d { "ConsentForm dismissed without errors" }
                        } else {
                            logger.w(e) { "ConsentForm dismissed with error: $e" }
                        }
                    }
                )
            },
            failureListener = { e ->
                logger.e(e) { "Failed to load consent form" }
            }
        )
    }

    actual override suspend fun canShowBanner(placementName: String?): Boolean {
        return placementName
            ?.let { Appodeal.canShow(Appodeal.BANNER_VIEW, it) }
            ?: Appodeal.canShow(Appodeal.BANNER_VIEW)
    }

    actual override suspend fun canShowInterstitial(placementName: String?): Boolean {
        return placementName
            ?.let { Appodeal.canShow(Appodeal.INTERSTITIAL, it) }
            ?: Appodeal.canShow(Appodeal.INTERSTITIAL)
    }

    actual override suspend fun canShowRewarded(placementName: String?): Boolean {
        return placementName
            ?.let { Appodeal.canShow(Appodeal.REWARDED_VIDEO, it) }
            ?: Appodeal.canShow(Appodeal.REWARDED_VIDEO)
    }

    actual override suspend fun showBanner(placementName: String?): Boolean {
        return showAd(adType = Appodeal.BANNER_VIEW, placementName = placementName)
    }

    actual override suspend fun showInterstitial(placementName: String?): Boolean {
        return showAd(adType = Appodeal.INTERSTITIAL, placementName = placementName)
    }

    actual override suspend fun showRewarded(placementName: String?): Boolean {
        return showAd(adType = Appodeal.REWARDED_VIDEO, placementName = placementName)
    }

    private fun showAd(adType: Int, placementName: String?): Boolean {
        val activity = activity?.get() ?: run {
            logger.w { "showAd: adType=$adType, placementName=$placementName: No activity" }
            return false
        }

        logger.d { "showAd: adType=$adType, placementName=$placementName: Showing" }

        return placementName
            ?.let { placementName ->
                Appodeal.show(activity, adType, placementName)
            }
            ?: Appodeal.show(activity, adType)
    }

    actual override suspend fun hideBanner(): Boolean {
        val activity = activity?.get() ?: run {
            logger.w { "hideBanner: No activity" }
            return false
        }

        Appodeal.hide(activity, Appodeal.BANNER_VIEW)

        return true
    }

    override fun setActivity(activity: Activity?) {
        logger.d { "setActivity: $activity" }

        if (activity != null) {
            this.activity = WeakReference(activity)
        } else {
            this.activity = null
        }
    }

    /**
     * Banner callbacks
     */
    private val bannerCallbacks = object : BannerCallbacks {
        override fun onBannerClicked() {
            logger.d { "onBannerClicked" }
        }

        override fun onBannerExpired() {
            logger.d { "onBannerExpired" }
        }

        override fun onBannerFailedToLoad() {
            logger.w { "onBannerFailedToLoad" }

            _isBannerAvailable.value = false
        }

        override fun onBannerLoaded(height: Int, isPrecache: Boolean) {
            logger.d { "onBannerLoaded: height=$height, isPrecache=$isPrecache" }

            _isBannerAvailable.value = true
        }

        override fun onBannerShowFailed() {
            logger.w { "onBannerShowFailed" }
        }

        override fun onBannerShown() {
            logger.d { "onBannerShown" }
        }
    }

    /**
     * Interstitial callbacks
     */
    private val interstitialCallbacks = object : InterstitialCallbacks {
        override fun onInterstitialClicked() {
            logger.d { "onInterstitialClicked" }
        }

        override fun onInterstitialClosed() {
            logger.d { "onInterstitialClosed" }
        }

        override fun onInterstitialExpired() {
            logger.d { "onInterstitialExpired" }
        }

        override fun onInterstitialFailedToLoad() {
            logger.w { "onInterstitialFailedToLoad" }

            _isInterstitialAvailable.value = false
        }

        override fun onInterstitialLoaded(isPrecache: Boolean) {
            logger.d { "onInterstitialLoaded: isPrecache=$isPrecache" }

            _isInterstitialAvailable.value = true
        }

        override fun onInterstitialShowFailed() {
            logger.w { "onInterstitialShowFailed" }
        }

        override fun onInterstitialShown() {
            logger.d { "onInterstitialShown" }
        }
    }

    /**
     * Rewarded video callbacks
     */
    private val rewardedVideoCallbacks = object : RewardedVideoCallbacks {
        override fun onRewardedVideoLoaded(isPrecache: Boolean) {
            logger.d { "onRewardedVideoLoaded: isPrecache=$isPrecache" }

            _isRewardedAvailable.value = true
        }

        override fun onRewardedVideoFailedToLoad() {
            logger.w { "onRewardedVideoFailedToLoad" }

            _isRewardedAvailable.value = true
        }

        override fun onRewardedVideoShown() {
            logger.d { "onRewardedVideoShown" }
        }

        override fun onRewardedVideoShowFailed() {
            logger.w { "onRewardedVideoShowFailed" }

            coroutineScope.launch {
                val result = RewardedVideoResult(
                    isFinished = false,
                )
                _rewardedAdFinishedEvents.emit(result)
            }
        }

        override fun onRewardedVideoClicked() {
            logger.d { "onRewardedVideoClicked" }
        }

        override fun onRewardedVideoFinished(amount: Double, currency: String) {
            logger.d { "onRewardedVideoFinished: amount=$amount, currency=$currency" }
        }

        override fun onRewardedVideoClosed(finished: Boolean) {
            logger.d { "onRewardedVideoClosed: finished=$finished" }

            coroutineScope.launch {
                val result = RewardedVideoResult(
                    isFinished = finished,
                )
                _rewardedAdFinishedEvents.emit(result)
            }
        }

        override fun onRewardedVideoExpired() {
            logger.d { "onRewardedVideoExpired" }
        }
    }

    private companion object {
        private const val LOG_TAG = "AdsProvider"
    }
}