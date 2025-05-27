package com.toritark.app.presentation.auth.sign_in.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import co.touchlab.kermit.Logger
import cocoapods.FirebaseAuth.FIRAuth
import cocoapods.FirebaseAuth.FIROAuthProvider
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.presentation.auth.sign_in.SignInViewModel
import com.toritark.app.presentation.auth.sign_in.exception.FirebaseSignInException
import com.toritark.app.presentation.auth.sign_in.exception.SignInException
import com.toritark.app.presentation.auth.sign_in.exception.UnsupportedProviderException
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.String
import kotlinx.coroutines.suspendCancellableCoroutine
import okio.ByteString.Companion.toByteString
import platform.AuthenticationServices.ASAuthorizationAppleIDCredential
import kotlin.coroutines.resume

private val logger = Logger.withTag("PlatformSignInHandler")

@Composable
internal actual fun PlatformSignInHandler(viewModel: SignInViewModel) {

    LaunchedEffect(viewModel) {
        viewModel.signInRequestEvents.collect { authProvider ->
            viewModel.setIsSigningIn(true)

            try {
                val firebaseToken = platformSignIn(
                    authProvider = authProvider,
                )

                logger.d { "platformSignIn: Successfully signed in to Firebase." }

                viewModel.handleSignInToken(token = firebaseToken)
            } catch (e: SignInException) {
                logger.e(e) { "platformSignIn: SignInException: ${e.message}" }

                viewModel.handleSignInException(exception = e)
            }
        }
    }
}

private suspend fun platformSignIn(
    authProvider: AuthProvider,
): String {
    logger.d { "platformSignIn: authProvider: $authProvider" }

    return when (authProvider) {
        AuthProvider.APPLE -> {
            val credential = signInWithApple()
            getFirebaseIdToken(credential)
        }

        else -> {
            logger.e { "platformSignIn: authProvider $authProvider is not supported" }
            throw UnsupportedProviderException()
        }
    }
}

private suspend fun getFirebaseIdToken(
    credential: ASAuthorizationAppleIDCredential,
): String {
    return suspendCancellableCoroutine coroutine@{ continuation ->
        val appleTokenBytes = credential.identityToken?.toByteString()?.toByteArray()
        if (appleTokenBytes == null) {
            throw FirebaseSignInException("Apple token is null")
        }

        val appleToken = appleTokenBytes.decodeToString()

        val firebaseCredential = FIROAuthProvider.appleCredentialWithIDToken(
            idToken = appleToken,
            rawNonce = null,
            fullName = credential.fullName,
        )

        FIRAuth.auth().signInWithCredential(
            credential = firebaseCredential,
        ) { authResult, error ->
            if (error != null) {
                throw FirebaseSignInException(error.localizedDescription)
            }

            if (authResult == null) {
                throw FirebaseSignInException("Failed to sign in")
            }

            authResult.user().getIDTokenWithCompletion { firebaseToken, error ->
                if (error != null || firebaseToken == null) {
                    throw FirebaseSignInException("Failed to get Firebase token")
                }

                continuation.resume(firebaseToken)
            }
        }
    }
}