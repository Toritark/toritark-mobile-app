package com.toritark.app.presentation.auth.sign_in.component

import androidx.compose.runtime.Composable
import com.toritark.app.presentation.auth.sign_in.SignInViewModel

@Composable
internal expect fun PlatformSignInHandler(
    viewModel: SignInViewModel,
)