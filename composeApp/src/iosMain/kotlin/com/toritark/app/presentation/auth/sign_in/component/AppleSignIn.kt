package com.toritark.app.presentation.auth.sign_in.component

import com.toritark.app.presentation.auth.sign_in.exception.SignInException
import com.toritark.app.presentation.auth.sign_in.exception.UserCancelledSignInException
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AuthenticationServices.*
import platform.Foundation.NSError
import platform.UIKit.UIApplication
import platform.darwin.NSObject
import kotlin.coroutines.resumeWithException
import platform.AuthenticationServices.ASAuthorizationControllerPresentationContextProvidingProtocol as PresentationContext

private val UiContext: PresentationContext by lazy {
    object : PresentationContext, NSObject() {
        override fun presentationAnchorForAuthorizationController(
            controller: ASAuthorizationController,
        ): ASPresentationAnchor = requireNotNull(UIApplication.sharedApplication.keyWindow)
    }
}

internal suspend fun signInWithApple(): ASAuthorizationAppleIDCredential {
    val appleIdProvider = ASAuthorizationAppleIDProvider()

    val request = appleIdProvider.createRequest().apply {
        requestedScopes = listOf(
            ASAuthorizationScopeEmail,
            ASAuthorizationScopeFullName,
        )
    }

    val controller = ASAuthorizationController(listOf(request))
    return controller.performSignIn()
}

private suspend fun ASAuthorizationController.performSignIn(): ASAuthorizationAppleIDCredential {
    return suspendCancellableCoroutine coroutine@{ continuation ->

        // we MUST store a strong reference to the object or is fill be cleared
        val delegate = AuthorizationDelegate(
            onComplete = { 
                continuation.resume(it) { cause, _, _ ->
                }
            },
            onError = { error ->
                continuation.resumeWithException(error)
            }
        )

        continuation.invokeOnCancellation {
            // we MUST keep a strong reference until full completion because cinterop ASAuthorizationController.delegate
            // is a weak reference. If you remove this (redundant) call, it will stop working.
            delegate.onComplete = {}
            delegate.onError = {}
            cancel()
        }

        presentationContextProvider = UiContext
        this.delegate = delegate

        performRequests()
    }
}

private class AuthorizationDelegate(
    var onComplete: (result: ASAuthorizationAppleIDCredential) -> Unit,
    var onError: (exception: SignInException) -> Unit,
) : NSObject(), ASAuthorizationControllerDelegateProtocol {

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithAuthorization: ASAuthorization,
    ) {
        val result = when (val credential = didCompleteWithAuthorization.credential) {
            is ASAuthorizationAppleIDCredential -> credential
            else -> {
                onError(SignInException("Unrecognized credential type: $credential"))
                return
            }
        }
        onComplete(result)
    }

    override fun authorizationController(
        controller: ASAuthorizationController,
        didCompleteWithError: NSError,
    ) {
        val exception = when (didCompleteWithError.code) {
            ASAuthorizationErrorCanceled -> UserCancelledSignInException()
            else -> SignInException("Failed to sign in: code=${didCompleteWithError.code}")
        }

        onError(exception)
    }
}
