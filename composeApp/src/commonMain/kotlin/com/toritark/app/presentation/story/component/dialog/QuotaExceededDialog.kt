@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Upgrade
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.app.theme.LocalExtendedColors
import com.toritark.app.presentation.story.model.QuotaExceededMessage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_monetization_upgrade_plan_btn
import toritark.composeapp.generated.resources.title_monetization_watch_rewarded_ad_btn

@Composable
internal fun QuotaExceededDialog(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    message: QuotaExceededMessage,
    onDismissRequest: () -> Unit,
    onWatchAdClick: () -> Unit,
    onUpgradePlanClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp,
        sheetState = sheetState,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = true,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = message.title,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp),
                text = message.text,
                style = MaterialTheme.typography.bodyLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (message.canShowRewardedAd) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                    ),
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                        onWatchAdClick()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoLibrary,
                        contentDescription = stringResource(Res.string.title_monetization_watch_rewarded_ad_btn),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier
                            .padding(vertical = 8.dp),
                        text = stringResource(Res.string.title_monetization_watch_rewarded_ad_btn)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalExtendedColors.current.success.success,
                    contentColor = LocalExtendedColors.current.success.onSuccess,
                ),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    onUpgradePlanClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Upgrade,
                    contentDescription = stringResource(Res.string.title_monetization_upgrade_plan_btn),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = stringResource(Res.string.title_monetization_upgrade_plan_btn)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
internal fun QuotaExceededDialogPreview() {
    val quotaExceededSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            QuotaExceededDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                message = QuotaExceededMessage(
                    title = "Oops, story limit exceeded",
                    text = "You can create 1 story per day on your plan (Free).\nPlease watch a short ad to create a new story, or upgrade your plan to get even more advantages.",
                    canShowRewardedAd = true,
                ),
                sheetState = quotaExceededSheetState,
                onDismissRequest = {},
                onWatchAdClick = {},
                onUpgradePlanClick = {},
            )
        }
    }

    coroutineScope.launch {
        quotaExceededSheetState.show()
    }
}

@Preview
@Composable
internal fun QuotaExceededDialogDarkPreview() {
    val quotaExceededSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    val coroutineScope = rememberCoroutineScope()

    AppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            QuotaExceededDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                message = QuotaExceededMessage(
                    title = "Oops, story limit exceeded",
                    text = "You can create 1 story per day on your plan (Free).\nPlease watch a short ad to create a new story, or upgrade your plan to get even more advantages.",
                    canShowRewardedAd = true,
                ),
                sheetState = quotaExceededSheetState,
                onDismissRequest = {},
                onWatchAdClick = {},
                onUpgradePlanClick = {},
            )
        }
    }

    coroutineScope.launch {
        quotaExceededSheetState.show()
    }
}