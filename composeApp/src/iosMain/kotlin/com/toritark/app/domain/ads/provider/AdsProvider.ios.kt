@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalNativeApi::class)

package com.toritark.app.domain.ads.provider

import co.touchlab.kermit.Logger
import cocoapods.Appodeal.*
import com.toritark.app.BuildKonfig
import com.toritark.app.data.ads.model.rewarded.RewardedVideoResult
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.model.AnalyticsEvent
import com.toritark.app.domain.core.debug.IsDebug
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.UIKit.UIViewController
import platform.darwin.NSObject
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.WeakReference

interface IOSAdsProvider {
    fun setViewController(viewController: UIViewController?)
}

internal actual class AdsProviderImpl(
    private val isDebug: IsDebug,
    private val defaultDispatcher: CoroutineDispatcher,
) : AdsProvider, IOSAdsProvider {

    private val logger = Logger.withTag(LOG_TAG)

    private val _isBannerAvailable = MutableStateFlow(false)
    actual override val isBannerAvailable = _isBannerAvailable.asStateFlow()

    private val _isInterstitialAvailable = MutableStateFlow(false)
    actual override val isInterstitialAvailable = _isInterstitialAvailable.asStateFlow()

    private val _isRewardedAvailable = MutableStateFlow(false)
    actual override val isRewardedAvailable = _isRewardedAvailable.asStateFlow()

    private val _rewardedAdFinishedEvents = MutableSharedFlow<RewardedVideoResult>()
    actual override val rewardedAdFinishedEvents = _rewardedAdFinishedEvents.asSharedFlow()

    private var viewController: WeakReference<UIViewController>? = null

    private val coroutineScope by lazy { CoroutineScope(defaultDispatcher + SupervisorJob()) }

    actual override suspend fun initialize(userId: Long) {
        logger.d { "initialize: userId=$userId" }

        Appodeal.setTestingEnabled(isDebug())
        Appodeal.setAutocache(
            autocache = true,
            types = AppodealAdTypeBanner or AppodealAdTypeInterstitial or AppodealAdTypeRewardedVideo,
        )
        Appodeal.setLogLevel(if (isDebug()) APDLogLevelDebug else APDLogLevelOff)

        Appodeal.setInitializationDelegate(object : NSObject(), AppodealInitializationDelegateProtocol {
            override fun appodealSDKDidInitialize() {
                logger.d { "Appodeal initialized for user $userId" }
            }
        })

        Appodeal.setBannerDelegate(bannerDelegate)
        Appodeal.setInterstitialDelegate(interstitialDelegate)
        Appodeal.setRewardedVideoDelegate(rewardedVideoDelegate)
        Appodeal.setAdRevenueDelegate(adRevenueDelegate)

        Appodeal.initializeWithApiKey(
            "3bd8b3a757d8fb52be113ab228f056579fc5874d65458486",
            AppodealAdTypeInterstitial
        )

//        Appodeal.initializeWithApiKey(
//            apiKey = BuildKonfig.APPODEAL_KEY,
//            types = AppodealAdTypeBanner// or AppodealAdTypeInterstitial or AppodealAdTypeRewardedVideo,
//        )
    }

    actual override suspend fun checkConsent() {
        // Shown automatically
    }

    actual override suspend fun canShowBanner(placementName: String?): Boolean {
        return Appodeal.canShow(
            type = AppodealAdTypeBanner,
            forPlacement = placementName.orEmpty(),
        )
    }

    actual override suspend fun canShowInterstitial(placementName: String?): Boolean {
        return Appodeal.canShow(
            type = AppodealAdTypeInterstitial,
            forPlacement = placementName.orEmpty(),
        )
    }

    actual override suspend fun canShowRewarded(placementName: String?): Boolean {
        return Appodeal.canShow(
            type = AppodealAdTypeRewardedVideo,
            forPlacement = placementName.orEmpty(),
        )
    }

    actual override suspend fun showBanner(placementName: String?): Boolean {
        // Do not show banner for now
        return false
    }

    actual override suspend fun showInterstitial(placementName: String?): Boolean {
        logger.d { "showInterstitial: placementName=$placementName" }

        return showAd(style = AppodealShowStyleInterstitial, placementName = placementName)
    }

    actual override suspend fun showRewarded(placementName: String?): Boolean {
        logger.d { "showRewarded: placementName=$placementName" }

        return showAd(style = AppodealShowStyleInterstitial, placementName = placementName)
    }

    actual override suspend fun hideBanner(): Boolean {
        // Not implemented for now
        return false
    }

    private fun showAd(style: AppodealShowStyle, placementName: String?): Boolean {
        val viewController = viewController?.get() ?: run {
            logger.w { "showAd: style=$style, placementName=$placementName: viewController == null" }
            return false
        }

        return Appodeal.showAd(
            style = style,
            forPlacement = placementName.orEmpty(),
            rootViewController = viewController,
        )
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
     * Banner delegate
     */
    private val bannerDelegate: AppodealBannerDelegateProtocol = object : NSObject(), AppodealBannerDelegateProtocol {

        override fun bannerDidClick() {
            logger.d { "bannerDidClick" }

            Analytics.logEvent(
                AnalyticsEvent(
                    name = "ad_banner_click",
                )
            )
        }

        override fun bannerDidExpired() {
            logger.d { "bannerDidExpired" }
        }

        override fun bannerDidFailToLoadAd() {
            logger.w { "bannerDidFailToLoadAd" }

            _isBannerAvailable.value = false
        }

        override fun bannerDidFailToPresentWithError(error: NSError) {
            logger.w { "bannerDidFailToLoadWithError: code=${error.code}, description=${error.description}" }
        }

        override fun bannerDidLoadAdIsPrecache(precache: Boolean) {
            logger.d { "bannerDidLoadAdIsPrecache: precache=$precache" }

            _isBannerAvailable.value = true
        }

        override fun bannerDidShow() {
            logger.d { "bannerDidShow" }
        }
    }

    /**
     * Interstitial delegate
     */
    private val interstitialDelegate: AppodealInterstitialDelegateProtocol =
        object : NSObject(), AppodealInterstitialDelegateProtocol {

            override fun interstitialDidClick() {
                logger.d { "interstitialDidClick" }

                Analytics.logEvent(
                    AnalyticsEvent(
                        name = "ad_interstitial_click",
                    )
                )
            }

            override fun interstitialDidDismiss() {
                logger.d { "interstitialDidDismiss" }
            }

            override fun interstitialDidExpired() {
                logger.d { "interstitialDidExpired" }
            }

            override fun interstitialDidFailToLoadAd() {
                logger.w { "interstitialDidFailToLoadAd" }

                _isInterstitialAvailable.value = false
            }

            override fun interstitialDidFailToPresent() {
                logger.w { "interstitialDidFailToPresent" }
            }

            override fun interstitialDidLoadAdIsPrecache(precache: Boolean) {
                logger.d { "interstitialDidLoadAdIsPrecache: precache=$precache" }

                _isInterstitialAvailable.value = true
            }

            override fun interstitialWillPresent() {
                logger.d { "interstitialWillPresent" }
            }
        }

    /**
     * Rewarded video delegate
     */
    private val rewardedVideoDelegate: AppodealRewardedVideoDelegateProtocol =
        object : NSObject(), AppodealRewardedVideoDelegateProtocol {

            override fun rewardedVideoDidClick() {
                logger.d { "rewardedVideoDidClick" }

                Analytics.logEvent(
                    AnalyticsEvent(
                        name = "ad_rewarded_video_click",
                    )
                )
            }

            override fun rewardedVideoDidExpired() {
                logger.d { "rewardedVideoDidExpired" }
            }

            override fun rewardedVideoDidFailToLoadAd() {
                logger.w { "rewardedVideoDidFailToLoadAd" }

                _isRewardedAvailable.value = true
            }

            override fun rewardedVideoDidFailToPresentWithError(error: NSError) {
                logger.e { "rewardedVideoDidFailToPresentWithError: code=${error.code}, description=${error.description}" }
            }

            override fun rewardedVideoDidFinish(rewardAmount: Float, name: String?) {
                logger.d { "rewardedVideoDidFinish: rewardAmount=$rewardAmount, name=$name" }

                Analytics.logEvent(
                    AnalyticsEvent(
                        name = "ad_rewarded_video_finished",
                        parameters = mapOf(
                            "amount" to rewardAmount,
                            "currency" to name,
                        )
                    )
                )
            }

            override fun rewardedVideoDidLoadAdIsPrecache(precache: Boolean) {
                logger.d { "rewardedVideoDidLoadAdIsPrecache: precache=$precache" }

                _isRewardedAvailable.value = true
            }

            override fun rewardedVideoDidPresent() {
                logger.d { "rewardedVideoDidPresent" }
            }

            override fun rewardedVideoWillDismissAndWasFullyWatched(wasFullyWatched: Boolean) {
                logger.d { "rewardedVideoWillDismissAndWasFullyWatched: wasFullyWatched=$wasFullyWatched" }

                coroutineScope.launch {
                    val result = RewardedVideoResult(
                        isFinished = wasFullyWatched,
                    )
                    _rewardedAdFinishedEvents.emit(result)
                }

                Analytics.logEvent(
                    AnalyticsEvent(
                        name = "ad_rewarded_video_close",
                        parameters = mapOf(
                            "is_finished" to wasFullyWatched,
                        )
                    )
                )
            }
        }

    /**
     * Ad revenue delegate
     */
    private val adRevenueDelegate: AppodealAdRevenueDelegateProtocol =
        object : NSObject(), AppodealAdRevenueDelegateProtocol {
            override fun didReceiveRevenueForAd(ad: AppodealAdRevenueProtocol) {
                logger.i {
                    "didReceiveRevenueForAd: network=${ad.networkName}, unit=${ad.adUnitName}, placement=${ad.placement}, " +
                            "precision=${ad.revenuePrecision}, demand=${ad.demandSource}, currency=${ad.currency}, " +
                            "revenue=${ad.revenue}, ad_type=${ad.adTypeString}"
                }

                Analytics.logAdRevenue(
                    format = ad.adTypeString,
                    source = ad.networkName,
                    adUnitName = ad.adUnitName,
                    amount = ad.revenue,
                    currency = ad.currency,
                )
            }
        }

    private companion object {
        private const val LOG_TAG = "AdsProvider"
    }
}