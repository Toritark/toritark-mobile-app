package com.toritark.stories.presentation.story.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.story.detail.component.StoryCreationProgressIndicator
import com.toritark.stories.presentation.story.detail.component.generate.GenerateStoryHeader
import com.toritark.stories.presentation.story.detail.component.generate.StoryPrompt
import com.toritark.stories.presentation.story.detail.component.preview.StoryPreviewCard
import com.toritark.stories.presentation.story.detail.component.preview.StoryQuizPreviewCard
import com.toritark.stories.presentation.story.detail.model.StoryDetailScreenContent
import com.toritark.stories.presentation.story.retelling.StoryRetellingSection
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun StoryDetailScreen(
    onNavigateTo: OnNavigateTo,
    viewModel: StoryDetailViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo

    val lazyListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Hide the keyboard when scrolling
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemIndex }
            .collect {
                keyboardController?.hide()
            }
    }

    BaseScreen(
        viewModel = viewModel,
    ) { contentValue ->

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
                    topics = contentValue.topics,
                    selectedTopic = contentValue.selectedTopic,
                    isPromptVisible = contentValue.isPromptInputVisible,
                    isGenerateButtonEnabled = contentValue.isGenerateButtonEnabled,
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
                    prompt = contentValue.promptText,
                    isExpanded = contentValue.isPromptInputVisible,
                    onPromptChange = viewModel::onPromptChange,
                )
            }

            // Story state
            when (val storyState = contentValue.storyState) {
                is StoryDetailScreenContent.StoryState.Created -> {
                    item {
                        StoryContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            story = storyState.story,
                            onStoryClick = viewModel::onStoryClick,
                            onStoryQuizClick = viewModel::onStoryQuestionsClick,
                            lazyListState = lazyListState,
                        )
                    }
                }

                StoryDetailScreenContent.StoryState.Creating -> {
                    item {
                        StoryCreationProgressIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 32.dp),
                        )
                    }
                }

                StoryDetailScreenContent.StoryState.Empty -> {}
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
    lazyListState: LazyListState? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        StoryPreviewCard(
            modifier = modifier,
            story = story,
            onClick = onStoryClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoryQuizPreviewCard(
            modifier = modifier,
            story = story,
            onClick = onStoryQuizClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        StoryRetellingSection(
            modifier = modifier,
            story = story,
            lazyListState = lazyListState,
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
