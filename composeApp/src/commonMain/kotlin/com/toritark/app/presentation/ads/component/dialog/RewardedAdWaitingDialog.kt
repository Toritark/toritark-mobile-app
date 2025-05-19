@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.ads.component.dialog

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
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.learning_words.component.dialog.model.RewardedAdWaitingDialogState
import com.toritark.app.presentation.main.app.AppTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
fun RewardedAdWaitingDialog(
    modifier: Modifier = Modifier,
    state: RewardedAdWaitingDialogState,
    onOkClick: () -> Unit,
) {
    if (state == RewardedAdWaitingDialogState.None) return

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    when {
        !sheetState.isVisible -> {
            coroutineScope.launch { sheetState.show() }
        }
    }

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {},
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            when (state) {
                RewardedAdWaitingDialogState.Waiting -> WaitingDialogContent()
                RewardedAdWaitingDialogState.Fail -> FailDialogContent(onOkClick = onOkClick)
                RewardedAdWaitingDialogState.Success -> SuccessDialogContent(onOkClick = onOkClick)
                RewardedAdWaitingDialogState.None -> {}
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun WaitingDialogContent() {
    Text(
        text = stringResource(Res.string.title_monetization_watch_rewarded_ad_waiting),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(Res.string.desc_monetization_watch_rewarded_ad_waiting),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun FailDialogContent(
    onOkClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Text(
        text = stringResource(Res.string.title_monetization_watch_rewarded_ad_fail),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(Res.string.desc_monetization_watch_rewarded_ad_fail),
        style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
            onOkClick()
        }
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = stringResource(Res.string.title_monetization_watch_rewarded_ad_ok_btn),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = stringResource(Res.string.title_monetization_watch_rewarded_ad_ok_btn)
        )
    }
}

@Composable
private fun SuccessDialogContent(
    onOkClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Text(
        text = stringResource(Res.string.title_monetization_watch_rewarded_ad_success),
        style = MaterialTheme.typography.titleLarge,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Text(
        text = stringResource(Res.string.desc_monetization_watch_rewarded_ad_success),
        style = MaterialTheme.typography.bodyLarge,
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onTertiary,
        ),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
            onOkClick()
        }
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = stringResource(Res.string.title_monetization_watch_rewarded_ad_ok_btn),
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = stringResource(Res.string.title_monetization_watch_rewarded_ad_ok_btn)
        )
    }
}

@Preview
@Composable
private fun RewardedAdWaitingDialogWaitingPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            RewardedAdWaitingDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                state = RewardedAdWaitingDialogState.Waiting,
                onOkClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun RewardedAdWaitingDialogFailPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            RewardedAdWaitingDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                state = RewardedAdWaitingDialogState.Fail,
                onOkClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun RewardedAdWaitingDialogSuccessPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            RewardedAdWaitingDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                state = RewardedAdWaitingDialogState.Success,
                onOkClick = {},
            )
        }
    }
}