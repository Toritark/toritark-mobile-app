package com.toritark.stories.domain.auth.model

sealed interface AuthState {
    data object Unknown : AuthState
    data object NotAuthenticated : AuthState

    sealed interface Authenticated : AuthState {
        data object Anonymous : Authenticated
        data object Normal : Authenticated
    }
}