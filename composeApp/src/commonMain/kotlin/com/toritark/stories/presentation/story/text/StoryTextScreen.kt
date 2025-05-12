@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.stories.presentation.story.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.story.StoryApiModel
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.story.detail.component.StoryText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_text_screen

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

    BaseScreen(viewModel) { contentValue ->
        contentValue.story?.let { story ->
            StoryTextScreenContent(
                story = story,
                onCloseClick = viewModel::onCloseClick,
            )
        }
    }
}

@Composable
private fun StoryTextScreenContent(
    story: StoryApiModel,
    onCloseClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
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
        StoryText(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            learningLanguageText = story.learningLanguageText,
            nativeLanguageText = story.nativeLanguageText,
        )
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

    Box(
        modifier = Modifier.size(width = 400.dp, height = 500.dp)
    ) {
        StoryTextScreenContent(
            story = story,
            onCloseClick = {},
        )
    }
}