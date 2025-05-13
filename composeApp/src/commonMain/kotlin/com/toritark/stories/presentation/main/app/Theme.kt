package com.toritark.stories.presentation.main.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class SuccessColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

private val LightSuccessColors = SuccessColors(
    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFC8E6C9),
    onSuccessContainer = Color(0xFF1B5E20),
)

private val DarkSuccessColors = SuccessColors(
    success = Color(0xFFA5D6A7),
    onSuccess = Color(0xFF000000),
    successContainer = Color(0xFF388E3C),
    onSuccessContainer = Color(0xFFDCEDC8),
)

private val LocalSuccessColors = staticCompositionLocalOf {
    SuccessColors(
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified,
    )
}


object AppTheme {

    val successColors: SuccessColors
        @Composable
        get() = LocalSuccessColors.current

    @Composable
    operator fun invoke(
        darkTheme: Boolean = false,
        content: @Composable () -> Unit,
    ) {
        val successColors = if (darkTheme) {
            DarkSuccessColors
        } else {
            LightSuccessColors
        }

        CompositionLocalProvider(
            LocalSuccessColors provides successColors
        ) {
            MaterialTheme(
                content = content
            )
        }
    }
}