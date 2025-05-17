package com.toritark.app.presentation.core_ui.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FadeInAnimation(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    durationMillis: Int = 300,
    content: @Composable() AnimatedVisibilityScope.() -> Unit,
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(animationSpec = tween(durationMillis = durationMillis)),
        exit = fadeOut(animationSpec = tween(durationMillis = durationMillis)),
        content = content,
    )
}