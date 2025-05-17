@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.data.auth.repository

import com.toritark.app.data.auth.model.auth.AuthProvider

internal actual class AuthProvidersRepositoryImpl : AuthProvidersRepository {

    actual override suspend fun getAuthProviders(): List<AuthProvider> {
        return listOf(
            AuthProvider.GOOGLE,
        )
    }
}