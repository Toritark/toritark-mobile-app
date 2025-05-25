@file:OptIn(ExperimentalMaterial3Api::class)

package com.toritark.app.presentation.story.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.app.data.story.model.story.story.StoryApiModel
import com.toritark.app.data.story.model.story.story.StoryQuestionAnswerApiModel
import com.toritark.app.data.story.model.story.story.StoryQuestionApiModel
import com.toritark.app.presentation.ads.banner.BannerContainer
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.story.quiz.component.StoryQuiz
import com.toritark.app.presentation.story.quiz.model.QuizAnswerState
import com.toritark.app.presentation.story.quiz.model.QuizQuestionState
import com.toritark.app.presentation.story.quiz.model.QuizState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_quiz_screen

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
            BannerContainer(
                modifier = Modifier.padding(bottom = 24.dp),
                onBannerViewReady = viewModel::onBannerViewReady,
            ) {
                StoryQuizScreenContent(
                    quizState = quizState,
                    onCloseClick = viewModel::onCloseClick,
                    onAnswerSelected = viewModel::onAnswerSelected,
                    onNextClick = viewModel::onNextClick,
                )
            }
        }
    }
}

@Composable
private fun StoryQuizScreenContent(
    modifier: Modifier = Modifier,
    quizState: QuizState,
    onCloseClick: () -> Unit,
    onAnswerSelected: (answer: StoryQuestionAnswerApiModel) -> Unit,
    onNextClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(stringResource(Res.string.title_story_quiz_screen))
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
            modifier = modifier
                .background(color = MaterialTheme.colorScheme.surface)
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            quizState = quizState,
            onAnswerSelected = onAnswerSelected,
            onNextClick = onNextClick,
        )
    }
}

@Preview
@Composable
private fun StoryQuizScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            StoryQuizScreenContent(
                modifier = Modifier
                    .fillMaxWidth(),
                quizState = QuizState(
                    questions = listOf(
                        StoryQuestionApiModel(
                            question = "Hello, World! Testing multi-line text.\nLong, long text.",
                            answers = listOf(
                                StoryQuestionAnswerApiModel(
                                    answer = "Option 1",
                                    isCorrect = false,
                                ),
                                StoryQuestionAnswerApiModel(
                                    answer = "Option 2",
                                    isCorrect = true,
                                ),
                                StoryQuestionAnswerApiModel(
                                    answer = "Option 3. This should be multi-line text. Long, long text that does not fit the screen.",
                                    isCorrect = false,
                                ),
                                StoryQuestionAnswerApiModel(
                                    answer = "Option 4",
                                    isCorrect = false,
                                ),
                            ),
                        )
                    ),
                    questionsStates = listOf(
                        QuizQuestionState.WRONG,
                        QuizQuestionState.CORRECT,
                        QuizQuestionState.CURRENT,
                        QuizQuestionState.NONE,
                    ),
                    currentQuestionIndex = 0,
                    answersStates = listOf(
                        QuizAnswerState.NONE,
                        QuizAnswerState.CORRECT,
                        QuizAnswerState.WRONG,
                        QuizAnswerState.NONE,
                    ),
                    isLastQuestion = false,
                    correctAnswers = 1,
                    wrongAnswers = 1,
                ),
                onCloseClick = {},
                onAnswerSelected = {},
                onNextClick = {},
            )
        }
    }
}