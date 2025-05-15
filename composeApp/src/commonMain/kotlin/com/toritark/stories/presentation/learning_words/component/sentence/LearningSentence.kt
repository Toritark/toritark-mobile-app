package com.toritark.stories.presentation.learning_words.component.sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.stories.presentation.core_ui.animation.FadeInAnimation
import com.toritark.stories.presentation.learning_words.main.model.LearningWordsMainScreenContent
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.stories.presentation.main.app.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_learning_words_next_sentence

private const val LOG_TAG = "LearningSentence"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun LearningSentence(
    modifier: Modifier = Modifier,
    currentSentence: LearningWordsMainScreenContent.CurrentSentence.Present,
    onInputChange: (partIndex: Int, text: String) -> Unit,
    onNextClick: () -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    val sentence = currentSentence.sentence

    logger.d { "currentSentence: $currentSentence" }

    Column(
        modifier = modifier,
    ) {
        val isComplete = sentence.areAllPartsComplete

        LearningSentenceText(
            modifier = Modifier
                .fillMaxWidth(),
            currentSentence = currentSentence,
            onInputChange = onInputChange,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 128.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp)
            ) {
                FadeAndExpandVerticallyAnimation(
                    visible = isComplete,
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(12.dp))

                        CorrectSentenceText(
                            modifier = Modifier,
                            sentence = sentence,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    modifier = Modifier,
                    text = sentence.nativeLanguageText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            FadeInAnimation(
                modifier = Modifier
                    .align(Alignment.Bottom),
                visible = currentSentence is LearningWordsMainScreenContent.CurrentSentence.Present.Todo,
            ) {
                IconButton(
                    modifier = Modifier
                        .background(
                            color = if (isComplete) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                            },
                            shape = CircleShape,
                        )
                        .size(64.dp)
                        .align(Alignment.Bottom),
                    enabled = currentSentence is LearningWordsMainScreenContent.CurrentSentence.Present.Todo,
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)
                        onNextClick()
                    },
                ) {
                    Icon(
                        modifier = Modifier
                            .size(36.dp),
                        imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                        contentDescription = stringResource(Res.string.title_learning_words_next_sentence),
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LearningSentencePreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .width(width = 600.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            LearningSentence(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                currentSentence = LearningWordsMainScreenContent.CurrentSentence.Present.Todo(
                    sentence = SentenceWithParts(
                        id = 1,
                        parts = listOf(
                            SentencePart.Text("Hello"),
                            SentencePart.Text(","),
                            SentencePart.Text(" "),
                            SentencePart.Text("my"),
                            SentencePart.Text(" "),
                            SentencePart.Text("dear"),
                            SentencePart.Text(" "),
                            SentencePart.Text("friends"),
                            SentencePart.Text(" "),
                            SentencePart.Text("and"),
                            SentencePart.Text(" "),
                            SentencePart.Input(
                                state = SentencePart.Input.State.EMPTY,
                                correctText = "this",
                            ),
                            SentencePart.Text(" "),
                            SentencePart.Text("naturally"),
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
                        nativeLanguageText = "Привет, прекрасный мир! Этот текст немного длиннее, чем обычно"
                    ),
                ),
                onInputChange = { _, _ -> },
                onNextClick = { }
            )
        }
    }
}
