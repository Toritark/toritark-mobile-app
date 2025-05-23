package com.toritark.app.presentation.story.quiz.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.toritark.app.data.story.model.story.story.StoryQuestionAnswerApiModel
import com.toritark.app.data.story.model.story.story.StoryQuestionApiModel
import com.toritark.app.presentation.core_ui.animation.FadeInAnimation
import com.toritark.app.presentation.main.app.theme.AppTheme
import com.toritark.app.presentation.main.app.theme.LocalExtendedColors
import com.toritark.app.presentation.story.quiz.model.QuizAnswerState
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_story_quiz_question_finish_btn
import toritark.composeapp.generated.resources.title_story_quiz_question_next_btn

private const val LOG_TAG = "QuizQuestion"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun QuizQuestion(
    modifier: Modifier = Modifier,
    question: StoryQuestionApiModel,
    answersStates: List<QuizAnswerState>,
    isLastQuestion: Boolean,
    onAnswerSelected: (answer: StoryQuestionAnswerApiModel) -> Unit,
    onNextClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current
    val isAnswerSelected = answersStates.any { it != QuizAnswerState.NONE }

    Column(
        modifier = modifier,
    ) {

        // Question
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            text = question.question,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Answers
        question.answers.forEachIndexed { index, answer ->
            QuizAnswer(
                modifier = Modifier.fillMaxWidth(),
                enabled = !isAnswerSelected,
                answer = answer,
                state = answersStates[index],
                onClick = {
                    onAnswerSelected(answer)

                    if (answer.isCorrect) {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                    } else {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Reject)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Button
        FadeInAnimation(visible = isAnswerSelected) {
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                    onNextClick()
                },
            ) {
                Text(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    text = if (isLastQuestion) {
                        stringResource(Res.string.title_story_quiz_question_finish_btn)
                    } else {
                        stringResource(Res.string.title_story_quiz_question_next_btn)
                    },
                )

                Icon(
                    modifier = Modifier
                        .padding(start = 8.dp),
                    imageVector = if (isLastQuestion) {
                        Icons.Default.Check
                    } else {
                        Icons.AutoMirrored.Default.ArrowForward
                    },
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
private fun QuizAnswer(
    modifier: Modifier = Modifier,
    enabled: Boolean,
    answer: StoryQuestionAnswerApiModel,
    state: QuizAnswerState,
    onClick: () -> Unit,
) {
    val textColor = when (state) {
        QuizAnswerState.NONE -> MaterialTheme.colorScheme.onBackground
        QuizAnswerState.CORRECT -> LocalExtendedColors.current.success.success
        QuizAnswerState.WRONG -> MaterialTheme.colorScheme.error
    }
    val animatedTextColor by animateColorAsState(textColor)

    val shape = RoundedCornerShape(size = 24.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = shape,
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = shape,
            )
            .clip(shape)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        // Text
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = answer.answer,
            style = MaterialTheme.typography.bodyLarge,
            color = animatedTextColor,
        )

        // Icon
        if (state != QuizAnswerState.NONE) {
            FadeInAnimation {
                Icon(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(24.dp)
                        .align(Alignment.CenterVertically),
                    imageVector = when (state) {
                        QuizAnswerState.CORRECT -> Icons.Default.Check
                        QuizAnswerState.WRONG -> Icons.Default.Close
                        else -> throw IllegalStateException("Invalid state for answer: $state")
                    },
                    contentDescription = null,
                    tint = animatedTextColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun QuizQuestionEmptyPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            QuizQuestion(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                question = fakeQuestion,
                answersStates = listOf(
                    QuizAnswerState.NONE,
                    QuizAnswerState.NONE,
                    QuizAnswerState.NONE,
                    QuizAnswerState.NONE,
                ),
                isLastQuestion = false,
                onAnswerSelected = {},
                onNextClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun QuizQuestionAnsweredPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            QuizQuestion(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                question = fakeQuestion,
                answersStates = listOf(
                    QuizAnswerState.NONE,
                    QuizAnswerState.CORRECT,
                    QuizAnswerState.WRONG,
                    QuizAnswerState.NONE,
                ),
                isLastQuestion = false,
                onAnswerSelected = {},
                onNextClick = {}
            )
        }
    }
}

private val fakeQuestion by lazy {
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
}