@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.quiz

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.data.story.model.story.story.StoryQuestionAnswerApiModel
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.story.quiz.component.StoryQuiz
import com.toritark.app.presentation.story.quiz.model.QuizState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_text_screen

@Composable
internal fun StoryQuizScreen(
    story: StoryApiModel,
    viewModel: StoryQuizViewModel = koinViewModel(),
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
) {
    viewModel.setStory(story)
    viewModel.onPopBackStack = onPopBackStack
    viewModel.onNavigateTo = onNavigateTo

    BaseScreen(viewModel) { contentValue ->
        contentValue.quizState?.let { quizState ->
            StoryQuizScreenContent(
                quizState = quizState,
                onCloseClick = viewModel::onCloseClick,
                onAnswerSelected = viewModel::onAnswerSelected,
                onNextClick = viewModel::onNextClick,
            )
        }
    }
}

@Composable
private fun StoryQuizScreenContent(
    quizState: QuizState,
    onCloseClick: () -> Unit,
    onAnswerSelected: (answer: StoryQuestionAnswerApiModel) -> Unit,
    onNextClick: () -> Unit,
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
        StoryQuiz(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            quizState = quizState,
            onAnswerSelected = onAnswerSelected,
            onNextClick = onNextClick,
        )
    }
}