package com.toritark.stories.presentation.story.retelling

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.icon.AppIcons
import com.toritark.stories.presentation.core_ui.icon.MagicChange
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.story.retelling.model.StoryRetellingScreenContent
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
    story: StoryApiModel,
    lazyListState: LazyListState? = null,
) {
    viewModel.setStory(story)

    BaseScreen(viewModel) { contentValue ->
        StoryRetellingContent(
            modifier = modifier,
            screenContent = contentValue,
            onTextInputChange = viewModel::onTextChange,
            onSubmitClick = viewModel::onSubmitButtonClick,
            lazyListState = lazyListState,
        )
    }
}

@Composable
private fun StoryRetellingContent(
    modifier: Modifier = Modifier,
    screenContent: StoryRetellingScreenContent,
    onTextInputChange: (text: String) -> Unit,
    onSubmitClick: () -> Unit,
    lazyListState: LazyListState? = null,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 8.dp, vertical = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
        ) {
            var inputText by remember { mutableStateOf(screenContent.retellingText) }
            var isFocused by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }

            val isLoading = screenContent.checkResult is StoryRetellingScreenContent.CheckResult.Checking

            CardHeader()

            Spacer(modifier = Modifier.height(16.dp))

            LaunchedEffect(isFocused, inputText) {
                if (lazyListState != null && (isFocused || inputText != screenContent.retellingText)) {
                    // Scroll to ensure the text field and button are visible
                    // Using a higher index with additional offset to ensure both text field and button are visible
                    lazyListState.animateScrollToItem(
                        index = 3,
                        scrollOffset = -200 // Negative offset to scroll further down
                    )
                }
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged { focusState ->
                        isFocused = focusState.isFocused
                    },
                value = inputText,
                onValueChange = { newText ->
                    inputText = newText
                    if (newText != screenContent.retellingText) {
                        onTextInputChange(newText)
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

            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onSubmitClick,
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

//    Spacer(modifier = Modifier.height(12.dp))
//
//    Text(
//        text = stringResource(Res.string.desc_story_retelling_card),
//        style = MaterialTheme.typography.bodyLarge,
//        color = MaterialTheme.colorScheme.onSurfaceVariant,
//    )
}

@Preview
@Composable
private fun StoryRetellingContentPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            StoryRetellingContent(
                modifier = Modifier,
                screenContent = StoryRetellingScreenContent(
                    retellingText = "Hello, World! Testing multi-line text.\nLong, long text.\n".repeat(3),
                    isSubmitButtonEnabled = true,
                ),
                onTextInputChange = {},
                onSubmitClick = {},
            )
        }
    }
}
