package com.toritark.app.presentation.auth.sign_in.exception

internal open class SignInException(
    message: String? = null,
) : Exception(message)

internal class UnsupportedProviderException : SignInException()
internal class UserCancelledSignInException : SignInException()
internal class InvalidCredentialTypeException : SignInException()
internal class FirebaseSignInException(message: String? = null) : SignInException(message)
