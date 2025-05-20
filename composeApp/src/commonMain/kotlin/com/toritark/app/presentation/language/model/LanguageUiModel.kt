package com.toritark.app.presentation.language.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.language.model.Language

@Immutable
data class LanguageUiModel(
    val language: Language,
    val displayName: String,
)
