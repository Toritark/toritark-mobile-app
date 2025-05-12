package com.toritark.stories.presentation.story.quiz.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.toritark.stories.data.story.model.story.story.StoryQuestionAnswerApiModel
import com.toritark.stories.data.story.model.story.story.StoryQuestionApiModel
import com.toritark.stories.presentation.story.quiz.model.QuizAnswerState
import com.toritark.stories.presentation.story.quiz.model.QuizQuestionState
import com.toritark.stories.presentation.story.quiz.model.QuizState
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun StoryQuiz(
    modifier: Modifier,
    quizState: QuizState,
    onAnswerSelected: (answer: StoryQuestionAnswerApiModel) -> Unit,
    onNextClick: () -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        QuizStepper(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            states = quizState.questionsStates,
            stripHeight = 8.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        QuizQuestion(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            question = quizState.questions[quizState.currentQuestionIndex],
            answersStates = quizState.answersStates,
            isLastQuestion = quizState.isLastQuestion,
            onAnswerSelected = onAnswerSelected,
            onNextClick = onNextClick,
        )
    }
}

@Preview
@Composable
private fun StoryQuizPreview() {
    Box(
        modifier = Modifier
            .size(width = 400.dp, height = 600.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        StoryQuiz(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
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
            onAnswerSelected = {},
            onNextClick = {},
        )
    }
}
