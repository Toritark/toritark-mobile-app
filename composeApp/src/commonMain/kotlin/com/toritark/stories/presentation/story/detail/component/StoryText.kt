package com.toritark.stories.presentation.story.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toritark.stories.presentation.core_ui.animation.FadeAndExpandVerticallyAnimation
import com.toritark.stories.presentation.core_ui.component.AiDisclaimer
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Displays story text sentence-by-sentence and translated sentences
 */
@Composable
internal fun StoryText(
    modifier: Modifier = Modifier,
    learningLanguageText: List<String>,
    nativeLanguageText: List<String>,
    onCopyClick: () -> Unit,
) {
    var expandedSentenceIndex by remember { mutableStateOf<Int?>(null) }

    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        AiDisclaimer()

        Spacer(modifier = Modifier.height(8.dp))

        learningLanguageText.forEachIndexed { index, learningSentence ->
            val nativeSentence = nativeLanguageText.getOrNull(index)

            SentenceItem(
                learningLanguageText = learningSentence,
                nativeLanguageText = nativeSentence,
                isExpanded = expandedSentenceIndex == index,
                hapticFeedback = hapticFeedback,
                onClick = {
                    expandedSentenceIndex = if (expandedSentenceIndex == index) null else index
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        IconButton(
            modifier = Modifier
                .align(Alignment.End),
            onClick = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)

                onCopyClick()
            }
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun SentenceItem(
    learningLanguageText: String,
    nativeLanguageText: String?,
    isExpanded: Boolean,
    hapticFeedback: HapticFeedback,
    onClick: () -> Unit,
) {
    val backgroundColor = if (isExpanded) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
    } else {
        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
    }

    val textColor = if (isExpanded) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onSecondaryContainer
    }

    val shape = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = backgroundColor, shape = shape)
            .clip(shape)
            .clickable {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.ContextClick)
                onClick()
            }
            .padding(horizontal = 12.dp, vertical = 12.dp),
    ) {
        LearningLanguageSentenceItem(
            text = learningLanguageText,
            textColor = textColor,
        )

        if (nativeLanguageText != null) {
            FadeAndExpandVerticallyAnimation(
                visible = isExpanded,
            ) {
                NativeSentenceItem(
                    text = nativeLanguageText,
                    textColor = textColor,
                )
            }
        }
    }
}

@Composable
private fun LearningLanguageSentenceItem(
    text: String,
    textColor: Color,
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        text = text,
        style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 18.sp,
        ),
        color = textColor,
    )
}

@Composable
private fun NativeSentenceItem(
    text: String,
    textColor: Color,
) {
    Text(
        modifier = Modifier
            .padding(start = 4.dp, top = 4.dp, end = 4.dp),
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = textColor,
    )
}

@Preview
@Composable
private fun StoryTextPreview() {
    Box(
        modifier = Modifier
            .size(width = 400.dp, height = 500.dp)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        StoryText(
            learningLanguageText = listOf(
                "Hello, my name is Toritark.",
                "I am a developer.",
                "I like to code.",
                "I am learning Kotlin.",
                "I am learning Jetpack Compose. This is a long, multi-line sentence",
            ),
            nativeLanguageText = listOf(
                "Tere, mina olen Toritark.",
                "Mina olen arendaja.",
                "Test test test",
                "Mina elan Tartus.",
                "Mina olen 32 aastat vana.",
            ),
            modifier = Modifier.fillMaxWidth(),
            onCopyClick = {},
        )
    }
}