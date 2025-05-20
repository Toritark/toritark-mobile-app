package com.toritark.app.presentation.auth.sign_in.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.presentation.auth.icon.Apple
import com.toritark.app.presentation.auth.icon.Google
import com.toritark.app.presentation.auth.sign_in.model.provider.AuthProviderUiModel
import com.toritark.app.presentation.core_ui.icon.AppIcons
import com.toritark.app.presentation.main.app.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_auth_sign_in_apple_btn
import toritark.composeapp.generated.resources.title_auth_sign_in_google_btn

@Composable
internal fun SignInButton(
    modifier: Modifier = Modifier,
    authProvider: AuthProviderUiModel,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
    ) {
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = authProvider.icon,
            contentDescription = stringResource(authProvider.titleResource),
        )

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            modifier = Modifier.padding(vertical = 12.dp),
            text = stringResource(authProvider.titleResource),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
        )
    }
}

@Preview
@Composable
private fun SignInButtonEnabledPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 300.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            SignInButton(
                modifier = Modifier
                    .fillMaxWidth(),
                authProvider = AuthProviderUiModel(
                    provider = AuthProvider.GOOGLE,
                    icon = AppIcons.Google,
                    titleResource = Res.string.title_auth_sign_in_google_btn,
                ),
                enabled = true,
                onClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun SignInButtonDisabledPreview() {
    AppTheme {
        Box(
            modifier = Modifier
                .size(width = 500.dp, height = 300.dp)
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp),
        ) {
            SignInButton(
                modifier = Modifier
                    .fillMaxWidth(),
                authProvider = AuthProviderUiModel(
                    provider = AuthProvider.APPLE,
                    icon = AppIcons.Apple,
                    titleResource = Res.string.title_auth_sign_in_apple_btn,
                ),
                enabled = false,
                onClick = {},
            )
        }
    }
}