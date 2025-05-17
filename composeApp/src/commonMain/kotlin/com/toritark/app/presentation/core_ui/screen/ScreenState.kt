package com.toritark.app.presentation.core_ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Immutable
sealed interface ScreenState {
    @Immutable
    data object Loading : ScreenState

    @Immutable
    data class Content<T>(
        val value: T,
    ) : ScreenState

    @Immutable
    sealed interface Error : ScreenState {
        // TODO: Leave only String version, resolving resource from ViewModel
        @Immutable
        data class Text(val message: String) : Error

        @Immutable
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