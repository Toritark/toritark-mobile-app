@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.billing.paywall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.toritark.app.presentation.billing.paywall.component.PaywallComponent
import com.toritark.app.presentation.billing.paywall.component.PurchaseResultDialog
import com.toritark.app.presentation.billing.paywall.model.PaywallScreenState
import com.toritark.app.presentation.billing.paywall.model.PaywallSource
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_paywall_screen

@Composable
internal fun PaywallScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    paywallSource: PaywallSource,
    viewModel: PaywallViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    viewModel.paywallSource = paywallSource

    BaseScreen(
        viewModel = viewModel,
    ) { screenState ->
        PaywallScreenContent(
            screenState = screenState,
            onPurchaseStarted = viewModel::onPurchaseStarted,
            onPurchaseCompleted = viewModel::onPurchaseCompleted,
            onPurchaseError = viewModel::onPurchaseError,
            onPurchaseCancelled = viewModel::onPurchaseCancelled,
            onRestoreStarted = viewModel::onRestoreStarted,
            onRestoreCompleted = viewModel::onRestoreCompleted,
            onRestoreError = viewModel::onRestoreError,
            onCloseClick = viewModel::onCloseClick,
            onResultDialogDismissed = viewModel::onResultDialogDismissed,
            onResultDialogOkClick = viewModel::onResultDialogOkClick,
        )
    }
}


@Composable
private fun PaywallScreenContent(
    screenState: PaywallScreenState,
    onPurchaseStarted: (rcPackage: Package) -> Unit,
    onPurchaseCompleted: (customerInfo: CustomerInfo, storeTransaction: StoreTransaction) -> Unit,
    onPurchaseError: (error: PurchasesError) -> Unit,
    onPurchaseCancelled: () -> Unit,
    onRestoreStarted: () -> Unit,
    onRestoreCompleted: (customerInfo: CustomerInfo) -> Unit,
    onRestoreError: (error: PurchasesError) -> Unit,
    onCloseClick: () -> Unit,
    onResultDialogDismissed: () -> Unit,
    onResultDialogOkClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val resultSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newState ->
            when (screenState.dialogState) {
                is PaywallScreenState.DialogState.Visible.Checking -> false
                else -> true
            }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
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
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding),
        ) {
            PaywallComponent(
                modifier = Modifier
                    .fillMaxSize(),
                onPurchaseStarted = onPurchaseStarted,
                onPurchaseCompleted = onPurchaseCompleted,
                onPurchaseError = onPurchaseError,
                onPurchaseCancelled = onPurchaseCancelled,
                onRestoreStarted = onRestoreStarted,
                onRestoreCompleted = onRestoreCompleted,
                onRestoreError = onRestoreError,
                onDismissRequest = {}, // We're checking the plan state later, so don't auto-close
            )

            when (val dialogState = screenState.dialogState) {
                PaywallScreenState.DialogState.Hidden -> {
                    // FIXME: Calls to launch should happen inside a LaunchedEffect and not composition
                    coroutineScope.launch {
                        if (resultSheetState.isVisible) {
                            resultSheetState.hide()
                        }
                    }
                }

                is PaywallScreenState.DialogState.Visible -> {
                    PurchaseResultDialog(
                        modifier = Modifier
                            .fillMaxWidth(),
                        sheetState = resultSheetState,
                        state = dialogState,
                        onDismissRequest = {
                            coroutineScope.launch {
                                resultSheetState.hide()
                                onResultDialogDismissed()
                            }
                        },
                        onOkClick = {
                            coroutineScope.launch {
                                resultSheetState.hide()
                                onResultDialogOkClick()
                                onResultDialogDismissed()
                            }
                        }
                    )

                    // FIXME: Calls to launch should happen inside a LaunchedEffect and not composition
                    coroutineScope.launch {
                        if (!resultSheetState.isVisible) {
                            resultSheetState.show()
                        }
                    }
                }
            }
        }
    }
}