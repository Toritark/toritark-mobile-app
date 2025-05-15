package com.toritark.stories.presentation.learning_words.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.learning_words.data.model.LearningStats
import com.toritark.stories.presentation.core_ui.animation.FadeInAnimation
import com.toritark.stories.presentation.core_ui.component.FlatCard
import com.toritark.stories.presentation.core_ui.icon.AppIcons
import com.toritark.stories.presentation.core_ui.icon.Magic
import com.toritark.stories.presentation.core_ui.nav.OnNavigateTo
import com.toritark.stories.presentation.core_ui.nav.OnPopBackStack
import com.toritark.stories.presentation.core_ui.screen.BaseScreen
import com.toritark.stories.presentation.learning_words.component.dialog.LearnedSentenceDialog
import com.toritark.stories.presentation.learning_words.component.sentence.LearningSentence
import com.toritark.stories.presentation.learning_words.component.stats.LearningStatsBlock
import com.toritark.stories.presentation.learning_words.main.model.LearningWordsMainScreenContent
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.stories.presentation.main.app.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_learning_words_no_words
import toritark.composeapp.generated.resources.title_learning_words_no_words_create_btn

@Composable
internal fun LearningWordsMainScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: LearningWordsMainViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    BaseScreen(
        viewModel = viewModel,
    ) { contentValue ->
        ScreenContent(
            modifier = Modifier.fillMaxSize(),
            contentValue = contentValue,
            onGoToStoryScreenClick = viewModel::onGoToStoryScreenClick,
            onInputChange = viewModel::onInputChange,
            onNextClick = viewModel::onNextClick,
            onLearnedClick = viewModel::onLearnedClick,
            onNotLearnedClick = viewModel::onNotLearnedClick,
        )
    }

}

@Composable
private fun ScreenContent(
    modifier: Modifier = Modifier,
    contentValue: LearningWordsMainScreenContent,
    onGoToStoryScreenClick: () -> Unit,
    onInputChange: (partIndex: Int, text: String) -> Unit,
    onNextClick: () -> Unit,
    onLearnedClick: () -> Unit,
    onNotLearnedClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            when (val currentSentence = contentValue.currentSentence) {
                is LearningWordsMainScreenContent.CurrentSentence.Loading -> {
                    FadeInAnimation { LoadingScreen() }
                }

                is LearningWordsMainScreenContent.CurrentSentence.Empty -> {
                    FadeInAnimation { EmptyScreen(onGoToStoryScreenClick = onGoToStoryScreenClick) }
                }

                is LearningWordsMainScreenContent.CurrentSentence.Present -> {
                    FadeInAnimation {
                        PresentScreen(
                            currentSentence = currentSentence,
                            onInputChange = onInputChange,
                            onNextClick = onNextClick,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val learningStatsState = contentValue.learningStatsState) {
                is LearningWordsMainScreenContent.LearningStatsState.Present -> {
                    Spacer(modifier = Modifier.height(16.dp))

                    LearningStatsBlock(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        learningStats = learningStatsState.stats,
                    )
                }

                else -> {}
            }
        }

        LearnedSentenceDialog(
            modifier = Modifier
                .fillMaxWidth(),
            visible = contentValue.showLearnedDialog,
            onLearnedClick = onLearnedClick,
            onNotLearnedClick = onNotLearnedClick,
        )
    }

}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 64.dp),
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.TopCenter),
        )
    }
}

@Composable
private fun EmptyScreen(
    onGoToStoryScreenClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 64.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(Res.string.title_learning_words_no_words),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            onClick = onGoToStoryScreenClick,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(Res.string.title_learning_words_no_words_create_btn)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = AppIcons.Magic,
                contentDescription = stringResource(Res.string.title_learning_words_no_words_create_btn),
            )
        }
    }
}

@Composable
private fun PresentScreen(
    currentSentence: LearningWordsMainScreenContent.CurrentSentence.Present,
    onInputChange: (partIndex: Int, text: String) -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        FlatCard(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.background,
            cornerRadius = 24.dp,
            borderWidth = 1.dp,
            borderColor = MaterialTheme.colorScheme.secondary,
        ) {
            LearningSentence(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                currentSentence = currentSentence,
                onInputChange = onInputChange,
                onNextClick = onNextClick,
            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentLoadingPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                contentValue = LearningWordsMainScreenContent(
                    currentSentence = LearningWordsMainScreenContent.CurrentSentence.Loading,
                ),
                onGoToStoryScreenClick = {},
                onInputChange = { _, _ -> },
                onNextClick = {},
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentEmptyPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                contentValue = LearningWordsMainScreenContent(
                    currentSentence = LearningWordsMainScreenContent.CurrentSentence.Empty,
                ),
                onGoToStoryScreenClick = {},
                onInputChange = { _, _ -> },
                onNextClick = {},
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentPresentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                contentValue = LearningWordsMainScreenContent(
                    currentSentence = LearningWordsMainScreenContent.CurrentSentence.Present.Todo(
                        sentence = SentenceWithParts(
                            id = 1,
                            parts = listOf(
                                SentencePart.Text("Hello, "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.EMPTY,
                                    correctText = "this",
                                ),
                                SentencePart.Text(" naturally "),
                                SentencePart.Text(" "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.EMPTY,
                                    currentText = "ve",
                                    correctText = "very",
                                ),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.INCORRECT,
                                    currentText = "butiffull",
                                    correctText = "beautiful",
                                ),
                                SentencePart.Text(" "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.CORRECT,
                                    currentText = "world",
                                    correctText = "World",
                                ),
                                SentencePart.Text("!")
                            ),
                            nativeLanguageText = "Привет, прекрасный мир!"
                        ),
                    ),
                    learningStatsState = LearningWordsMainScreenContent.LearningStatsState.Present(
                        stats = LearningStats(
                            words = LearningStats.Words(
                                total = 110,
                                learned = 50,
                                toLearn = 60,
                            )
                        ),
                    )
                ),
                onGoToStoryScreenClick = {},
                onInputChange = { _, _ -> },
                onNextClick = {},
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentPresentCompletePreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                contentValue = LearningWordsMainScreenContent(
                    currentSentence = LearningWordsMainScreenContent.CurrentSentence.Present.Todo(
                        sentence = SentenceWithParts(
                            id = 1,
                            parts = listOf(
                                SentencePart.Text("Hello, "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.INCORRECT,
                                    correctText = "this",
                                ),
                                SentencePart.Text(" naturally "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.CORRECT,
                                    currentText = "very",
                                    correctText = "very",
                                ),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.INCORRECT,
                                    currentText = "butiffull",
                                    correctText = "beautiful",
                                ),
                                SentencePart.Text(" "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.CORRECT,
                                    currentText = "world",
                                    correctText = "World",
                                ),
                                SentencePart.Text("!")
                            ),
                            nativeLanguageText = "Привет, прекрасный мир!"
                        ),
                    ),
                ),
                onGoToStoryScreenClick = {},
                onInputChange = { _, _ -> },
                onNextClick = {},
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun ScreenContentPresentWithLearnedDialogPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 800.dp)
                .background(color = MaterialTheme.colorScheme.surface),
        ) {
            ScreenContent(
                modifier = Modifier.fillMaxSize(),
                contentValue = LearningWordsMainScreenContent(
                    currentSentence = LearningWordsMainScreenContent.CurrentSentence.Present.Todo(
                        sentence = SentenceWithParts(
                            id = 1,
                            parts = listOf(
                                SentencePart.Text("Hello, "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.INCORRECT,
                                    correctText = "this",
                                ),
                                SentencePart.Text(" naturally "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.CORRECT,
                                    currentText = "very",
                                    correctText = "very",
                                ),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.INCORRECT,
                                    currentText = "butiffull",
                                    correctText = "beautiful",
                                ),
                                SentencePart.Text(" "),
                                SentencePart.Input(
                                    state = SentencePart.Input.State.CORRECT,
                                    currentText = "world",
                                    correctText = "World",
                                ),
                                SentencePart.Text("!")
                            ),
                            nativeLanguageText = "Привет, прекрасный мир!"
                        ),
                    ),
                    showLearnedDialog = true,
                ),
                onGoToStoryScreenClick = {},
                onInputChange = { _, _ -> },
                onNextClick = {},
                onLearnedClick = {},
                onNotLearnedClick = {},
            )
        }
    }
}