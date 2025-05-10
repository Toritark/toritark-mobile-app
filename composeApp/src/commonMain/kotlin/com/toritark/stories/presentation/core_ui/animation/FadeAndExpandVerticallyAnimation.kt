package com.toritark.stories.presentation.core_ui.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun FadeAndExpandVerticallyAnimation(
    visible: Boolean = true,
    durationMillis: Int = 300,
    content: @Composable() AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)) + expandVertically(
            animationSpec = tween(durationMillis = durationMillis),
            expandFrom = Alignment.Top
        ),
        exit = fadeOut(animationSpec = tween(durationMillis = durationMillis)) + shrinkVertically(
            animationSpec = tween(durationMillis = durationMillis),
            shrinkTowards = Alignment.Bottom
        ),
        content = content,
    )
}