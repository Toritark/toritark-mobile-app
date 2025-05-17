package com.toritark.app.presentation.auth.sign_in.model.provider

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.presentation.auth.icon.Apple
import com.toritark.app.presentation.auth.icon.Facebook
import com.toritark.app.presentation.auth.icon.Google
import com.toritark.app.presentation.core_ui.icon.AppIcons
import org.jetbrains.compose.resources.StringResource
import toritark.composeapp.generated.resources.Res
import toritark.composeapp.generated.resources.title_auth_sign_in_apple_btn
import toritark.composeapp.generated.resources.title_auth_sign_in_facebook_btn
import toritark.composeapp.generated.resources.title_auth_sign_in_google_btn

@Immutable
internal data class AuthProviderUiModel(
    val provider: AuthProvider,
    val icon: ImageVector,
    val titleResource: StringResource,
) {

    companion object {

        val google by lazy {
            AuthProviderUiModel(
                provider = AuthProvider.GOOGLE,
                icon = AppIcons.Google,
                titleResource = Res.string.title_auth_sign_in_google_btn,
            )
        }

        val apple by lazy {
            AuthProviderUiModel(
                provider = AuthProvider.APPLE,
                icon = AppIcons.Apple,
                titleResource = Res.string.title_auth_sign_in_apple_btn,
            )
        }

        val facebook by lazy {
            AuthProviderUiModel(
                provider = AuthProvider.FACEBOOK,
                icon = AppIcons.Facebook,
                titleResource = Res.string.title_auth_sign_in_facebook_btn,
            )
        }
    }
}
