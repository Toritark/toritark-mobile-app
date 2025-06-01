@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalNativeApi::class, BetaInteropApi::class)

package com.toritark.app.domain.ads.provider

import co.touchlab.kermit.Logger
import cocoapods.GoogleUserMessagingPlatform.UMPConsentForm
import cocoapods.GoogleUserMessagingPlatform.UMPConsentInformation
import cocoapods.GoogleUserMessagingPlatform.UMPRequestParameters
import cocoapods.Google_Mobile_Ads_SDK.*
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import com.toritark.app.domain.core.debug.IsDebug
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ForeignException
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import platform.Foundation.NSError
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

interface IOSAdsProvider {
    fun setViewController(viewController: UIViewController?)
}

internal actual class AdsProviderImpl(
    private val isDebug: IsDebug,
    private val defaultDispatcher: CoroutineDispatcher,
    private val mainDispatcher: CoroutineDispatcher,
) : AdsProvider, IOSAdsProvider {

    private val logger = Logger.withTag(LOG_TAG)

    private val _isBannerAvailable = MutableStateFlow(false)
    actual override val isBannerAvailable = _isBannerAvailable.asStateFlow()

    private val _isInterstitialAvailable = MutableStateFlow(false)
    actual override val isInterstitialAvailable = _isInterstitialAvailable.asStateFlow()

    private val _isRewardedAvailable = MutableStateFlow(true)
    actual override val isRewardedAvailable = _isRewardedAvailable.asStateFlow()

    private val _rewardedAdFinishedEvents = MutableSharedFlow<RewardedVideoResult>()
    actual override val rewardedAdFinishedEvents = _rewardedAdFinishedEvents.asSharedFlow()

    private var userId: String? = null
    private var viewController: WeakReference<UIViewController>? = null

    private val rewardedAds = mutableMapOf<AdPlacement, GADRewardedAd>()

    private val coroutineScope by lazy { CoroutineScope(defaultDispatcher + SupervisorJob()) }

    actual override suspend fun initialize(userId: Long) {
        this.userId = userId.toString()

        GADMobileAds.sharedInstance().startWithCompletionHandler { status ->
            logger.i { "initialize: initialization done" }

            initializeRewardedAds()
        }

        checkConsent()
    }

    private fun initializeRewardedAds() {
        coroutineScope.launch {
            waitForCanRequestAds()

            val placements = listOf(
                AdPlacement.Rewarded.Generation,
                AdPlacement.Rewarded.RetellingCheck,
            )

            placements.forEach { placement ->
                loadAndSaveRewardedAd(placement = placement)
            }
        }
    }

    actual override suspend fun checkConsent() {
        suspendCoroutine { continuation ->
            UMPConsentInformation.sharedInstance().requestConsentInfoUpdateWithParameters(
                parameters = UMPRequestParameters()
            ) { error ->
                logger.i { "checkConsent: info update done" }

                if (error != null) {
                    logger.e { "Failed to request consent info update: ${error.code}, ${error.description}" }
                }

                continuation.resume(Unit)
            }
        }

        val viewController = waitForViewController()
        withContext(mainDispatcher) {
            UMPConsentForm.loadAndPresentIfRequiredFromViewController(
                viewController = viewController,
            ) { error ->
                logger.i { "checkConsent: loadAndPresent done" }

                if (error != null) {
                    logger.e { "Failed to present consent info form: ${error.code}, ${error.description}" }
                }
            }
        }
    }

    actual override suspend fun canShowBanner(placement: AdPlacement): Boolean {
        logger.e { "canShowBanner is not implemented" }
        return false
    }

    actual override suspend fun canShowInterstitial(placement: AdPlacement): Boolean {
        logger.e { "canShowInterstitial is not implemented" }
        return false
    }

    actual override suspend fun canShowRewarded(placement: AdPlacement): Boolean {
        return rewardedAds.containsKey(placement)
    }

    private fun loadRewardedAd(placement: AdPlacement, onCompletion: (ad: GADRewardedAd?) -> Unit) {
        logger.i { "loadRewardedAd: placement=$placement" }

        val adId = getRewardedAdId(placement = placement)
        if (adId == null) {
            logger.e { "loadRewardedAd: no ad id for placement=$placement" }
            onCompletion(null)
            return
        }

        try {
            GADRewardedAd.loadWithAdUnitID(
                adUnitID = adId,
                request = GADRequest(),
            ) { ad: GADRewardedAd?, error: NSError? ->
                if (error != null) {
                    logger.e {
                        "Failed to load rewarded ad for placement=$placement: ${error.code}, ${error.description}"
                    }

                    onCompletion(null)
                    return@loadWithAdUnitID
                }

                ad?.apply {
                    fullScreenContentDelegate = rewardedAdCallback
                    serverSideVerificationOptions = GADServerSideVerificationOptions().apply {
                        logger.i { "Loaded rewarded ad, adding userId=$userId" }
                        this.setUserIdentifier(userId)
                    }
                }

                onCompletion(ad)
            }
        } catch (e: ForeignException) {
            logger.e(e) { "Failed to load rewarded ad for placement=$placement" }
            onCompletion(null)
        }
    }

    private fun loadAndSaveRewardedAd(placement: AdPlacement) {
        coroutineScope.launch {
            withContext(defaultDispatcher) {
                loadRewardedAd(placement = placement) { ad ->
                    ad?.let { rewardedAds[placement] = it } ?: rewardedAds.remove(placement)
                }
            }
        }
    }

    actual override suspend fun showBanner(placement: AdPlacement): Boolean {
        // Do not show banner for now
        return false
    }

    actual override suspend fun showInterstitial(placement: AdPlacement): Boolean {
        logger.e { "showInterstitial: placement=$placement" }

        return false
    }

    actual override suspend fun showRewarded(placement: AdPlacement): Boolean {
        logger.i { "showRewarded: placement=$placement" }

        val viewController = viewController?.get()
        if (viewController == null) {
            logger.w { "showRewarded: No viewController available" }
            return false
        }

        var ad = rewardedAds.remove(placement)
        if (ad == null) {
            ad = suspendCoroutine { continuation ->
                loadRewardedAd(placement) { loadedAd ->
                    continuation.resume(loadedAd)
                }
            }
        }

        if (ad == null) {
            logger.w { "Failed to load rewarded ad for placement=$placement" }
            loadAndSaveRewardedAd(placement = placement)
            return false
        }

        return try {
            loadAndSaveRewardedAd(placement = placement)

            withContext(mainDispatcher) {
                ad.presentFromRootViewController(viewController) {
                    logger.i { "User got reward" }
                }
            }

            true
        } catch (e: ForeignException) {
            logger.e(e) { "showRewarded: Failed to show: ${e.message}" }

            false
        }
    }

    actual override suspend fun hideBanner(): Boolean {
        // Not implemented for now
        return false
    }

    override fun setViewController(viewController: UIViewController?) {
        logger.i { "setViewController: viewController=$viewController" }

        this.viewController = if (viewController != null) {
            WeakReference(viewController)
        } else {
            null
        }
    }

    /**
     * Rewarded ad callback
     */
    private val rewardedAdCallback = object : GADFullScreenContentDelegateProtocol, NSObject() {
        override fun ad(
            ad: GADFullScreenPresentingAdProtocol,
            didFailToPresentFullScreenContentWithError: NSError,
        ) {
            logger.w {
                "Failed to show rewarded ad: ${didFailToPresentFullScreenContentWithError.code} " +
                        "${didFailToPresentFullScreenContentWithError.description}"
            }
        }

        override fun adDidDismissFullScreenContent(ad: GADFullScreenPresentingAdProtocol) {
            logger.i { "Rewarded ad dismissed" }
        }

        override fun adDidRecordClick(ad: GADFullScreenPresentingAdProtocol) {
            logger.i { "Rewarded ad clicked" }
        }

        override fun adDidRecordImpression(ad: GADFullScreenPresentingAdProtocol) {
            logger.i { "Rewarded ad impression recorded" }
        }

        override fun adWillDismissFullScreenContent(ad: GADFullScreenPresentingAdProtocol) {
            logger.i { "Rewarded ad will dismiss" }

            coroutineScope.launch {
                _rewardedAdFinishedEvents.emit(RewardedVideoResult(isFinished = true))
            }
        }

        override fun adWillPresentFullScreenContent(ad: GADFullScreenPresentingAdProtocol) {
            logger.i { "Rewarded ad will present" }
        }
    }

    private fun getRewardedAdId(placement: AdPlacement): String? {
        logger.i { "getRewardedAdId: placement=$placement" }

//        if (isDebug()) {
//            return TEST_REWARDED_AD_ID
//        }

        return rewardedAdIds[placement]
    }

    // Hacky, but ok for MVP
    private suspend fun waitForViewController(): UIViewController {
        return withContext(defaultDispatcher) {
            while (true) {
                val viewController = viewController?.get()
                if (viewController != null) {
                    return@withContext viewController
                }

                delay(WAIT_FOR_VIEW_CONTROLLER_INTERVAL_MS)
            }

            @Suppress("KotlinUnreachableCode")
            throw IllegalStateException()
        }
    }

    // Hacky, but ok for MVP
    private suspend fun waitForCanRequestAds() {
        withContext(defaultDispatcher) {
            while (!UMPConsentInformation.sharedInstance().canRequestAds) {
                delay(WAIT_FOR_CAN_REQUEST_ADS_INTERVAL_MS)
            }

            logger.i { "Can request ads now" }
        }
    }

    private companion object {
        private const val LOG_TAG = "AdsProvider"

        // TODO: Replace busy-waiting with kind of signalling
        private const val WAIT_FOR_VIEW_CONTROLLER_INTERVAL_MS = 100L
        private const val WAIT_FOR_CAN_REQUEST_ADS_INTERVAL_MS = 300L

        private const val TEST_REWARDED_AD_ID = "ca-app-pub-3940256099942544/1712485313"

        private const val PRODUCTION_REWARDED_STORY_AD_ID = "ca-app-pub-4910514618209773/6913623591"
        private const val PRODUCTION_REWARDED_RETELLING_CHECK_AD_ID = "ca-app-pub-4910514618209773/6742363083"

        private val rewardedAdIds = mapOf(
            AdPlacement.Rewarded.Generation to PRODUCTION_REWARDED_STORY_AD_ID,
            AdPlacement.Rewarded.RetellingCheck to PRODUCTION_REWARDED_RETELLING_CHECK_AD_ID,
        )
    }

}