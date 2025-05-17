package com.toritark.app.presentation.learning_words.component.part

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.toritark.app.presentation.learning_words.main.model.sentence.SentencePart

@Composable
internal fun SentenceTextPart(
    modifier: Modifier,
    textStyle: TextStyle,
    part: SentencePart.Text,
) {
    Text(
        modifier = modifier,
        text = part.text,
        style = textStyle,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
    )
}