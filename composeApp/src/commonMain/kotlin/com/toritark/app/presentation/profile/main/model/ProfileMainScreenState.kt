package com.toritark.app.presentation.profile.main.model

import androidx.compose.runtime.Immutable
import com.toritark.app.data.language.model.Language
import com.toritark.app.data.profile.api.model.ProfileApiModel
import com.toritark.app.presentation.language.setup.level.model.LanguageLevelUiModel

@Immutable
internal data class ProfileMainScreenState(
    val profileState: ProfileUiState = ProfileUiState.Loading,
    val languageSettingsState: LanguageSettingsState = LanguageSettingsState.Loading,
) {

    @Immutable
    sealed interface ProfileUiState {
        @Immutable
        data object Loading : ProfileUiState

        @Immutable
        data class Present(
            val profile: ProfileApiModel,
        ) : ProfileUiState
    }

    @Immutable
    sealed interface LanguageSettingsState {
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