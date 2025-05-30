package com.toritark.app.di.module.auth

import com.toritark.app.data.auth.repository.AuthProvidersRepository
import com.toritark.app.data.auth.repository.AuthProvidersRepositoryImpl
import org.koin.dsl.module

internal actual val authPlatformModule = module {

    single<AuthProvidersRepository> { AuthProvidersRepositoryImpl() }
}