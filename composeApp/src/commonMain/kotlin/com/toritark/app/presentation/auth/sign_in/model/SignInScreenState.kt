package com.toritark.app.presentation.auth.sign_in.model

import androidx.compose.runtime.Immutable
import com.toritark.app.presentation.auth.sign_in.model.provider.AuthProviderUiModel

@Immutable
internal data class SignInScreenState(
    val authProviders: List<AuthProviderUiModel> = emptyList(),
    val authProgressState: AuthProgressState = AuthProgressState.Idle,
) {

    @Immutable
    sealed interface AuthProgressState {
        @Immutable
        data object Idle : AuthProgressState

        @Immutable
        data object InProgress : AuthProgressState
    }
}