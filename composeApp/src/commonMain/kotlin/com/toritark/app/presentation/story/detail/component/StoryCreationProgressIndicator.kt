package com.toritark.app.presentation.story.detail.component

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
import com.toritark.app.presentation.core_ui.animation.FadeInAnimation
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.*

@Composable
internal fun StoryCreationProgressIndicator(
    modifier: Modifier = Modifier,
) {
    val steps = listOf(
        Res.string.title_story_creation_progress_1,
        Res.string.title_story_creation_progress_2,
        Res.string.title_story_creation_progress_3,
        Res.string.title_story_creation_progress_4,
    )
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            step = (step + 1) % steps.size
        }
    }

    FadeInAnimation {
        Column(
            modifier = modifier,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StoryCreationProgressIndicatorPreview() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(MaterialTheme.colorScheme.surface)
            .padding(8.dp)
    ) {
        StoryCreationProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
    }
}
