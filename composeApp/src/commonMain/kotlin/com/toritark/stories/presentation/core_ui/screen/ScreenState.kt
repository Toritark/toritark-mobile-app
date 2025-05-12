package com.toritark.stories.presentation.core_ui.screen

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

sealed interface ScreenState {
    data object Loading : ScreenState

    data class Content<T>(
        val value: T,
    ) : ScreenState

    sealed interface Error : ScreenState {
        data class Text(val message: String) : Error
        data class Resource(val resource: StringResource) : Error

        @Composable
        fun getMessage(): String {
            return when (this) {
                is Text -> message
                is Resource -> stringResource(resource)
            }
        }
    }

}