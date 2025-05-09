package com.toritark.stories.presentation.story.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.icon.AppIcons
import com.toritark.stories.presentation.core_ui.icon.Customize
import com.toritark.stories.presentation.core_ui.icon.MagicWand
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.screen.ScreenState
import com.toritark.stories.presentation.story.detail.component.StoryPrompt
import com.toritark.stories.presentation.story.detail.component.StoryText
import com.toritark.stories.presentation.story.detail.component.StoryTopicChooser
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_generate_btn

@Composable
internal fun StoryDetailScreen(
    onNavigate: OnNavigateTo,
    viewModel: StoryDetailViewModel = koinViewModel(),
) {
    viewModel.onNavigate = onNavigate

    val screenState by viewModel.screenState.collectAsState()

    val storyTopics by viewModel.storyTopics.collectAsState()
    val selectedStoryTopic by viewModel.selectedStoryTopic.collectAsState()
    val prompt by viewModel.prompt.collectAsState()
    val isPromptVisible by viewModel.isPromptVisible.collectAsState()
    val isGenerateButtonEnabled by viewModel.isGenerateButtonEnabled.collectAsState()

    val story by viewModel.story.collectAsState()

    val lazyListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Hide the keyboard when scrolling
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect {
                keyboardController?.hide()
            }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Topic chooser
                StoryTopicChooser(
                    modifier = Modifier
                        .weight(1f),
                    topics = storyTopics,
                    selectedTopic = selectedStoryTopic,
                    onTopicSelected = viewModel::onStoryTopicSelected,
                )

                // Customize topic prompt
                IconButton(
                    onClick = { viewModel.togglePromptVisibility() }
                ) {
                    Icon(
                        imageVector = AppIcons.Customize,
                        contentDescription = null,
                        tint = if (isPromptVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        item {
            // Prompt input
            StoryPrompt(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                prompt = prompt,
                isExpanded = isPromptVisible,
                onPromptChange = viewModel::onPromptChange,
            )
        }

        item {
            // Generate button
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                onClick = { viewModel.onGenerateStoryClick() },
                enabled = isGenerateButtonEnabled && screenState != ScreenState.Loading,
            ) {
                Text(
                    text = stringResource(Res.string.title_story_generate_btn),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                Icon(
                    imageVector = AppIcons.MagicWand,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(16.dp)
                )
            }
        }

        // Story state
        when (screenState) {
            ScreenState.Content -> {
                story?.let {
                    item {
                        StoryContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            story = it,
                        )
                    }
                }
            }

            is ScreenState.Error -> {}
            ScreenState.Loading -> {
                item {
                    StoryLoading()
                }
            }
        }
    }
}

@Composable
private fun StoryContent(
    modifier: Modifier = Modifier,
    story: StoryApiModel,
) {
    StoryText(
        modifier = modifier
            .padding(top = 16.dp),
        learningLanguageText = story.learningLanguageText,
        nativeLanguageText = story.nativeLanguageText,
    )
}

@Composable
private fun StoryLoading() {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/lottie/magic.json").decodeToString()
        )
    }

    Image(
        modifier = Modifier.fillMaxWidth(),
        painter = rememberLottiePainter(
            composition = composition,
            iterations = Compottie.IterateForever,
        ),
        contentDescription = null,
    )
}
