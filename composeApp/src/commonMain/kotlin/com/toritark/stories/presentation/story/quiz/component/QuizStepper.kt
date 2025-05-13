package com.toritark.stories.presentation.story.quiz.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.story.quiz.model.QuizQuestionState
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
internal fun QuizStepper(
    states: List<QuizQuestionState>,
    modifier: Modifier = Modifier,
    stripHeight: Dp = 4.dp,
    stripSpacing: Dp = 4.dp,
    animationDurationMillis: Int = 300,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(stripSpacing)
    ) {
        states.forEach { state ->
            StepIndicatorItem(
                state = state,
                modifier = Modifier
                    .weight(1f) // Distribute width equally
                    .height(stripHeight),
                animationDurationMillis = animationDurationMillis,
                cornerRadius = 8f,
            )
        }
    }
}

@Composable
private fun StepIndicatorItem(
    state: QuizQuestionState,
    modifier: Modifier = Modifier,
    animationDurationMillis: Int,
    cornerRadius: Float,
) {
    val targetColor = state.toColor()

    var previousVisualColor by remember { mutableStateOf(targetColor) }

    val fillFraction = remember { Animatable(1f) }

    LaunchedEffect(targetColor) {
        if (previousVisualColor == targetColor) {
            if (fillFraction.value != 1f) {
                fillFraction.snapTo(1f)
            }
        } else {
            fillFraction.snapTo(0f)
            fillFraction.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = animationDurationMillis, easing = LinearEasing)
            )
            previousVisualColor = targetColor
        }
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        drawRoundRect(
            cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius),
            color = previousVisualColor,
            size = Size(canvasWidth, canvasHeight)
        )

        if (fillFraction.value > 0f) {
            drawRoundRect(
                cornerRadius = CornerRadius(x = cornerRadius, y = cornerRadius),
                color = targetColor,
                topLeft = Offset.Zero,
                size = Size(canvasWidth * fillFraction.value, canvasHeight)
            )
        }
    }
}

@Composable
private fun QuizQuestionState.toColor(): Color {
    return when (this) {
        QuizQuestionState.NONE -> MaterialTheme.colorScheme.surfaceVariant
        QuizQuestionState.CURRENT -> MaterialTheme.colorScheme.inversePrimary
        QuizQuestionState.CORRECT -> MaterialTheme.colorScheme.primaryContainer
        QuizQuestionState.WRONG -> MaterialTheme.colorScheme.errorContainer
    }
}

@Preview()
@Composable
fun HorizontalStepperPreview() {
    AppTheme {
        Column {
            QuizStepper(
                states = listOf(
                    QuizQuestionState.CORRECT,
                    QuizQuestionState.CURRENT,
                    QuizQuestionState.NONE,
                    QuizQuestionState.NONE
                )
            )

            Spacer(Modifier.height(20.dp))

            QuizStepper(
                states = listOf(
                    QuizQuestionState.WRONG,
                    QuizQuestionState.CORRECT,
                    QuizQuestionState.CORRECT,
                    QuizQuestionState.CURRENT,
                    QuizQuestionState.NONE
                ),
                stripHeight = 8.dp,
                stripSpacing = 8.dp,
                animationDurationMillis = 600
            )

            Spacer(Modifier.height(20.dp))

            QuizStepper(
                states = listOf(
                    QuizQuestionState.NONE,
                    QuizQuestionState.CURRENT,
                    QuizQuestionState.CORRECT,
                    QuizQuestionState.WRONG
                ),
                stripHeight = 6.dp,
                stripSpacing = 2.dp
            )
        }
    }
}

@Preview // Dark background
@Composable
fun HorizontalStepperDarkPreview() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Column {
            QuizStepper(
                states = listOf(
                    QuizQuestionState.CORRECT,
                    QuizQuestionState.CURRENT,
                    QuizQuestionState.NONE,
                    QuizQuestionState.WRONG
                ),
                stripHeight = 10.dp,
                stripSpacing = 5.dp
            )
        }
    }
}