package com.toritark.app.presentation.profile.main.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.language.model.Language
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel

@Immutable
internal data class ProfileMainScreenState(
    val languageSettingsState: LanguageSettingsState = LanguageSettingsState.Loading,
) {

    @Immutable
    interface LanguageSettingsState {
        @Immutable
        data object Loading : LanguageSettingsState

        @Immutable
        data class Present(
            val learningLanguage: Language,
            val languageLevel: LanguageLevelUiModel,
            val nativeLanguage: Language,
        ) : LanguageSettingsState
    }
}