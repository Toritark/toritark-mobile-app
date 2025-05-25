package com.toritark.app.domain.billing.interactor

import co.touchlab.kermit.Logger
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.data.billing.api.repository.BillingApiRepository
import com.toritark.app.data.billing.repository.BillingSettingsRepository
import com.toritark.app.domain.billing.model.plan.PlanUpgradeCheckEvent
import com.toritark.app.domain.billing.provider.BillingProvider
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.datetime.Clock

interface BillingInteractor {
    fun initialize()

    fun waitForPlanToUpgrade(initialPlan: PlanApiModel?): Flow<PlanUpgradeCheckEvent>
    suspend fun triggerPlanCheck()

    suspend fun shouldShowPaywall(): Boolean
}

internal class BillingInteractorImpl(
    private val billingApiRepository: BillingApiRepository,
    private val billingSettingsRepository: BillingSettingsRepository,
    private val billingProvider: BillingProvider,
    private val profileInteractor: ProfileInteractor,
    private val defaultDispatcher: CoroutineDispatcher,
) : BillingInteractor {

    private val logger = Logger.withTag(LOG_TAG)

    private val coroutineScope = CoroutineScope(defaultDispatcher + SupervisorJob())

    override fun initialize() {
        logger.d { "initialize" }

        coroutineScope.launch {
            profileInteractor
                .presentProfileState
                .map { profileState -> profileState.profile }
                .collect { profile ->
                    logger.d { "initialize: profile=$profile" }

                    billingProvider.initialize(userId = profile.id)
                }
        }
    }

    /**
     * A bit hacky approach, will find a better one later
     */
    override fun waitForPlanToUpgrade(initialPlan: PlanApiModel?): Flow<PlanUpgradeCheckEvent> {
        logger.d { "waitForPlanToUpgrade" }

        return flow {
            val initialPlan = initialPlan ?: profileInteractor.getCurrentPlan()

            logger.d { "waitForPlanToUpgrade: initialPlan=$initialPlan" }

            val startTimestamp = Clock.System.now().toEpochMilliseconds()
            var lastCheckTriggerTimestamp = 0L

            while (currentCoroutineContext().isActive) {
                withContext(defaultDispatcher) {
                    delay(PLAN_UPGRADE_CHECK_INTERVAL_MS)
                }

                try {
                    profileInteractor.updateProfile().collect()
                } catch (t: Throwable) {
                    // Catch & mute all exceptions here as they are not important
                    logger.w(t) { "waitForPlanToChange: Failed to update profile" }
                }
                val currentPlan = profileInteractor.getCurrentPlan()
                logger.d { "waitForPlanToChange: currentPlan=$currentPlan" }

                val currentTimestamp = Clock.System.now().toEpochMilliseconds()

                if (!currentPlan.isFree && currentPlan.id != initialPlan.id) {
                    logger.i { "waitForPlanToChange: plan changed to $currentPlan" }
                    emit(
                        PlanUpgradeCheckEvent.Success(
                            timeMs = currentTimestamp - startTimestamp,
                            newPlan = currentPlan,
                        )
                    )
                    break
                }

                emit(PlanUpgradeCheckEvent.Waiting(timeMs = currentTimestamp - startTimestamp))

                if (currentTimestamp - startTimestamp >= PLAN_UPGRADE_CHECK_TRIGGER_CHECK_MIN_TIME_MS) {
                    if (currentTimestamp - lastCheckTriggerTimestamp >= PLAN_UPGRADE_CHECK_TRIGGER_CHECK_INTERVAL_MS) {
                        logger.i { "waitForPlanToChange: trigger check" }
                        lastCheckTriggerTimestamp = currentTimestamp
                        triggerPlanCheck()
                        logger.i { "waitForPlanToChange: check triggered" }
                    }
                }
            }
        }
    }

    override suspend fun triggerPlanCheck() {
        try {
            billingApiRepository.triggerPlanCheck()
        } catch (t: Throwable) {
            // Catch & mute all exceptions here as they are not important
            logger.w(t) { "triggerPlanCheck: Failed to trigger plan check" }
        }
    }

    override suspend fun shouldShowPaywall(): Boolean {
        return withContext(defaultDispatcher) {
            if (!profileInteractor.presentProfileState.first().profile.plan.isFree) {
                return@withContext false
            }

            val lastPaywallShowAt = billingSettingsRepository.getLastPaywallShowAt()
            val now = Clock.System.now().toEpochMilliseconds()

            if (now - lastPaywallShowAt > SHOW_PAYWALL_INTERVAL_MS) {
                billingSettingsRepository.setLastPaywallShowNow()

                return@withContext true
            }

            return@withContext false
        }
    }

    private companion object {
        private const val LOG_TAG = "BillingInteractor"

        private const val PLAN_UPGRADE_CHECK_INTERVAL_MS = 1_000L
        private const val PLAN_UPGRADE_CHECK_TRIGGER_CHECK_MIN_TIME_MS = 1_000L * 15
        private const val PLAN_UPGRADE_CHECK_TRIGGER_CHECK_INTERVAL_MS = 1_000L * 15

        private const val SHOW_PAYWALL_INTERVAL_MS = 1_000L * 60 * 60 * 24 * 3 // 3 days
    }
}