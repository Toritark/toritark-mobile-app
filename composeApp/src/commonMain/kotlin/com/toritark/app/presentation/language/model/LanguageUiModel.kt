package com.toritark.app.presentation.language.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.language.model.Language
import org.jetbrains.compose.resources.getString
import toritark.composeapp.generated.resources.*

@Immutable
data class LanguageUiModel(
    val language: Language,
    val displayName: String,
) {

    companion object {
        internal suspend fun fromLanguage(language: Language): LanguageUiModel {
            return LanguageUiModel(
                language = language,
                displayName = when (language.isoCode) {
                    "en" -> getString(Res.string.title_language_name_english)
                    "es" -> getString(Res.string.title_language_name_spanish)
                    "de" -> getString(Res.string.title_language_name_german)
                    "fr" -> getString(Res.string.title_language_name_french)
                    "it" -> getString(Res.string.title_language_name_italian)
                    "ru" -> getString(Res.string.title_language_name_russian)
                    "uk" -> getString(Res.string.title_language_name_ukrainian)
                    "pl" -> getString(Res.string.title_language_name_polish)
                    "cs" -> getString(Res.string.title_language_name_czech)
                    "sr" -> getString(Res.string.title_language_name_serbian)
                    "pt" -> getString(Res.string.title_language_name_portuguese)
                    "fi" -> getString(Res.string.title_language_name_finnish)
                    "sv" -> getString(Res.string.title_language_name_swedish)
                    "et" -> getString(Res.string.title_language_name_estonian)
                    "lv" -> getString(Res.string.title_language_name_latvian)
                    "lt" -> getString(Res.string.title_language_name_lithuanian)
                    "lb" -> getString(Res.string.title_language_name_luxembourgish)
                    else -> throw IllegalArgumentException("Unknown language code: ${language.isoCode}")
                }
            )
        }
    }
}
