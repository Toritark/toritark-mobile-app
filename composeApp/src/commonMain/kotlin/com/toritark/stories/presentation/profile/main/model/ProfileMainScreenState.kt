package com.toritark.stories.presentation.profile.main.model

import androidx.compose.runtime.Immutable
import com.toritark.stories.data.language.model.Language
import com.toritark.stories.presentation.language.setup.level.model.LanguageLevelUiModel

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