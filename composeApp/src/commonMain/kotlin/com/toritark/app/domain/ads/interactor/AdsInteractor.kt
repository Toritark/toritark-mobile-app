package com.toritark.app.domain.ads.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.api.repository.AdsApiRepository
import com.toritark.app.data.ads.model.placement.AdPlacement
import com.toritark.app.data.ads.model.rewarded.RewardedVideoKind
import com.toritark.app.data.profile.model.ProfileState
import com.toritark.app.data.profile.model.ProfileSubscriptionState
import com.toritark.app.domain.ads.exception.FailedToShowRewardedAdException
import com.toritark.app.domain.ads.exception.NoRewardedAdException
import com.toritark.app.domain.ads.exception.RewardedAdNoBonusAddedException
import com.toritark.app.domain.ads.exception.RewardedAdNotFinishedException
import com.toritark.app.domain.ads.provider.AdsProvider
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.util.core.extension.flow.unitFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock

interface AdsInteractor {
    fun initialize()

    suspend fun canShowRewardedAd(adPlacement: AdPlacement): Boolean

    fun showBannerAd(adPlacement: AdPlacement)
    fun showInterstitialAd(adPlacement: AdPlacement)
    fun showRewardedAd(rewardedVideoKind: RewardedVideoKind): Flow<Unit>
}

internal class AdsInteractorImpl(
    private val adsProvider: AdsProvider,
    private val adsApiRepository: AdsApiRepository,
    private val profileInteractor: ProfileInteractor,
    private val defaultDispatcher: CoroutineDispatcher,
) : AdsInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    init {
        listenToSubscriptionState()
    }

    private fun listenToSubscriptionState() {
        coroutineScope.launch {
            profileInteractor
                .subscriptionState
                .collect { state ->
                    logger.d { "listenToSubscriptionState: state=$state" }

                    if (state == ProfileSubscriptionState.Paid) {
                        logger.d { "Hiding banner because of paid subscription" }
                        adsProvider.hideBanner()
                    }
                }
        }
    }

    override fun initialize() {
        logger.d { "initialize" }

        coroutineScope.launch {
            profileInteractor
                .profileState
                .filterIsInstance<ProfileState.Present>()
                .map { profileState -> profileState.profile }
                .collect { profile ->
                    logger.d { "initialize: profile=$profile" }

                    adsProvider.initialize(
                        userId = profile.id,
                    )

                    logger.d { "initialize: Initialized for userId=${profile.id}" }
                }
        }
    }

    override suspend fun canShowRewardedAd(adPlacement: AdPlacement): Boolean {
        return adsProvider.isRewardedAvailable.value && adsProvider.canShowRewarded(
            placementName = adPlacement.placementName,
        )
    }

    override fun showBannerAd(adPlacement: AdPlacement) {
        coroutineScope.launch {
            if (areAdsDisabled) {
                logger.d { "showBannerAd: adPlacement=$adPlacement, ads disabled" }
                return@launch
            }

            waitForSubscriptionType()

            if (isPaidSubscription) {
                logger.d { "showBannerAd: adPlacement=$adPlacement, paid subscription" }
                return@launch
            }

            adsProvider.showBanner(adPlacement.placementName)
        }
    }

    /**
     * In case if the subscription type is not loaded yet - wait for it
     */
    private suspend fun waitForSubscriptionType() {
        if (profileInteractor.subscriptionState.value != ProfileSubscriptionState.Unknown) {
            return
        }

        logger.d { "waitForSubscriptionType: Waiting for subscription type" }

        profileInteractor
            .subscriptionState
            .filterNot { it == ProfileSubscriptionState.Unknown }
            .first()
    }

    override fun showRewardedAd(rewardedVideoKind: RewardedVideoKind): Flow<Unit> {
        logger.d { "showRewardedAd: rewardedVideoKind=$rewardedVideoKind" }

        return unitFlow {
            val adPlacement = when (rewardedVideoKind) {
                RewardedVideoKind.Generation -> AdPlacement.Rewarded.Generation
                RewardedVideoKind.RetellingCheck -> AdPlacement.Rewarded.RetellingCheck
            }

            if (!adsProvider.canShowRewarded(adPlacement.placementName)) {
                throw NoRewardedAdException()
            }

            if (!adsProvider.showRewarded(adPlacement.placementName)) {
                throw FailedToShowRewardedAdException()
            }

            logger.d { "showRewardedAd: shown, adPlacement=$adPlacement" }

            // Wait for the ad to finish
            val result = adsProvider.rewardedAdFinishedEvents.first()

            logger.d { "showRewardedAd: finished, adPlacement=$adPlacement, result=$result" }

            if (!result.isFinished) {
                throw RewardedAdNotFinishedException()
            }

            // TODO: Start checking on the backend
            val startTime = Clock.System.now().toEpochMilliseconds()

            while (Clock.System.now().toEpochMilliseconds() - startTime <= MAX_REWARDED_AD_BONUS_CHECK_TIME_MS) {
                try {
                    val bonusesCount = adsApiRepository.getNotConsumedRewardedAdBonusesCount()
                    if (bonusesCount.count > 0) {
                        logger.d { "showRewardedAd: Bonus added, bonusesCount=${bonusesCount.count}" }

                        return@unitFlow
                    }
                } catch (t: Throwable) {
                    logger.w(t) { "showRewardedAd: Failed to check rewarded ad bonus" }
                }

                delay(REWARDED_AD_BONUS_CHECK_INTERVAL_MS)
            }

            throw RewardedAdNoBonusAddedException()
        }
    }

    override fun showInterstitialAd(adPlacement: AdPlacement) {
        coroutineScope.launch {
            if (areAdsDisabled) {
                logger.d { "showInterstitialAd: adPlacement=$adPlacement, ads disabled" }
                return@launch
            }

            waitForSubscriptionType()

            if (isPaidSubscription) {
                logger.d { "showInterstitialAd: adPlacement=$adPlacement, paid subscription" }
                return@launch
            }

            if (adsProvider.canShowInterstitial(adPlacement.placementName)) {
                adsProvider.showInterstitial(adPlacement.placementName)
            }
        }
    }

    private val isPaidSubscription: Boolean
        get() = profileInteractor.subscriptionState.value == ProfileSubscriptionState.Paid

    private val areAdsDisabled: Boolean
        get() = ADS_DISABLED

    private companion object {
        private const val LOG_TAG = "AdsInteractor"

        private const val ADS_DISABLED = false

        private const val MAX_REWARDED_AD_BONUS_CHECK_TIME_MS = 1000L * 60 * 2
        private const val REWARDED_AD_BONUS_CHECK_INTERVAL_MS = 300L
    }
}