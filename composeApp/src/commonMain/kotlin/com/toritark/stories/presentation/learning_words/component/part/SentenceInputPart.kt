package com.toritark.stories.presentation.learning_words.component.part

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.touchlab.kermit.Logger
import com.toritark.stories.presentation.learning_words.main.model.sentence.SentencePart
import com.toritark.stories.presentation.main.app.AppTheme
import com.toritark.stories.presentation.main.app.LocalExtendedColors
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val LOG_TAG = "SentenceInputPart"
private val logger = Logger.withTag(LOG_TAG)

private const val TEXT_WIDTH_MULTIPLIER = 1.1f

@Immutable
private object SentenceInputPartDefaults {
    val borderWidth = 2.dp

    val horizontalPadding = 8.dp
    val verticalPadding = 4.dp

    val successColor: Color
        @Composable
        get() = LocalExtendedColors.current.success.success

    val errorColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.error

    val outlineColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.primaryContainer

    val defaultTextColor: Color
        @Composable
        get() = MaterialTheme.colorScheme.onSurface
}

@Composable
internal fun SentenceInputPart(
    modifier: Modifier,
    textStyle: TextStyle,
    part: SentencePart.Input,
    isEditable: Boolean,
    onInputChange: (text: String) -> Unit,
    onInputDone: () -> Unit,
) {
    SentenceInputPartText(
        modifier = modifier,
        textStyle = textStyle,
        part = part,
        isEditable = isEditable,
        onInputChange = onInputChange,
        onInputDone = onInputDone,
    )
}

@Composable
private fun SentenceInputPartText(
    modifier: Modifier = Modifier,
    textStyle: TextStyle,
    part: SentencePart.Input,
    isEditable: Boolean,
    onInputChange: (text: String) -> Unit,
    onInputDone: () -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    val calculatedWidthDp = remember(part.correctText, textStyle, textMeasurer, density) {
        textMeasurer.calculateTextWidth(
            text = part.correctText,
            textStyle = textStyle,
            horizontalPadding = SentenceInputPartDefaults.horizontalPadding,
            borderWidth = SentenceInputPartDefaults.borderWidth,
            density = density,
        )
    }

    val defaultTextColor = SentenceInputPartDefaults.defaultTextColor
    val successTextColor = SentenceInputPartDefaults.successColor
    val errorTextColor = SentenceInputPartDefaults.errorColor

    var text by remember { mutableStateOf(part.currentText) }

    val coloredText = remember(text, part.state, part.correctText) {
        styleInputText(
            text = text,
            part = part,
            defaultColor = defaultTextColor,
            successColor = successTextColor,
            errorColor = errorTextColor,
        )
    }

    val textFieldValue = remember(coloredText, text) {
        TextFieldValue(
            annotatedString = coloredText,
            selection = TextRange(text.length)
        )
    }

    InputPartTextField(
        modifier = modifier
            .width(calculatedWidthDp)
            .border(
                width = SentenceInputPartDefaults.borderWidth,
                color = getBorderColor(part),
                shape = MaterialTheme.shapes.small
            )
            .padding(
                horizontal = SentenceInputPartDefaults.horizontalPadding,
                vertical = SentenceInputPartDefaults.verticalPadding
            ),
        textFieldValue = textFieldValue,
        correctTextLength = part.correctText.length,
        textStyle = textStyle,
        isReadOnly = !isEditable,
        onInputChange = { newText ->
            text = newText
            onInputChange(newText)
        },
        onInputDone = onInputDone,
    )
}

@Composable
private fun InputPartTextField(
    modifier: Modifier,
    textFieldValue: TextFieldValue,
    correctTextLength: Int,
    textStyle: TextStyle,
    isReadOnly: Boolean,
    onInputChange: (text: String) -> Unit,
    onInputDone: () -> Unit,
) {
    var lastText by remember(textFieldValue.text) { mutableStateOf(textFieldValue.text) }
    var lastTextValue by remember(textFieldValue.annotatedString) { mutableStateOf(textFieldValue) }

    BasicTextField(
        modifier = modifier,
        value = lastTextValue,
        onValueChange = { newValue: TextFieldValue ->
            val newText = newValue.text

            if (newText.length <= correctTextLength) {
                onInputChange(newText)
                lastTextValue = newValue

                if (newText.length == correctTextLength && lastText != newText && !isReadOnly) {
                    onInputDone()
                }

                lastText = newText
            }


        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
            showKeyboardOnFocus = true,
            autoCorrectEnabled = false,
        ),
        singleLine = true,
        textStyle = textStyle.copy(color = Color.Unspecified),
        readOnly = isReadOnly,
    )
}

private fun TextMeasurer.calculateTextWidth(
    text: String,
    textStyle: TextStyle,
    horizontalPadding: Dp,
    borderWidth: Dp,
    density: Density,
): Dp {
    val widthPx = measure(
        text = text,
        style = textStyle,
        overflow = TextOverflow.Visible,
        softWrap = false,
        maxLines = 1,
    ).size.width * TEXT_WIDTH_MULTIPLIER

    return with(density) {
        widthPx.toDp() + horizontalPadding * 2 + borderWidth * 2
    }
}

private fun styleInputText(
    text: String,
    part: SentencePart.Input,
    defaultColor: Color,
    successColor: Color,
    errorColor: Color,
): AnnotatedString {
    return when (part.state) {
        SentencePart.Input.State.CORRECT -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = successColor)) {
                    append(text)
                }
            }
        }

        SentencePart.Input.State.INCORRECT -> {
            buildAnnotatedString {
                val correctText = part.correctText
                text.forEachIndexed { index, char ->
                    val color = if (index < correctText.length && char == correctText[index]) {
                        successColor
                    } else {
                        errorColor
                    }
                    withStyle(SpanStyle(color = color)) {
                        append(char)
                    }
                }
            }
        }

        else -> {
            buildAnnotatedString {
                withStyle(SpanStyle(color = defaultColor)) {
                    append(text)
                }
            }
        }
    }
}

@Composable
private fun getBorderColor(part: SentencePart.Input): Color {
    return when (part.state) {
        SentencePart.Input.State.EMPTY -> SentenceInputPartDefaults.outlineColor
        SentencePart.Input.State.INCORRECT -> SentenceInputPartDefaults.errorColor
        SentencePart.Input.State.CORRECT -> SentenceInputPartDefaults.successColor
    }
}

@Preview
@Composable
private fun EmptyPartPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SentenceInputPart(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.Center),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                part = SentencePart.Input(
                    state = SentencePart.Input.State.EMPTY,
                    correctText = "SomeLongWord",
                ),
                isEditable = true,
                onInputChange = { },
                onInputDone = { }
            )
        }
    }
}

@Preview
@Composable
private fun EmptyPartWithTextPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SentenceInputPart(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.Center),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                part = SentencePart.Input(
                    state = SentencePart.Input.State.EMPTY,
                    currentText = "Some",
                    correctText = "SomeLongWord",
                ),
                isEditable = true,
                onInputChange = { },
                onInputDone = { }
            )
        }
    }
}

@Preview
@Composable
private fun IncorrectPartPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SentenceInputPart(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.Center),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                part = SentencePart.Input(
                    state = SentencePart.Input.State.INCORRECT,
                    currentText = "SomeLonWeird",
                    correctText = "SomeLongWord",
                ),
                isEditable = true,
                onInputChange = { },
                onInputDone = { }
            )
        }
    }
}

@Preview
@Composable
private fun CorrectPartPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 300.dp, height = 100.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            SentenceInputPart(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .align(Alignment.Center),
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp),
                part = SentencePart.Input(
                    state = SentencePart.Input.State.CORRECT,
                    currentText = "SomeLongWord",
                    correctText = "SomeLongWord",
                ),
                isEditable = true,
                onInputChange = { },
                onInputDone = { }
            )
        }
    }
}