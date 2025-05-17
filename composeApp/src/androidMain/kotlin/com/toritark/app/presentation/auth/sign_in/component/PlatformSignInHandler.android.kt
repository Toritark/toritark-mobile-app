package com.toritark.app.presentation.auth.sign_in.component

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import co.touchlab.kermit.Logger
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.toritark.app.data.auth.model.auth.AuthProvider
import com.toritark.app.presentation.auth.sign_in.SignInViewModel
import com.toritark.app.presentation.auth.sign_in.exception.FirebaseSignInException
import com.toritark.app.presentation.auth.sign_in.exception.SignInException
import com.toritark.app.presentation.auth.sign_in.exception.UnsupportedProviderException
import com.toritark.app.util.extension.activity
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val LOG_TAG = "PlatformSignInHandler"
private val logger = Logger.withTag(LOG_TAG)

@Composable
internal actual fun PlatformSignInHandler(viewModel: SignInViewModel) {
    val context = LocalContext.current

    val activity = remember(context) {
        requireNotNull(context.activity) { "PlatformSignInHandler: Activity is null" }
    }

    LaunchedEffect(viewModel) {
        viewModel.signInRequestEvents.collect { authProvider ->
            viewModel.setIsSigningIn(true)

            try {
                val firebaseToken = platformSignIn(
                    activity = activity,
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
    activity: Activity,
    authProvider: AuthProvider,
): String {
    logger.d { "platformSignIn: authProvider: $authProvider" }

    return when (authProvider) {
        AuthProvider.GOOGLE -> {
            signInWithGoogle(activity = activity)
            getFirebaseIdToken()
        }

        else -> {
            logger.e { "platformSignIn: authProvider $authProvider is not supported" }
            throw UnsupportedProviderException()
        }
    }
}

private suspend fun getFirebaseIdToken(): String {
    return suspendCoroutine { continuation ->
        val user = Firebase.auth.currentUser
        if (user == null) {
            logger.e { "getFirebaseIdToken: User is null" }
            continuation.resumeWithException(
                FirebaseSignInException(message = "User is null")
            )
            return@suspendCoroutine
        }

        user
            .getIdToken(false)
            .addOnSuccessListener { result ->
                logger.d { "getFirebaseIdToken: Got ID token: ${result.token}" }

                val token = result.token
                if (token != null) {
                    continuation.resume(token)
                } else {
                    logger.e { "getFirebaseIdToken: ID token is null" }
                    continuation.resumeWithException(
                        FirebaseSignInException(message = "ID token is null")
                    )
                }
            }
            .addOnFailureListener { e ->
                logger.e(e) { "getFirebaseIdToken: Failed to get ID token: ${e.message}" }

                continuation.resumeWithException(
                    FirebaseSignInException(message = e.message ?: "Failed to get Firebase ID token.")
                )
            }
    }
}