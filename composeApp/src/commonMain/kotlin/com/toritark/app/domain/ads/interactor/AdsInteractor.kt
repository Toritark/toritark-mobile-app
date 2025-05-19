package com.toritark.app.domain.ads.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.ads.model.AdPlacement
import com.toritark.app.data.profile.model.ProfileSubscriptionState
import com.toritark.app.domain.ads.provider.AdsProvider
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

interface AdsInteractor {
    fun initialize()

    fun showBannerAd(adPlacement: AdPlacement)
}

internal class AdsInteractorImpl(
    private val adsProvider: AdsProvider,
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
        coroutineScope.launch {
            adsProvider.initialize()
        }
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

    private val isPaidSubscription: Boolean
        get() = profileInteractor.subscriptionState.value == ProfileSubscriptionState.Paid

    private val areAdsDisabled: Boolean
        get() = ADS_DISABLED

    private companion object {
        private const val LOG_TAG = "AdsInteractor"

        private const val ADS_DISABLED = false
    }
}