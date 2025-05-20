package com.toritark.app.presentation.learning_words.component.sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.app.presentation.core_ui.animation.FadeInAnimation
import com.toritark.app.presentation.learning_words.main.model.LearningWordsMainScreenState
import com.toritark.app.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.app.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_learning_words_next_sentence

private const val LOG_TAG = "LearningSentence"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun LearningSentence(
    modifier: Modifier = Modifier,
    currentSentence: LearningWordsMainScreenState.CurrentSentence.Present,
    onInputChange: (partIndex: Int, text: String) -> Unit,
    onNextClick: () -> Unit,
    onHelpClick: (partIndex: Int?) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    val sentence = currentSentence.sentence

    val focusedPartIndex = remember(currentSentence.sentence.id) {
        mutableStateOf<Int?>(null)
    }

    Column(
        modifier = modifier,
    ) {
        val isComplete = sentence.areAllPartsComplete

        LearningSentenceText(
            modifier = Modifier
                .fillMaxWidth(),
            currentSentence = currentSentence,
            onInputChange = onInputChange,
            onFocusChange = { index, isFocused ->
                when {
                    focusedPartIndex.value == index && !isFocused -> {
                        focusedPartIndex.value = null
                    }

                    focusedPartIndex.value != index && isFocused -> {
                        focusedPartIndex.value = index
                    }
                }
            }
        )

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

        Spacer(modifier = Modifier.height(16.dp))

        FadeInAnimation(
            visible = currentSentence is LearningWordsMainScreenState.CurrentSentence.Present.Todo,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                            shape = CircleShape,
                        )
                        .size(48.dp),
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                        onHelpClick(focusedPartIndex.value)
                    },
                    enabled = !currentSentence.isComplete,
                ) {
                    Icon(
                        modifier = Modifier
                            .size(28.dp),
                        imageVector = Icons.AutoMirrored.Rounded.HelpOutline,
                        contentDescription = stringResource(Res.string.title_learning_words_next_sentence),
                        tint = MaterialTheme.colorScheme.onSecondary,
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

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
                        .size(64.dp),
                    enabled = currentSentence is LearningWordsMainScreenState.CurrentSentence.Present.Todo,
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
                currentSentence = LearningWordsMainScreenState.CurrentSentence.Present.Todo(
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
                onNextClick = { },
                onHelpClick = { },
            )
        }
    }
}
