package com.toritark.app.presentation.billing.paywall

import co.touchlab.kermit.Logger
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.screen.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher

internal class PaywallViewModel(
    private val billingInteractor: BillingInteractor,
    private val profileInteractor: ProfileInteractor,
    defaultDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    mainDispatcher: CoroutineDispatcher,
) : BaseViewModel<Unit>(
    defaultDispatcher = defaultDispatcher,
    ioDispatcher = ioDispatcher,
    mainDispatcher = mainDispatcher,
    defaultContentValue = Unit,
) {
    override val logger = Logger.withTag(LOG_TAG)

    var paywallSource: PaywallSource = PaywallSource.Unknown

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

        // TODO
    }

    fun onPurchaseError(error: PurchasesError) {
        logger.w { "onPurchaseError: error=$error" }

        // TODO
    }

    fun onPurchaseCancelled() {
        logger.d { "onPurchaseCancelled" }

        // TODO
    }

    fun onRestoreStarted() {
        logger.d { "onRestoreStarted" }

        // TODO
    }

    fun onRestoreCompleted(customerInfo: CustomerInfo) {
        logger.d { "onRestoreCompleted: customerInfo=$customerInfo" }

        // TODO
    }

    fun onRestoreError(error: PurchasesError) {
        logger.w { "onRestoreError: error=$error" }

        // TODO
    }

    private companion object {
        private const val LOG_TAG = "PaywallViewModel"
    }
}