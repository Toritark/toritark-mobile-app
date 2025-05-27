package com.toritark.app.presentation.auth.sign_in.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import com.toritark.app.presentation.auth.sign_in.SignInViewModel

private val logger = Logger.withTag("PlatformSignInHandler")

@Composable
internal actual fun PlatformSignInHandler(viewModel: SignInViewModel) {

    LaunchedEffect(viewModel) {
        viewModel.signInRequestEvents.collect { authProvider ->
            logger.e { "PlatformSignInHandler is not implemented" }
            // TODO
        }
    }
}