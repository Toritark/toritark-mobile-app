@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.billing.paywall

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.toritark.app.presentation.billing.component.PaywallComponent
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_paywall_screen

@Composable
internal fun PaywallScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: PaywallViewModel = koinViewModel(),
    paywallSource: PaywallSource,
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    viewModel.paywallSource = paywallSource

    BaseScreen(
        viewModel = viewModel,
    ) { screenState ->
        PaywallScreenContent(
            onPurchaseStarted = viewModel::onPurchaseStarted,
            onPurchaseCompleted = viewModel::onPurchaseCompleted,
            onPurchaseError = viewModel::onPurchaseError,
            onPurchaseCancelled = viewModel::onPurchaseCancelled,
            onRestoreStarted = viewModel::onRestoreStarted,
            onRestoreCompleted = viewModel::onRestoreCompleted,
            onRestoreError = viewModel::onRestoreError,
            onCloseClick = viewModel::onCloseClick,
        )
    }
}

@Composable
private fun PaywallScreenContent(
    onPurchaseStarted: (rcPackage: Package) -> Unit,
    onPurchaseCompleted: (customerInfo: CustomerInfo, storeTransaction: StoreTransaction) -> Unit,
    onPurchaseError: (error: PurchasesError) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onRestoreStarted: () -> Unit,
    onRestoreCompleted: (customerInfo: CustomerInfo) -> Unit,
    onRestoreError: (error: PurchasesError) -> Unit,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                title = {
                    Text(stringResource(Res.string.title_paywall_screen))
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            )
        },
    ) { innerPadding ->
        PaywallComponent(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onPurchaseStarted = onPurchaseStarted,
            onPurchaseCompleted = onPurchaseCompleted,
            onPurchaseError = onPurchaseError,
            onPurchaseCancelled = onPurchaseCancelled,
            onRestoreStarted = onRestoreStarted,
            onRestoreCompleted = onRestoreCompleted,
            onRestoreError = onRestoreError,
            onDismissRequest = onCloseClick,
        )
    }
}