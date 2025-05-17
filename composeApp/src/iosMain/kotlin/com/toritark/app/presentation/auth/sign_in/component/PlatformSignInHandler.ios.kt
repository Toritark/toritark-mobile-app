package com.toritark.app.presentation.auth.sign_in.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.toritark.app.presentation.auth.sign_in.SignInViewModel

@Composable
internal actual fun PlatformSignInHandler(viewModel: SignInViewModel) {
    LaunchedEffect(viewModel) {
        viewModel.signInRequestEvents.collect { authProvider ->
            TODO()
        }
    }
}