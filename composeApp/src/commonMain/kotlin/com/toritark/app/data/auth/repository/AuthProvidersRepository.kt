@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.data.auth.repository

import com.toritark.app.data.auth.model.auth.AuthProvider

internal interface AuthProvidersRepository {
    suspend fun getAuthProviders(): List<AuthProvider>
}

internal expect class AuthProvidersRepositoryImpl : AuthProvidersRepository {
    override suspend fun getAuthProviders(): List<AuthProvider>
}