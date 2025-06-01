@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalNativeApi::class, BetaInteropApi::class)

package com.toritark.app.domain.ads.provider

import co.touchlab.kermit.Logger
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
        logger.e { "initialize NOT IMPLEMENTED: userId=$userId" }

        this.userId = userId.toString()

        GADMobileAds.sharedInstance().startWithCompletionHandler { status ->
            logger.i { "initialize: initialization done" }

            initializeRewardedAds()
        }
    }

    private fun initializeRewardedAds() {
        val placements = listOf(
            AdPlacement.Rewarded.Generation,
            AdPlacement.Rewarded.RetellingCheck,
        )

        placements.forEach { placement ->
            loadAndSaveRewardedAd(placement = placement)
        }
    }

    actual override suspend fun checkConsent() {
        // Shown automatically
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

    private companion object {
        private const val LOG_TAG = "AdsProvider"

        private const val TEST_REWARDED_AD_ID = "ca-app-pub-3940256099942544/1712485313"

        private const val PRODUCTION_REWARDED_STORY_AD_ID = "ca-app-pub-4910514618209773/6913623591"
        private const val PRODUCTION_REWARDED_RETELLING_CHECK_AD_ID = "ca-app-pub-4910514618209773/6742363083"

        private val rewardedAdIds = mapOf(
            AdPlacement.Rewarded.Generation to PRODUCTION_REWARDED_STORY_AD_ID,
            AdPlacement.Rewarded.RetellingCheck to PRODUCTION_REWARDED_RETELLING_CHECK_AD_ID,
        )
    }

}
//internal actual class AdsProviderImpl(
//    private val isDebug: IsDebug,
//    private val defaultDispatcher: CoroutineDispatcher,
//) : AdsProvider, IOSAdsProvider {
//
//    private val logger = Logger.withTag(LOG_TAG)
//
//    private val _isBannerAvailable = MutableStateFlow(false)
//    actual override val isBannerAvailable = _isBannerAvailable.asStateFlow()
//
//    private val _isInterstitialAvailable = MutableStateFlow(false)
//    actual override val isInterstitialAvailable = _isInterstitialAvailable.asStateFlow()
//
//    private val _isRewardedAvailable = MutableStateFlow(false)
//    actual override val isRewardedAvailable = _isRewardedAvailable.asStateFlow()
//
//    private val _rewardedAdFinishedEvents = MutableSharedFlow<RewardedVideoResult>()
//    actual override val rewardedAdFinishedEvents = _rewardedAdFinishedEvents.asSharedFlow()
//
//    private var viewController: WeakReference<UIViewController>? = null
//
//    private val coroutineScope by lazy { CoroutineScope(defaultDispatcher + SupervisorJob()) }
//
//    actual override suspend fun initialize(userId: Long) {
//        logger.d { "initialize: userId=$userId" }
//
//        Appodeal.setTestingEnabled(isDebug())
//        Appodeal.setAutocache(
//            autocache = true,
//            types = AppodealAdTypeBanner or AppodealAdTypeInterstitial or AppodealAdTypeRewardedVideo,
//        )
//        Appodeal.setLogLevel(if (isDebug()) APDLogLevelDebug else APDLogLevelOff)
//
//        Appodeal.setInitializationDelegate(object : NSObject(), AppodealInitializationDelegateProtocol {
//            override fun appodealSDKDidInitialize() {
//                logger.d { "Appodeal initialized for user $userId" }
//            }
//        })
//
//        Appodeal.setBannerDelegate(bannerDelegate)
//        Appodeal.setInterstitialDelegate(interstitialDelegate)
//        Appodeal.setRewardedVideoDelegate(rewardedVideoDelegate)
//        Appodeal.setAdRevenueDelegate(adRevenueDelegate)
//
////        Appodeal.initializeWithApiKey(
////            apiKey = BuildKonfig.APPODEAL_KEY,
////            types = AppodealAdTypeBanner// or AppodealAdTypeInterstitial or AppodealAdTypeRewardedVideo,
////        )
//    }
//
//    actual override suspend fun checkConsent() {
//        // Shown automatically
//    }
//
//    actual override suspend fun canShowBanner(placementName: String?): Boolean {
//        return Appodeal.canShow(
//            type = AppodealAdTypeBanner,
//            forPlacement = placementName.orEmpty(),
//        )
//    }
//
//    actual override suspend fun canShowInterstitial(placementName: String?): Boolean {
//        return Appodeal.canShow(
//            type = AppodealAdTypeInterstitial,
//            forPlacement = placementName.orEmpty(),
//        )
//    }
//
//    actual override suspend fun canShowRewarded(placementName: String?): Boolean {
//        return Appodeal.canShow(
//            type = AppodealAdTypeRewardedVideo,
//            forPlacement = placementName.orEmpty(),
//        )
//    }
//
//    actual override suspend fun showBanner(placementName: String?): Boolean {
//        // Do not show banner for now
//        return false
//    }
//
//    actual override suspend fun showInterstitial(placementName: String?): Boolean {
//        logger.d { "showInterstitial: placementName=$placementName" }
//
//        return showAd(style = AppodealShowStyleInterstitial, placementName = placementName)
//    }
//
//    actual override suspend fun showRewarded(placementName: String?): Boolean {
//        logger.d { "showRewarded: placementName=$placementName" }
//
//        return showAd(style = AppodealShowStyleInterstitial, placementName = placementName)
//    }
//
//    actual override suspend fun hideBanner(): Boolean {
//        // Not implemented for now
//        return false
//    }
//
//    private fun showAd(style: AppodealShowStyle, placementName: String?): Boolean {
//        val viewController = viewController?.get() ?: run {
//            logger.w { "showAd: style=$style, placementName=$placementName: viewController == null" }
//            return false
//        }
//
//        return Appodeal.showAd(
//            style = style,
//            forPlacement = placementName.orEmpty(),
//            rootViewController = viewController,
//        )
//    }
//
//    override fun setViewController(viewController: UIViewController?) {
//        logger.i { "setViewController: viewController=$viewController" }
//
//        this.viewController = if (viewController != null) {
//            WeakReference(viewController)
//        } else {
//            null
//        }
//    }
//
//    /**
//     * Banner delegate
//     */
//    private val bannerDelegate: AppodealBannerDelegateProtocol = object : NSObject(), AppodealBannerDelegateProtocol {
//
//        override fun bannerDidClick() {
//            logger.d { "bannerDidClick" }
//
//            Analytics.logEvent(
//                AnalyticsEvent(
//                    name = "ad_banner_click",
//                )
//            )
//        }
//
//        override fun bannerDidExpired() {
//            logger.d { "bannerDidExpired" }
//        }
//
//        override fun bannerDidFailToLoadAd() {
//            logger.w { "bannerDidFailToLoadAd" }
//
//            _isBannerAvailable.value = false
//        }
//
//        override fun bannerDidFailToPresentWithError(error: NSError) {
//            logger.w { "bannerDidFailToLoadWithError: code=${error.code}, description=${error.description}" }
//        }
//
//        override fun bannerDidLoadAdIsPrecache(precache: Boolean) {
//            logger.d { "bannerDidLoadAdIsPrecache: precache=$precache" }
//
//            _isBannerAvailable.value = true
//        }
//
//        override fun bannerDidShow() {
//            logger.d { "bannerDidShow" }
//        }
//    }
//
//    /**
//     * Interstitial delegate
//     */
//    private val interstitialDelegate: AppodealInterstitialDelegateProtocol =
//        object : NSObject(), AppodealInterstitialDelegateProtocol {
//
//            override fun interstitialDidClick() {
//                logger.d { "interstitialDidClick" }
//
//                Analytics.logEvent(
//                    AnalyticsEvent(
//                        name = "ad_interstitial_click",
//                    )
//                )
//            }
//
//            override fun interstitialDidDismiss() {
//                logger.d { "interstitialDidDismiss" }
//            }
//
//            override fun interstitialDidExpired() {
//                logger.d { "interstitialDidExpired" }
//            }
//
//            override fun interstitialDidFailToLoadAd() {
//                logger.w { "interstitialDidFailToLoadAd" }
//
//                _isInterstitialAvailable.value = false
//            }
//
//            override fun interstitialDidFailToPresent() {
//                logger.w { "interstitialDidFailToPresent" }
//            }
//
//            override fun interstitialDidLoadAdIsPrecache(precache: Boolean) {
//                logger.d { "interstitialDidLoadAdIsPrecache: precache=$precache" }
//
//                _isInterstitialAvailable.value = true
//            }
//
//            override fun interstitialWillPresent() {
//                logger.d { "interstitialWillPresent" }
//            }
//        }
//
//    /**
//     * Rewarded video delegate
//     */
//    private val rewardedVideoDelegate: AppodealRewardedVideoDelegateProtocol =
//        object : NSObject(), AppodealRewardedVideoDelegateProtocol {
//
//            override fun rewardedVideoDidClick() {
//                logger.d { "rewardedVideoDidClick" }
//
//                Analytics.logEvent(
//                    AnalyticsEvent(
//                        name = "ad_rewarded_video_click",
//                    )
//                )
//            }
//
//            override fun rewardedVideoDidExpired() {
//                logger.d { "rewardedVideoDidExpired" }
//            }
//
//            override fun rewardedVideoDidFailToLoadAd() {
//                logger.w { "rewardedVideoDidFailToLoadAd" }
//
//                _isRewardedAvailable.value = true
//            }
//
//            override fun rewardedVideoDidFailToPresentWithError(error: NSError) {
//                logger.e { "rewardedVideoDidFailToPresentWithError: code=${error.code}, description=${error.description}" }
//            }
//
//            override fun rewardedVideoDidFinish(rewardAmount: Float, name: String?) {
//                logger.d { "rewardedVideoDidFinish: rewardAmount=$rewardAmount, name=$name" }
//
//                Analytics.logEvent(
//                    AnalyticsEvent(
//                        name = "ad_rewarded_video_finished",
//                        parameters = mapOf(
//                            "amount" to rewardAmount,
//                            "currency" to name,
//                        )
//                    )
//                )
//            }
//
//            override fun rewardedVideoDidLoadAdIsPrecache(precache: Boolean) {
//                logger.d { "rewardedVideoDidLoadAdIsPrecache: precache=$precache" }
//
//                _isRewardedAvailable.value = true
//            }
//
//            override fun rewardedVideoDidPresent() {
//                logger.d { "rewardedVideoDidPresent" }
//            }
//
//            override fun rewardedVideoWillDismissAndWasFullyWatched(wasFullyWatched: Boolean) {
//                logger.d { "rewardedVideoWillDismissAndWasFullyWatched: wasFullyWatched=$wasFullyWatched" }
//
//                coroutineScope.launch {
//                    val result = RewardedVideoResult(
//                        isFinished = wasFullyWatched,
//                    )
//                    _rewardedAdFinishedEvents.emit(result)
//                }
//
//                Analytics.logEvent(
//                    AnalyticsEvent(
//                        name = "ad_rewarded_video_close",
//                        parameters = mapOf(
//                            "is_finished" to wasFullyWatched,
//                        )
//                    )
//                )
//            }
//        }
//
//    /**
//     * Ad revenue delegate
//     */
//    private val adRevenueDelegate: AppodealAdRevenueDelegateProtocol =
//        object : NSObject(), AppodealAdRevenueDelegateProtocol {
//            override fun didReceiveRevenueForAd(ad: AppodealAdRevenueProtocol) {
//                logger.i {
//                    "didReceiveRevenueForAd: network=${ad.networkName}, unit=${ad.adUnitName}, placement=${ad.placement}, " +
//                            "precision=${ad.revenuePrecision}, demand=${ad.demandSource}, currency=${ad.currency}, " +
//                            "revenue=${ad.revenue}, ad_type=${ad.adTypeString}"
//                }
//
//                Analytics.logAdRevenue(
//                    format = ad.adTypeString,
//                    source = ad.networkName,
//                    adUnitName = ad.adUnitName,
//                    amount = ad.revenue,
//                    currency = ad.currency,
//                )
//            }
//        }
//
//    private companion object {
//        private const val LOG_TAG = "AdsProvider"
//    }
//}