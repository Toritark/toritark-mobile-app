package com.toritark.stories.presentation.story.detail.component

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Displays story text sentence-by-sentence and translated sentences
 */
@Composable
internal fun StoryText(
    modifier: Modifier = Modifier,
    learningLanguageText: List<String>,
    nativeLanguageText: List<String>,
) {
    var expandedSentenceIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = modifier
            .padding(vertical = 8.dp)
    ) {
        learningLanguageText.forEachIndexed { index, learningSentence ->
            val nativeSentence = nativeLanguageText.getOrNull(index)

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                SentenceItem(
                    text = learningSentence,
                    isNativeSentenceExpanded = expandedSentenceIndex == index,
                    onClick = {
                        expandedSentenceIndex = if (expandedSentenceIndex == index) null else index
                    },
                )

                if (nativeSentence != null) {
                    AnimatedVisibility(
                        visible = expandedSentenceIndex == index,
                        enter = fadeIn(animationSpec = tween(durationMillis = 300)) + expandVertically(
                            animationSpec = tween(durationMillis = 300),
                            expandFrom = Alignment.Top
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 300)) + shrinkVertically(
                            animationSpec = tween(durationMillis = 300),
                            shrinkTowards = Alignment.Top
                        )
                    ) {
                        NativeSentenceItem(
                            text = nativeSentence,
                            onClick = {
                                expandedSentenceIndex = null
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentenceItem(
    text: String,
    isNativeSentenceExpanded: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick, interactionSource = null, indication = null),
    ) {
        val backgroundColor = if (isNativeSentenceExpanded) {
            MaterialTheme.colorScheme.inversePrimary
        } else {
            Color.Transparent
        }
        val modifier = Modifier
            .padding(horizontal = 8.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp)

        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
            ),
            modifier = modifier,
        )
    }
}

@Composable
private fun NativeSentenceItem(
    text: String,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick, interactionSource = null, indication = null),
    )
}

@Preview
@Composable
private fun StoryTextPreview() {
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
        modifier = Modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}