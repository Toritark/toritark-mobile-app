@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.learning_words.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreTime
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
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_learning_words_learned_dialog
import toritark.composeapp.generated.resources.title_learning_words_learned_dialog_learned_btn
import toritark.composeapp.generated.resources.title_learning_words_learned_dialog_not_learned_btn

@Composable
internal fun LearnedSentenceDialog(
    modifier: Modifier = Modifier,
    visible: Boolean,
    onLearnedClick: () -> Unit,
    onNotLearnedClick: () -> Unit,
) {
    if (!visible) return

    val hapticFeedback = LocalHapticFeedback.current

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    when {
        visible && !sheetState.isVisible -> {
            coroutineScope.launch { sheetState.show() }
        }

        !visible && sheetState.isVisible -> {
            coroutineScope.launch { sheetState.hide() }
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
            Text(
                text = stringResource(Res.string.title_learning_words_learned_dialog),
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalExtendedColors.current.success.successContainer,
                    contentColor = LocalExtendedColors.current.success.onSuccessContainer,
                ),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    onLearnedClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = stringResource(Res.string.title_learning_words_learned_dialog_learned_btn),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = stringResource(Res.string.title_learning_words_learned_dialog_learned_btn)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    onNotLearnedClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreTime,
                    contentDescription = stringResource(Res.string.title_learning_words_learned_dialog_not_learned_btn),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = stringResource(Res.string.title_learning_words_learned_dialog_not_learned_btn)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}

@Preview
@Composable
internal fun LearnedSentenceDialogPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            LearnedSentenceDialog(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                visible = true,
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}