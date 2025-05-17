package com.toritark.stories.presentation.language.setup.level.model

import androidx.compose.runtime.Immutable
import com.toritark.stories.data.language.model.LanguageLevel
import org.jetbrains.compose.resources.StringResource
import toritark.composeapp.generated.resources.*

@Immutable
data class LanguageLevelUiModel(
    val languageLevel: LanguageLevel,
    val titleStringResource: StringResource,
    val descriptionStringResource: StringResource,
) {

    companion object {

        val allLevels = listOf(
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.A1,
                titleStringResource = Res.string.title_language_level_none,
                descriptionStringResource = Res.string.desc_language_level_none,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.A2,
                titleStringResource = Res.string.title_language_level_a1,
                descriptionStringResource = Res.string.desc_language_level_a1,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.B1,
                titleStringResource = Res.string.title_language_level_a2,
                descriptionStringResource = Res.string.desc_language_level_a2,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.B2,
                titleStringResource = Res.string.title_language_level_b1,
                descriptionStringResource = Res.string.desc_language_level_b1,
            ),
            LanguageLevelUiModel(
                languageLevel = LanguageLevel.C1,
                titleStringResource = Res.string.title_language_level_c1,
                descriptionStringResource = Res.string.desc_language_level_c1,
            ),
        )

        fun getForLanguageLevel(level: LanguageLevel): LanguageLevelUiModel {
            val levelUiModel = allLevels.find { it.languageLevel == level }
            return requireNotNull(levelUiModel) { "LanguageLevelUiModel not found for level $level" }
        }
    }
}
