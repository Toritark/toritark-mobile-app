package com.toritark.stories.presentation.core_ui.screen

sealed interface ScreenState {
    data object Loading : ScreenState
    data object Content : ScreenState
    data class Error(val message: String) : ScreenState
}