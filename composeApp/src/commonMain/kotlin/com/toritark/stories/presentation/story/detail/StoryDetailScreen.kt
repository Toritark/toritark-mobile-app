package com.toritark.stories.presentation.story.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.animation.FadeInAnimation
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.screen.ScreenState
import com.toritark.stories.presentation.story.detail.component.*
import com.toritark.stories.presentation.story.detail.component.generate.GenerateStoryHeader
import com.toritark.stories.presentation.story.detail.component.generate.StoryPrompt
import com.toritark.stories.presentation.story.detail.component.preview.StoryPreviewCard
import com.toritark.stories.presentation.story.detail.component.preview.StoryQuizPreviewCard
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun StoryDetailScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: StoryDetailViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

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
            // Generate story header
            GenerateStoryHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
                topics = storyTopics,
                selectedTopic = selectedStoryTopic,
                isPromptVisible = isPromptVisible,
                isGenerateButtonEnabled = isGenerateButtonEnabled,
                onTopicSelected = viewModel::onStoryTopicSelected,
                onCustomizeClick = viewModel::togglePromptVisibility,
                onGenerateClick = viewModel::onGenerateStoryClick,
            )
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

        // Story state
        when (screenState) {
            ScreenState.Content -> {
                story?.let {
                    item {
                        StoryContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            story = it,
                            onStoryClick = viewModel::onStoryClick,
                            onStoryQuizClick = viewModel::onStoryQuestionsClick,
                        )
                    }
                }
            }

            is ScreenState.Error -> {}
            ScreenState.Loading -> {
                item {
                    FadeInAnimation {
                        StoryCreationProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StoryContent(
    modifier: Modifier = Modifier,
    story: StoryApiModel,
    onStoryClick: () -> Unit,
    onStoryQuizClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        StoryPreviewCard(
            modifier = modifier
                .padding(top = 16.dp),
            story = story,
            onClick = onStoryClick,
        )

        StoryQuizPreviewCard(
            modifier = modifier
                .padding(top = 16.dp),
            story = story,
            onClick = onStoryQuizClick,
        )
    }
}

@Preview
@Composable
private fun StoryContentPreview() {
    val story = StoryApiModel(
        learningLanguageText = listOf(
            "Hello, my name is Toritark.",
            "I am a developer, writing code in Kotlin and Python.",
            "I like to code.",
            "I am coding in Kotlin.",
            "I am learning Jetpack Compose. This is a long, multi-line sentence",
            "One more sentence",
        ),
        nativeLanguageText = emptyList(),
        questions = emptyList(),
    )

    StoryContent(
        modifier = Modifier.fillMaxWidth(),
        story = story,
        onStoryClick = {},
        onStoryQuizClick = {},
    )
}