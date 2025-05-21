package com.toritark.app.presentation.billing.paywall

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.toritark.app.data.billing.api.model.PlanApiModel
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.billing.model.plan.PlanUpgradeCheckEvent
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.billing.paywall.model.PaywallScreenState
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal class PaywallViewModel(
    private val billingInteractor: BillingInteractor,
    private val profileInteractor: ProfileInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<PaywallScreenState>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = PaywallScreenState(),
) {
    override val logger = Logger.withTag(LOG_TAG)

    var paywallSource: PaywallSource = PaywallSource.Unknown

    private var initialPlan: PlanApiModel? = null

    init {
        initializeInitialPlan()
    }

    /**
     * Getting the initial plan before the purchase, because it may update earlier that we'll get it later
     */
    private fun initializeInitialPlan() {
        viewModelScope.launch {
            initialPlan = profileInteractor.getCurrentPlan()
        }
    }

    fun onCloseClick() {
        logger.d { "onCloseClick" }

        // TODO: Analytics

        onPopBackStack()
    }

    fun onPurchaseStarted(rcPackage: Package) {
        logger.d { "onPurchaseStarted: rcPackage=$rcPackage" }

        // TODO
    }

    fun onPurchaseCompleted(customerInfo: CustomerInfo, storeTransaction: StoreTransaction) {
        logger.d { "onPurchaseCompleted: customerInfo=$customerInfo, storeTransaction=$storeTransaction" }

        // TODO: Analytics

        waitForPlanToChange()
    }

    fun onPurchaseError(error: PurchasesError) {
        logger.w { "onPurchaseError: error=$error" }

        // TODO: Analytics

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Fail,
            )
        }
    }

    fun onPurchaseCancelled() {
        logger.d { "onPurchaseCancelled" }

        // TODO: Analytics
    }

    fun onRestoreStarted() {
        logger.d { "onRestoreStarted" }

        // TODO
    }

    fun onRestoreCompleted(customerInfo: CustomerInfo) {
        logger.d { "onRestoreCompleted: customerInfo=$customerInfo" }

        // TODO: Analytics

        waitForPlanToChange()
    }

    fun onRestoreError(error: PurchasesError) {
        logger.w { "onRestoreError: error=$error" }

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Fail,
            )
        }
    }


    private fun waitForPlanToChange() {
        logger.d { "waitForPlanToChange" }

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Checking.Normal,
            )
        }

        viewModelScope.launch {
            val startTimestamp = Clock.System.now().toEpochMilliseconds()

            billingInteractor
                .waitForPlanToUpgrade(initialPlan = initialPlan)
                .flowOn(defaultDispatcher)
                .collect { event ->
                    when (event) {
                        is PlanUpgradeCheckEvent.Success -> {
                            logger.i { "waitForPlanToChange: Success, plan=${event.newPlan}" }
                            updateAndShowContent {
                                copy(
                                    dialogState = PaywallScreenState.DialogState.Visible.Success,
                                )
                            }
                        }

                        is PlanUpgradeCheckEvent.Waiting -> {
                            logger.d { "waitForPlanToChange: Waiting, timeMs=${event.timeMs}" }
                            if (
                                event.timeMs > CHECK_TOO_LONG_TIME_MS &&
                                contentValue.dialogState is PaywallScreenState.DialogState.Visible.Checking.Normal
                            ) {
                                updateAndShowContent {
                                    copy(
                                        dialogState = PaywallScreenState.DialogState.Visible.Checking.TooLong,
                                    )
                                }
                            }
                        }
                    }
                }
        }
    }


    fun onResultDialogOkClick() {
        logger.d { "onResultDialogOkClick" }

        // TODO: Analytics
    }

    fun onResultDialogDismissed() {
        logger.d { "onResultDialogDismissed" }

        // TODO: Analytics

        if (contentValue.dialogState is PaywallScreenState.DialogState.Visible.Success) {
            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "PaywallViewModel"

        private const val CHECK_TOO_LONG_TIME_MS = 1_000L * 10
    }
}