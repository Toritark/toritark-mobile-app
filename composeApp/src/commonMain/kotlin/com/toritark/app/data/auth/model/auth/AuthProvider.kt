package com.toritark.app.data.auth.model.auth

enum class AuthProvider(
    val value: String,
) {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    APPLE("apple"),
}