package com.toritark.app.presentation.auth.sign_in

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.presentation.auth.icon.Google
import com.toritark.app.presentation.auth.sign_in.component.PlatformSignInHandler
import com.toritark.app.presentation.auth.sign_in.component.SignInButton
import com.toritark.app.presentation.auth.sign_in.model.SignInScreenState
import com.toritark.app.presentation.auth.sign_in.model.provider.AuthProviderUiModel
import com.toritark.app.presentation.core_ui.icon.AppIcons
import com.toritark.app.presentation.core_ui.nav.OnNavigateTo
import com.toritark.app.presentation.core_ui.nav.OnPopBackStack
import com.toritark.app.presentation.core_ui.screen.BaseScreen
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_auth_sign_in_google_btn
import toritark.composeapp.generated.resources.title_auth_sign_in_screen
import toritark.composeapp.generated.resources.toritark_owl

@Composable
internal fun SignInScreen(
    onNavigateTo: OnNavigateTo,
    onPopBackStack: OnPopBackStack,
    viewModel: SignInViewModel = koinViewModel(),
) {
    viewModel.onNavigateTo = onNavigateTo
    viewModel.onPopBackStack = onPopBackStack

    BaseScreen(
        viewModel = viewModel,
    ) { contentValue ->
        SignInScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .systemBarsPadding(),
            signInScreenState = contentValue,
            onSignInClick = viewModel::onSignInClick,
        )

        PlatformSignInHandler(
            viewModel = viewModel,
        )
    }
}

@Composable
private fun SignInScreenContent(
    modifier: Modifier = Modifier,
    signInScreenState: SignInScreenState,
    onSignInClick: (authProviderUiModel: AuthProviderUiModel) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Image(
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterHorizontally),
            painter = painterResource(Res.drawable.toritark_owl),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            text = stringResource(Res.string.title_auth_sign_in_screen),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.weight(1f))

        SignInButtons(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            authProviders = signInScreenState.authProviders,
            enabled = signInScreenState.authProgressState is SignInScreenState.AuthProgressState.Idle,
            onSignInClick = onSignInClick,
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun SignInButtons(
    modifier: Modifier = Modifier,
    authProviders: List<AuthProviderUiModel>,
    enabled: Boolean,
    onSignInClick: (authProviderUiModel: AuthProviderUiModel) -> Unit,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Column(
        modifier = modifier,
    ) {
        authProviders.forEach { authProvider ->
            SignInButton(
                modifier = Modifier
                    .fillMaxWidth(),
                authProvider = authProvider,
                enabled = enabled,
                onClick = {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.Confirm)

                    onSignInClick(authProvider)
                },
            )
        }
    }
}

@Preview
@Composable
private fun SignInScreenContentPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 700.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            SignInScreenContent(
                modifier = Modifier
                    .fillMaxWidth(),
                signInScreenState = SignInScreenState(
                    authProviders = listOf(
                        AuthProviderUiModel(
                            provider = AuthProvider.GOOGLE,
                            icon = AppIcons.Google,
                            titleResource = Res.string.title_auth_sign_in_google_btn,
                        ),
                    ),
                    authProgressState = SignInScreenState.AuthProgressState.Idle,
                ),
                onSignInClick = {},
            )
        }
    }
}