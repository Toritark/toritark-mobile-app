@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.retelling.section

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingReviewApiModel
import com.toritark.app.data.story.model.retelling.retelling.StoryRetellingScoresApiModel
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.ads.component.dialog.RewardedAdWaitingDialog
import com.toritark.app.presentation.core_ui.animation.FadeInAnimation
import com.toritark.app.presentation.core_ui.icon.AppIcons
import com.toritark.app.presentation.core_ui.icon.MagicChange
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.story.component.dialog.QuotaExceededDialog
import com.toritark.app.presentation.story.model.QuotaExceededMessage
import com.toritark.app.presentation.story.retelling.component.StoryRetellingReviewInProgress
import com.toritark.app.presentation.story.retelling.component.StoryRetellingReviewSummary
import com.toritark.app.presentation.story.retelling.section.model.StoryRetellingScreenState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.desc_story_retelling_card
import toritark.composeapp.generated.resources.title_story_retelling_card
import toritark.composeapp.generated.resources.title_story_retelling_check_btn

@Composable
internal fun StoryRetellingSection(
    modifier: Modifier,
    viewModel: StoryRetellingViewModel = koinViewModel(),
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    storyRequestId: Long,
    story: StoryApiModel,
) {
    viewModel.setStory(storyRequestId = storyRequestId, story = story)
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    val coroutineScope = rememberCoroutineScope()

    val quotaExceededSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    var quotaExceededMessage by remember { mutableStateOf<QuotaExceededMessage?>(null) }

    LaunchedEffect(viewModel) {
        viewModel.showRetellingCheckQuotaExceededDialog.collect { message ->
            quotaExceededMessage = message

            coroutineScope.launch {
                quotaExceededSheetState.show()
            }
        }
    }

    BaseScreen(viewModel) { contentValue ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            StoryRetellingContent(
                modifier = modifier,
                screenContent = contentValue,
                onTextInputChange = viewModel::onTextChange,
                onSubmitClick = viewModel::onSubmitButtonClick,
                onDetailsClick = viewModel::onDetailsClick,
            )

            quotaExceededMessage?.let { message ->
                fun dismissDialog() {
                    coroutineScope.launch {
                        quotaExceededMessage = null
                        quotaExceededSheetState.hide()
                    }
                }

                QuotaExceededDialog(
                    modifier = Modifier
                        .fillMaxWidth(),
                    sheetState = quotaExceededSheetState,
                    message = message,
                    onDismissRequest = {
                        viewModel.onQuotaExceededDialogClosed()
                        dismissDialog()
                    },
                    onWatchAdClick = {
                        viewModel.onWatchAdToUnlockClick()
                        dismissDialog()
                    },
                    onUpgradePlanClick = {
                        viewModel.onUpgradePlanToUnlockClick()
                        dismissDialog()
                    },
                )
            }

            RewardedAdWaitingDialog(
                modifier = Modifier
                    .fillMaxWidth(),
                state = contentValue.rewardedAdWaitingDialogState,
                onOkClick = viewModel::onRewardedAdWaitingDialogOkClick,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun StoryRetellingContent(
    modifier: Modifier = Modifier,
    screenContent: StoryRetellingScreenState,
    onTextInputChange: (text: String) -> Unit,
    onSubmitClick: () -> Unit,
    onDetailsClick: () -> Unit,
) {
    val contentBringIntoViewRequester = remember { BringIntoViewRequester() }
    val textFieldBringIntoViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()

    val shape = RoundedCornerShape(24.dp)

    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = shape,
            )
            .clip(shape)
            .background(color = MaterialTheme.colorScheme.background)
            .clickable(onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

                if (screenContent.reviewResult is StoryRetellingScreenState.ReviewResult.Ready) {
                    onDetailsClick()
                }
            })
            .padding(horizontal = 8.dp, vertical = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .bringIntoViewRequester(contentBringIntoViewRequester)
                .imePadding(),
        ) {

            // Scroll when the review appears
            LaunchedEffect(screenContent.reviewResult) {
                if (screenContent.reviewResult is StoryRetellingScreenState.ReviewResult.InProgress ||
                    screenContent.reviewResult is StoryRetellingScreenState.ReviewResult.Ready
                ) {
                    contentBringIntoViewRequester.bringIntoView()
                }
            }

            when (val reviewResult = screenContent.reviewResult) {
                is StoryRetellingScreenState.ReviewResult.None -> {
                    RetellingForm(
                        screenContent = screenContent,
                        onTextInputChange = onTextInputChange,
                        onSubmitClick = onSubmitClick,
                        textFieldBringIntoViewRequester = textFieldBringIntoViewRequester,
                        coroutineScope = coroutineScope
                    )
                }

                is StoryRetellingScreenState.ReviewResult.InProgress -> {
                    Spacer(modifier = Modifier.height(32.dp))

                    FadeInAnimation {
                        StoryRetellingReviewInProgress()
                    }
                }

                is StoryRetellingScreenState.ReviewResult.Ready -> {
                    FadeInAnimation {
                        StoryRetellingReviewSummary(
                            modifier = Modifier.fillMaxWidth(),
                            review = reviewResult.result,
                            onDetailsClick = onDetailsClick,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CardHeader() {
    Row {
        Icon(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .size(24.dp),
            imageVector = AppIcons.MagicChange,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Text(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 8.dp),
            text = stringResource(Res.string.title_story_retelling_card),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RetellingForm(
    screenContent: StoryRetellingScreenState,
    onTextInputChange: (text: String) -> Unit,
    onSubmitClick: () -> Unit,
    textFieldBringIntoViewRequester: BringIntoViewRequester,
    coroutineScope: CoroutineScope,
) {
    var inputText by remember { mutableStateOf(screenContent.retellingText) }
    val focusRequester = remember { FocusRequester() }

    val isLoading = screenContent.reviewResult is StoryRetellingScreenState.ReviewResult.InProgress

    CardHeader()

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    coroutineScope.launch {
                        textFieldBringIntoViewRequester.bringIntoView()
                    }
                }
            },
        value = inputText,
        onValueChange = { newText ->
            inputText = newText
            if (newText != screenContent.retellingText) {
                onTextInputChange(newText)
            }
            coroutineScope.launch {
                textFieldBringIntoViewRequester.bringIntoView()
            }
        },
        readOnly = isLoading,
        minLines = 2,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences
        ),
        placeholder = {
            Text(
                text = stringResource(Res.string.desc_story_retelling_card),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            )
        }
    )

    Spacer(modifier = Modifier.height(16.dp))

    val hapticFeedback = LocalHapticFeedback.current

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .bringIntoViewRequester(textFieldBringIntoViewRequester),
        onClick = {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
            onSubmitClick()
        },
        enabled = screenContent.isSubmitButtonEnabled && !isLoading,
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = stringResource(Res.string.title_story_retelling_check_btn),
        )
    }
}


@Preview
@Composable
private fun StoryRetellingContentPreviewNormal() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun StoryRetellingContentPreviewInProgress() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                    reviewResult = StoryRetellingScreenState.ReviewResult.InProgress,
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun StoryRetellingContentPreviewReady() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                    reviewResult = StoryRetellingScreenState.ReviewResult.Ready(
                        result = StoryRetellingReviewApiModel(
                            overallReview = "This is a very good retelling. I like it very much.\nSome mistakes were made, but they are not significant.",
                            scores = StoryRetellingScoresApiModel(
                                overall = 90,
                                completeness = 80,
                                grammar = 50,
                                vocabulary = 30,
                                spelling = 20,
                                punctuation = 0,
                            ),
                            sentences = emptyList(),
                        ),
                    ),
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}

// Dark previews

@Preview
@Composable
private fun StoryRetellingContentDarkPreviewNormal() {
    AppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun StoryRetellingContentDarkPreviewInProgress() {
    AppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                    reviewResult = StoryRetellingScreenState.ReviewResult.InProgress,
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun StoryRetellingContentDarkPreviewReady() {
    AppTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenState(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                    reviewResult = StoryRetellingScreenState.ReviewResult.Ready(
                        result = StoryRetellingReviewApiModel(
                            overallReview = "This is a very good retelling. I like it very much.\nSome mistakes were made, but they are not significant.",
                            scores = StoryRetellingScoresApiModel(
                                overall = 90,
                                completeness = 80,
                                grammar = 50,
                                vocabulary = 30,
                                spelling = 20,
                                punctuation = 0,
                            ),
                            sentences = emptyList(),
                        ),
                    ),
                ),
                onTextInputChange = {},
                onSubmitClick = {},
                onDetailsClick = {},
            )
        }
    }
}