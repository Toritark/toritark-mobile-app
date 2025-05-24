package com.toritark.app.presentation.billing.paywall

import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.toritark.app.data.analytics.Analytics
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
        logScreenView()
    }

    private fun logScreenView() {
        viewModelScope.launch {
            Analytics.logScreenView(SCREEN_NAME)

            Analytics.logEvent(
                eventName = "paywall_show",
                parameters = mapOf(
                    "source" to paywallSource.name,
                )
            )
        }
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

        Analytics.logEvent(
            eventName = "paywall_close_click",
            parameters = mapOf(
                "source" to paywallSource.name,
            )
        )

        onPopBackStack()
    }

    fun onPurchaseStarted(rcPackage: Package) {
        logger.d { "onPurchaseStarted: rcPackage=$rcPackage" }

        Analytics.logEvent(
            eventName = "paywall_purchase_start",
            parameters = mapOf(
                "source" to paywallSource.name,
                "package_identifier" to rcPackage.identifier,
                "package_type" to rcPackage.packageType.name,
                "store_product_id" to rcPackage.storeProduct.id,
                "store_product_type" to rcPackage.storeProduct.type.name,
                "store_product_price_formatted" to rcPackage.storeProduct.price.formatted,
                "store_product_price_amount" to rcPackage.storeProduct.price.amountMicros,
                "store_product_price_currency" to rcPackage.storeProduct.price.currencyCode,
                "store_product_title" to rcPackage.storeProduct.title,
                "offering_identifier" to rcPackage.presentedOfferingContext.offeringIdentifier,
                "offering_placement_identifier" to rcPackage.presentedOfferingContext.placementIdentifier,
            )
        )
    }

    fun onPurchaseCompleted(customerInfo: CustomerInfo, storeTransaction: StoreTransaction) {
        logger.d { "onPurchaseCompleted: customerInfo=$customerInfo, storeTransaction=$storeTransaction" }

        Analytics.logEvent(
            eventName = "paywall_purchase_completed",
            parameters = mapOf(
                "source" to paywallSource.name,
                "first_active_entitlement" to customerInfo.entitlements.active.keys.firstOrNull(),
                "first_active_subscription" to customerInfo.activeSubscriptions.firstOrNull(),
                "transaction_id" to storeTransaction.transactionId,
                "first_product_id" to storeTransaction.productIds.firstOrNull(),
            )
        )

        waitForPlanToChange()
    }

    fun onPurchaseError(error: PurchasesError) {
        logger.w { "onPurchaseError: error=$error" }

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Fail,
            )
        }

        Analytics.logEvent(
            eventName = "paywall_purchase_error",
            parameters = mapOf(
                "source" to paywallSource.name,
                "error_code" to error.code.code,
                "error_description" to error.code.description,
                "underlying_error_message" to error.underlyingErrorMessage,
            )
        )
    }

    fun onPurchaseCancelled() {
        logger.d { "onPurchaseCancelled" }

        Analytics.logEvent(
            eventName = "paywall_purchase_cancelled",
        )
    }

    fun onRestoreStarted() {
        logger.d { "onRestoreStarted" }

        Analytics.logEvent(
            eventName = "paywall_restore_started",
        )
    }

    fun onRestoreCompleted(customerInfo: CustomerInfo) {
        logger.d { "onRestoreCompleted: customerInfo=$customerInfo" }


        waitForPlanToChange()

        Analytics.logEvent(
            eventName = "paywall_restore_completed",
            parameters = mapOf(
                "source" to paywallSource.name,
                "first_active_entitlement" to customerInfo.entitlements.active.keys.firstOrNull(),
                "first_active_subscription" to customerInfo.activeSubscriptions.firstOrNull(),
            )
        )
    }

    fun onRestoreError(error: PurchasesError) {
        logger.w { "onRestoreError: error=$error" }

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Fail,
            )
        }

        Analytics.logEvent(
            eventName = "paywall_restore_error",
            parameters = mapOf(
                "source" to paywallSource.name,
                "error_code" to error.code.code,
                "error_description" to error.code.description,
                "underlying_error_message" to error.underlyingErrorMessage,
            )
        )
    }


    private fun waitForPlanToChange() {
        logger.d { "waitForPlanToChange" }

        updateAndShowContent {
            copy(
                dialogState = PaywallScreenState.DialogState.Visible.Checking.Normal,
            )
        }

        viewModelScope.launch {
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

                            Analytics.logEvent(
                                eventName = "paywall_plan_changed",
                                parameters = mapOf(
                                    "source" to paywallSource.name,
                                    "time_ms" to event.timeMs,
                                    "old_plan_name" to initialPlan?.name,
                                    "old_plan_is_free" to initialPlan?.isFree,
                                    "new_plan_name" to event.newPlan.name,
                                    "new_plan_is_free" to event.newPlan.isFree,
                                )
                            )
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

        Analytics.logEvent(
            eventName = "paywall_result_dialog_ok_click",
            parameters = mapOf(
                "source" to paywallSource.name,
            )
        )
    }

    fun onResultDialogDismissed() {
        logger.d { "onResultDialogDismissed" }

        Analytics.logEvent(
            eventName = "paywall_result_dialog_dismissed",
            parameters = mapOf(
                "source" to paywallSource.name,
            )
        )

        if (contentValue.dialogState is PaywallScreenState.DialogState.Visible.Success) {
            onPopBackStack()
        }
    }

    private companion object {
        private const val LOG_TAG = "PaywallViewModel"

        private const val SCREEN_NAME = "PaywallScreen"

        private const val CHECK_TOO_LONG_TIME_MS = 1_000L * 10
    }
}