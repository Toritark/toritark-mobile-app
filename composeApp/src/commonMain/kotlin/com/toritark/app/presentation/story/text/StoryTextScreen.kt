@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.presentation.ads.banner.BannerContainer
import com.toritark.app.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.app.presentation.core_ui.clipboard.clipEntryOf
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.story.detail.component.SelectWordsHint
import com.toritark.app.presentation.story.detail.component.SelectedStoryWordsHeader
import com.toritark.app.presentation.story.detail.component.StoryText
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_selected_words_empty
import toritark.composeapp.generated.resources.title_story_text_screen

private val ignoredWords = setOf(".", ",", "!", "?", ":", ";")

@Composable
internal fun StoryTextScreen(
    story: StoryApiModel,
    viewModel: StoryTextViewModel = koinViewModel(),
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    viewModel.setStory(story)
    viewModel.onPopBackStack = onPopBackStack
    viewModel.onNavigateTo = onNavigateTo

    val clipboard = LocalClipboard.current
    val coroutineScope = rememberCoroutineScope()

    BaseScreen(viewModel) { contentValue ->
        contentValue.story?.let { story ->
            BannerContainer(
                onBannerViewReady = viewModel::onBannerViewReady,
            ) {
                StoryTextScreenContent(
                    story = story,
                    onCloseClick = viewModel::onCloseClick,
                    onCopyClick = {
                        coroutineScope.launch {
                            clipboard.setClipEntry(clipEntryOf(story.learningLanguageText.joinToString("\n")))
                        }
                    },
                    onAddWordsToLearningSetClick = viewModel::onAddWordsToLearningSetClick,
                )
            }
        }
    }
}

@Composable
private fun StoryTextScreenContent(
    story: StoryApiModel,
    onCloseClick: () -> Unit,
    onCopyClick: () -> Unit,
    onAddWordsToLearningSetClick: (words: Set<String>) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(stringResource(Res.string.title_story_text_screen))
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
        ) {
            // TODO: Store in ViewModel?
            var selectedWords by remember { mutableStateOf(setOf<String>()) }

            FadeAndExpandVerticallyAnimation(visible = selectedWords.isEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                SelectWordsHint(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            FadeAndExpandVerticallyAnimation(visible = selectedWords.isNotEmpty()) {
                Column {
                    Spacer(modifier = Modifier.height(4.dp))

                    SelectedStoryWordsHeader(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        words = selectedWords,
                        onClearClick = {
                            selectedWords = emptySet()
                        },
                        onAddAllClick = {
                            onAddWordsToLearningSetClick(selectedWords)
                            selectedWords = emptySet()
                        },
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            StoryText(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                learningLanguageText = story.learningLanguageText,
                nativeLanguageText = story.nativeLanguageText,
                selectedWords = selectedWords,
                onWordSelected = { word ->
                    if (word in ignoredWords) return@StoryText

                    selectedWords = if (selectedWords.contains(word)) {
                        selectedWords - word
                    } else {
                        selectedWords + word
                    }
                },
                onCopyClick = onCopyClick,
            )
        }
    }
}

@Preview
@Composable
private fun StoryTextScreenPreview() {
    val story = StoryApiModel(
        learningLanguageText = listOf(
            "Hello, my name is Toritark.",
            "I am a developer.",
            "I am a passionate programmer.",
            "I love to code.",
            "I am currently working with Compose Multiplatform. This is a long, multi-line sentence",
            "I am currently working with Jetpack Compose.",
            "I am currently working with Kotlin Multiplatform.",
            "I am currently working with Jetpack Compose.",
            "I am currently working with KMM.",
        ),
        nativeLanguageText = listOf(
            "Привет, меня зовут Toritark.",
            "Я являюсь разработчиком.",
            "Я люблю программировать.",
            "I love to code.",
            "Это длинное-длинное предложение на русском языке для теста мульти-лайна",
            "I am currently working with Jetpack Compose.",
            "I am currently working with Kotlin Multiplatform.",
            "I am currently working with Jetpack Compose.",
            "I am currently working with KMM.",
        ),
        questions = emptyList(),
    )

    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 500.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            StoryTextScreenContent(
                story = story,
                onCloseClick = {},
                onAddWordsToLearningSetClick = {},
                onCopyClick = {},
            )
        }
    }
}