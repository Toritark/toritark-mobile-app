package com.toritark.app.presentation.story.detail

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
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.ads.banner.BannerContainer
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.story.detail.component.StoryCreationProgressIndicator
import com.toritark.app.presentation.story.detail.component.generate.GenerateStoryHeader
import com.toritark.app.presentation.story.detail.component.generate.StoryPrompt
import com.toritark.app.presentation.story.detail.component.preview.StoryPreviewCard
import com.toritark.app.presentation.story.detail.component.preview.StoryQuizPreviewCard
import com.toritark.app.presentation.story.detail.model.StoryDetailScreenState
import com.toritark.app.presentation.story.model.StoryTopicUiModel
import com.toritark.app.presentation.story.retelling.section.StoryRetellingSection
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun StoryDetailScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
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

        BannerContainer(
            onBannerViewReady = viewModel::onBannerViewReady,
        ) {
            StoryScreenContent(
                modifier = Modifier
                    .fillMaxSize(),
                screenState = contentValue,
                lazyListState = lazyListState,
                onTopicSelected = viewModel::onStoryTopicSelected,
                onCustomizeClick = viewModel::togglePromptVisibility,
                onPromptChange = viewModel::onPromptChange,
                onGenerateClick = viewModel::onGenerateStoryClick,
                onStoryClick = viewModel::onStoryClick,
                onStoryQuizClick = viewModel::onStoryQuestionsClick,
                onNavigateTo = onNavigateTo,
                onPopBackStack = onPopBackStack,
            )
        }

    }
}

@Composable
private fun StoryScreenContent(
    modifier: Modifier = Modifier,
    screenState: StoryDetailScreenState,
    lazyListState: LazyListState,
    onTopicSelected: (StoryTopicUiModel) -> Unit,
    onCustomizeClick: () -> Unit,
    onPromptChange: (String) -> Unit = {},
    onGenerateClick: () -> Unit,
    onStoryClick: () -> Unit,
    onStoryQuizClick: () -> Unit,
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
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
                topics = screenState.topics,
                selectedTopic = screenState.selectedTopic,
                isPromptVisible = screenState.isPromptInputVisible,
                isGenerateButtonEnabled = screenState.isGenerateButtonEnabled,
                onTopicSelected = onTopicSelected,
                onCustomizeClick = onCustomizeClick,
                onGenerateClick = onGenerateClick,
            )
        }

        item {
            // Prompt input
            StoryPrompt(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp),
                prompt = screenState.promptText,
                isExpanded = screenState.isPromptInputVisible,
                onPromptChange = onPromptChange,
            )
        }

        // Story state
        when (val storyState = screenState.storyState) {
            is StoryDetailScreenState.StoryState.Created -> {
                item {
                    StoryContent(
                        modifier = Modifier
                            .fillMaxWidth(),
                        storyRequestId = storyState.storyRequestId,
                        story = storyState.story,
                        onStoryClick = onStoryClick,
                        onStoryQuizClick = onStoryQuizClick,
                        onNavigateTo = onNavigateTo,
                        onPopBackStack = onPopBackStack,
                    )
                }
            }

            StoryDetailScreenState.StoryState.Creating -> {
                item {
                    StoryCreationProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                    )
                }
            }

            StoryDetailScreenState.StoryState.Empty -> {}
        }
    }
}

@Composable
private fun StoryContent(
    modifier: Modifier = Modifier,
    storyRequestId: Long,
    story: StoryApiModel,
    onStoryClick: () -> Unit,
    onStoryQuizClick: () -> Unit,
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
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
            storyRequestId = storyRequestId,
            story = story,
            onNavigateTo = onNavigateTo,
            onPopBackStack = onPopBackStack,
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
        storyRequestId = 0L,
        story = story,
        onStoryClick = {},
        onStoryQuizClick = {},
        onNavigateTo = { _, _ -> },
        onPopBackStack = {},
    )
}
