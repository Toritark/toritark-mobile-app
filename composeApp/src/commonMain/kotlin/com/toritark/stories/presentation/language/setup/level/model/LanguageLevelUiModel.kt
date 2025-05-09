package com.toritark.stories.presentation.language.setup.level.model

import com.toritark.stories.data.language.model.LanguageLevel
import org.jetbrains.compose.resources.StringResource

data class LanguageLevelUiModel(
    val languageLevel: LanguageLevel,
    val titleStringResource: StringResource,
    val descriptionStringResource: StringResource,
)
