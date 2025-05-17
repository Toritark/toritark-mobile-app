package com.toritark.app.presentation.learning_words.component.sentence

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.learning_words.component.part.SentenceInputPart
import com.toritark.app.presentation.learning_words.component.part.SentenceTextPart
import com.toritark.app.presentation.learning_words.main.model.LearningWordsMainScreenContent
import com.toritark.app.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.app.presentation.learning_words.main.model.sentence.SentenceWithParts
import com.toritark.app.presentation.main.app.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val LOG_TAG = "LearningSentenceComponent"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal fun LearningSentenceText(
    modifier: Modifier = Modifier,
    currentSentence: LearningWordsMainScreenContent.CurrentSentence.Present,
    onInputChange: (partIndex: Int, text: String) -> Unit,
    onFocusChange: (index: Int, isFocused: Boolean) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val inputFocusRequesters = remember(currentSentence.sentence.id) { mutableListOf<FocusRequester>() }

    val inputPartIndices = remember(currentSentence.sentence.id) {
        currentSentence.sentence.parts.mapIndexedNotNull { index, part ->
            if (part is SentencePart.Input) index else null
        }
    }

    while (inputFocusRequesters.size < inputPartIndices.size) {
        inputFocusRequesters.add(FocusRequester())
    }

    LaunchedEffect(currentSentence.sentence.id) {
        if (inputFocusRequesters.isNotEmpty()) {
            inputFocusRequesters[0].requestFocus()
        }
    }

    FlowRow(
        modifier = modifier,
        itemVerticalAlignment = Alignment.CenterVertically,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)

        val sentence = currentSentence.sentence

        var inputPartIndex = 0

        sentence.parts.forEachIndexed { index, part ->
            when (part) {
                is SentencePart.Text -> {
                    SentenceTextPart(
                        modifier = Modifier,
                        textStyle = textStyle,
                        part = part,
                    )
                }

                is SentencePart.Input -> {
                    val currentInputIndex = inputPartIndex
                    inputPartIndex++

                    val isEditable = currentSentence is LearningWordsMainScreenContent.CurrentSentence.Present.Todo &&
                            part.state != SentencePart.Input.State.CORRECT

                    val onInputChange = remember(index, onInputChange) {
                        { text: String ->
                            onInputChange(index, text)
                        }
                    }
                    val onInputDone = remember(currentInputIndex) {
                        {
                            if (currentInputIndex < inputFocusRequesters.size - 1) {
                                inputFocusRequesters[currentInputIndex + 1].requestFocus()
                            } else {
                                focusManager.clearFocus()
                            }

                            Unit
                        }
                    }

                    val onFocusChanged = remember(index, onFocusChange) {
                        { isFocused: Boolean ->
                            onFocusChange(index, isFocused)
                        }
                    }

                    if (index != 0 && sentence.parts[index - 1] is SentencePart.Input) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    SentenceInputPart(
                        modifier = Modifier
                            .focusRequester(inputFocusRequesters[currentInputIndex]),
                        textStyle = textStyle,
                        part = part,
                        isEditable = isEditable,
                        onInputChange = onInputChange,
                        onInputDone = onInputDone,
                        onFocusChanged = onFocusChanged,
                    )
                }
            }
        }
    }
}


@Preview
@Composable
private fun LearningSentenceComponentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 600.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            LearningSentenceText(
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
                        nativeLanguageText = "Привет, прекрасный мир!"
                    )
                ),
                onInputChange = { _, _ -> },
                onFocusChange = { _, _ -> }
            )
        }
    }
}
