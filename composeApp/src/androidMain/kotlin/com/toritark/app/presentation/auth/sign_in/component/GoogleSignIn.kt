package com.toritark.app.presentation.auth.sign_in.component

import android.app.Activity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.GetCredentialInterruptedException
import androidx.credentials.exceptions.NoCredentialException
import co.touchlab.kermit.Logger
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.toritark.app.BuildConfig
import com.toritark.app.presentation.auth.sign_in.exception.FirebaseSignInException
import com.toritark.app.presentation.auth.sign_in.exception.InvalidCredentialTypeException
import com.toritark.app.presentation.auth.sign_in.exception.SignInException
import com.toritark.app.presentation.auth.sign_in.exception.UserCancelledSignInException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val LOG_TAG = "GoogleSignIn"
private val logger = Logger.withTag(LOG_TAG)

suspend fun signInWithGoogle(
    activity: Activity,
) {
    val credentialManager = CredentialManager.create(activity)

    try {
        val result = try {
            credentialManager.getCredential(
                activity = activity,
                filterByAuthorizedAccounts = true,
            )
        } catch (e: NoCredentialException) {
            logger.d { "signInWithGoogle: No credential for authorized accounts: ${e.message}" }

            credentialManager.getCredential(
                activity = activity,
                filterByAuthorizedAccounts = false,
            )
        }

        logger.d { "signInWithGoogle: Got result: $result" }

        val googleIdTokenCredential = getGoogleIdTokenCredential(result)
        logger.d { "signInWithGoogle: Google ID token credential: $googleIdTokenCredential" }

        signInToFirebaseWithGoogle(googleIdTokenCredential)
        logger.d { "signInWithGoogle: Successfully signed in to Firebase." }
    } catch (e: GetCredentialInterruptedException) {
        logger.d(e) { "signInWithGoogle: Interrupted Google Sign-In: ${e.message}" }

        throw UserCancelledSignInException()
    } catch (e: GetCredentialCancellationException) {
        logger.d(e) { "signInWithGoogle: User cancelled Google Sign-In: ${e.message}" }

        throw UserCancelledSignInException()
    } catch (e: NoCredentialException) {
        logger.e(e) { "signInWithGoogle: No credentials available: ${e.message}" }

        throw SignInException()
    } catch (e: GetCredentialException) {
        logger.e(e) { "signInWithGoogle: Google Sign-In failed: ${e.message}" }

        throw SignInException()
    }
}

private suspend fun CredentialManager.getCredential(
    activity: Activity,
    filterByAuthorizedAccounts: Boolean,
): GetCredentialResponse {
    logger.d { "getCredential: filterByAuthorizedAccounts=$filterByAuthorizedAccounts" }

    val credentialRequest = createGoogleGetCredentialRequest(
        filterByAuthorizedAccounts = filterByAuthorizedAccounts,
    )

    return getCredential(
        request = credentialRequest,
        context = activity,
    )
}

private fun createGoogleGetCredentialRequest(filterByAuthorizedAccounts: Boolean): GetCredentialRequest {
//    val googleIdOption = GetGoogleIdOption.Builder()
//        .setFilterByAuthorizedAccounts(filterByAuthorizedAccounts)
//        .setServerClientId(BuildConfig.GOOGLE_SIGN_IN_SERVER_CLIENT_ID)
//        .setAutoSelectEnabled(filterByAuthorizedAccounts)
//        .build()

    val getSignInWithGoogleOption = GetSignInWithGoogleOption.Builder(BuildConfig.GOOGLE_SIGN_IN_SERVER_CLIENT_ID)
        .build()

    return GetCredentialRequest.Builder()
        .addCredentialOption(getSignInWithGoogleOption)
        .setPreferImmediatelyAvailableCredentials(false)
        .build()
}

private fun getGoogleIdTokenCredential(response: GetCredentialResponse): GoogleIdTokenCredential {
    logger.d { "handleGetCredentialResponse: $response" }

    val credential = response.credential

    if (credential !is CustomCredential) {
        logger.w { "Credential is not CustomCredential: ${credential.type}" }
        throw InvalidCredentialTypeException()
    }

    if (credential.type != GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        logger.w { "Credential type != Google ID token: ${credential.type}" }
        throw InvalidCredentialTypeException()
    }

    try {
        return GoogleIdTokenCredential.createFrom(credential.data)
    } catch (e: GoogleIdTokenParsingException) {
        logger.e(e) { "handleGetCredentialResponse: Google ID token parsing failed: ${e.message}" }

        throw SignInException(message = e.message ?: "Google ID token parsing failed.")
    }
}

private suspend fun signInToFirebaseWithGoogle(googleIdTokenCredential: GoogleIdTokenCredential) {
    logger.d { "signInToFirebaseWithGoogle: googleIdTokenCredential=$googleIdTokenCredential" }

    suspendCoroutine { continuation ->
        val credential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
        Firebase.auth
            .signInWithCredential(credential)
            .addOnSuccessListener {
                logger.d { "signInToFirebaseWithGoogle: Successful sign-in to Firebase." }

                continuation.resume(Unit)
            }
            .addOnFailureListener { e ->
                logger.e(e) { "signInToFirebaseWithGoogle: Failed to sign-in to Firebase: ${e.message}" }

                continuation.resumeWithException(
                    FirebaseSignInException(
                        message = e.message,
                    )
                )
            }
    }
}