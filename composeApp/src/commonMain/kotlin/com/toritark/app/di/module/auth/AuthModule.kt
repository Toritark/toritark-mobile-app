package com.toritark.app.di.module.auth

import com.toritark.app.data.auth.repository.api.AuthApiRepository
import com.toritark.app.data.auth.repository.api.AuthApiRepositoryImpl
import com.toritark.app.data.core_preferences.Preferences
import com.toritark.app.di.name.AuthSettingsNames
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.domain.auth.interactor.AuthInteractor
import com.toritark.app.domain.auth.interactor.AuthInteractorImpl
import com.toritark.app.presentation.auth.sign_in.SignInViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

    includes(authPlatformModule)

    single<AuthApiRepository> {
        AuthApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    single(named(AuthSettingsNames.AUTH)) {
        val preferences: Preferences = get()
        preferences.createSettings("auth")
    }

    single<AuthInteractor> {
        AuthInteractorImpl(
            authProvidersRepository = get(),
            authApiRepository = get(),
            jwtTokensRepository = get(),
            getDeviceLanguageCode = get(),
            authSettings = get(named(AuthSettingsNames.AUTH)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    viewModel {
        SignInViewModel(
            authInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}