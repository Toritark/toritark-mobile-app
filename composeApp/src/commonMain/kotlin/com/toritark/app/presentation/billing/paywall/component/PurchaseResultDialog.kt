@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.billing.paywall.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.billing.paywall.model.PaywallScreenState
import com.toritark.app.presentation.main.app.theme.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun PurchaseResultDialog(
    modifier: Modifier,
    sheetState: SheetState,
    state: PaywallScreenState.DialogState.Visible,
    onDismissRequest: () -> Unit,
    onOkClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            when (state) {
                is PaywallScreenState.DialogState.Visible.Checking -> {}
                else -> onDismissRequest()
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = state !is PaywallScreenState.DialogState.Visible.Checking,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            when (state) {
                PaywallScreenState.DialogState.Visible.Checking.Normal -> CheckingNormalContent()
                PaywallScreenState.DialogState.Visible.Checking.TooLong -> CheckingTooLongContent()
                PaywallScreenState.DialogState.Visible.Fail -> FailContent(
                    onOkClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                        onOkClick()
                    }
                )

                PaywallScreenState.DialogState.Visible.Success -> SuccessContent(
                    onOkClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                        onOkClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CheckingNormalContent() {
    BaseDialogContent(
        title = stringResource(Res.string.title_paywall_result_dialog_checking_normal),
        text = stringResource(Res.string.desc_paywall_result_dialog_checking_normal),
    )
}

@Composable
private fun CheckingTooLongContent() {
    BaseDialogContent(
        title = stringResource(Res.string.title_paywall_result_dialog_checking_too_long),
        text = stringResource(Res.string.desc_paywall_result_dialog_checking_too_long),
    )
}

@Composable
private fun FailContent(
    onOkClick: () -> Unit,
) {
    BaseDialogContent(
        title = stringResource(Res.string.title_paywall_result_dialog_fail),
        text = stringResource(Res.string.desc_paywall_result_dialog_fail),
    )

    Spacer(modifier = Modifier.height(24.dp))

    OkButton(
        onClick = onOkClick,
    )
}

@Composable
private fun SuccessContent(
    onOkClick: () -> Unit,
) {
    BaseDialogContent(
        title = stringResource(Res.string.title_paywall_result_dialog_success),
        text = stringResource(Res.string.desc_paywall_result_dialog_success),
    )

    Spacer(modifier = Modifier.height(24.dp))

    OkButton(
        onClick = onOkClick,
    )
}

@Composable
private fun BaseDialogContent(
    title: String,
    text: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
    )

    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
private fun OkButton(
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = stringResource(Res.string.title_paywall_result_dialog_ok_btn)
        )
    }
}

@Preview
@Composable
private fun PurchaseResultDialogCheckingNormalPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            PurchaseResultDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                sheetState = sheetState,
                state = PaywallScreenState.DialogState.Visible.Checking.Normal,
                onDismissRequest = {},
                onOkClick = {},
            )
        }
    }

    coroutineScope.launch {
        sheetState.show()
    }
}

@Preview
@Composable
private fun PurchaseResultDialogCheckingTooLongPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            PurchaseResultDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                sheetState = sheetState,
                state = PaywallScreenState.DialogState.Visible.Checking.TooLong,
                onDismissRequest = {},
                onOkClick = {},
            )
        }
    }

    coroutineScope.launch {
        sheetState.show()
    }
}

@Preview
@Composable
private fun PurchaseResultDialogFailPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            PurchaseResultDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                sheetState = sheetState,
                state = PaywallScreenState.DialogState.Visible.Fail,
                onDismissRequest = {},
                onOkClick = {},
            )
        }
    }

    coroutineScope.launch {
        sheetState.show()
    }
}

@Preview
@Composable
private fun PurchaseResultDialogSuccessPreview() {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            PurchaseResultDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                sheetState = sheetState,
                state = PaywallScreenState.DialogState.Visible.Success,
                onDismissRequest = {},
                onOkClick = {},
            )
        }
    }

    coroutineScope.launch {
        sheetState.show()
    }
}