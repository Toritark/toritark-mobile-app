package com.toritark.stories.presentation.story.retelling.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.stories.presentation.core_ui.animation.FadeInAnimation
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun StoryRetellingReviewInProgress(
    modifier: Modifier = Modifier,
) {
    val steps = listOf(
        Res.string.title_story_retelling_review_progress_1,
        Res.string.title_story_retelling_review_progress_2,
        Res.string.title_story_retelling_review_progress_3,
        Res.string.title_story_retelling_review_progress_4,
        Res.string.title_story_retelling_review_progress_5,
        Res.string.title_story_retelling_review_progress_6,
    )

    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        for (i in 1..steps.lastIndex) {
            delay(2000)
            step = i
        }
    }

    FadeInAnimation {
        Column(
            modifier = modifier,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Spacer(modifier = Modifier.size(16.dp))

            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    fadeIn(animationSpec = tween(durationMillis = 500)) togetherWith
                            fadeOut(animationSpec = tween(durationMillis = 500))
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) { currentStepIndex ->
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(steps[currentStepIndex]),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoryRetellingReviewInProgressPreview() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .size(width = 400.dp, height = 600.dp)
                .background(color = MaterialTheme.colorScheme.background),
        ) {
            StoryRetellingReviewInProgress(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
            )
        }
    }
}