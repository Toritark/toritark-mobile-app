package com.toritark.app.presentation.billing.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction

import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions

@Composable
fun PaywallComponent(
    modifier: Modifier = Modifier,
    onPurchaseStarted: (rcPackage: Package) -> Unit,
    onPurchaseCompleted: (customerInfo: CustomerInfo, storeTransaction: StoreTransaction) -> Unit,
    onPurchaseError: (error: PurchasesError) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onRestoreStarted: () -> Unit,
    onRestoreCompleted: (customerInfo: CustomerInfo) -> Unit,
    onRestoreError: (error: PurchasesError) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val options = remember {
        val paywallListener = object : PaywallListener {
            override fun onPurchaseStarted(rcPackage: Package) {
                onPurchaseStarted(rcPackage)
            }

            override fun onPurchaseCompleted(
                customerInfo: CustomerInfo,
                storeTransaction: StoreTransaction,
            ) {
                onPurchaseCompleted(customerInfo, storeTransaction)
            }

            override fun onPurchaseError(error: PurchasesError) {
                onPurchaseError(error)
            }

            override fun onPurchaseCancelled() {
                onPurchaseCancelled()
            }

            override fun onRestoreStarted() {
                onRestoreStarted()
            }

            override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                onRestoreCompleted(customerInfo)
            }

            override fun onRestoreError(error: PurchasesError) {
                onRestoreError(error)
            }
        }

        PaywallOptions(
            dismissRequest = onDismissRequest,
        ) {
            shouldDisplayDismissButton = false
            listener = paywallListener
        }
    }

    Box(
        modifier = modifier,
    ) {
        Paywall(options)
    }
}