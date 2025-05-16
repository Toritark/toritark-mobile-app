package com.toritark.stories.presentation.learning_words.component.sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.main.app.LocalExtendedColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
internal fun CorrectSentenceText(
    modifier: Modifier = Modifier,
    sentence: SentenceWithParts,
) {
    FlowRow(
        modifier = modifier,
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        sentence.parts.forEachIndexed { index, part ->
            when (part) {
                is SentencePart.Text -> {
                    Text(
                        text = part.text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                is SentencePart.Input -> {
                    if (index != 0 && sentence.parts[index - 1] is SentencePart.Input) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Text(
                        text = styleInputText(
                            part = part,
                            defaultColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            successColor = LocalExtendedColors.current.success.success,
                            errorColor = MaterialTheme.colorScheme.error,
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

private fun styleInputText(
    part: SentencePart.Input,
    defaultColor: Color,
    successColor: Color,
    errorColor: Color,
): AnnotatedString {
    return when (part.state) {
        SentencePart.Input.State.CORRECT -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = successColor)) {
                    append(part.correctText)
                }
            }
        }

        SentencePart.Input.State.INCORRECT -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = errorColor, textDecoration = TextDecoration.Underline)) {
                    append(part.correctText)
                }
            }
        }

        else -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = defaultColor)) {
                    append(part.correctText)
                }
            }
        }
    }
}

@Preview
@Composable
private fun CorrectSentenceTextPreview() {
    AppTheme {
        AppTheme {
            Box(
                modifier = Modifier
                    .size(width = 300.dp, height = 100.dp)
                    .background(color = MaterialTheme.colorScheme.background),
            ) {
                CorrectSentenceText(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .align(Alignment.Center),
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
                        nativeLanguageText = "",
                    )
                )
            }
        }
    }
}