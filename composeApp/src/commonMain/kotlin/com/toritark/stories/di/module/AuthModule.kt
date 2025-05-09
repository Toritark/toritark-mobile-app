package com.toritark.stories.di.module

import com.toritark.stories.data.auth.repository.AuthApiRepository
import com.toritark.stories.data.auth.repository.AuthApiRepositoryImpl
import com.toritark.stories.data.core_preferences.Preferences
import com.toritark.stories.di.name.AuthSettingsNames
import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.di.name.HttpClientNames
import com.toritark.stories.domain.auth.interactor.AuthInteractor
import com.toritark.stories.domain.auth.interactor.AuthInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule = module {

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
            authApiRepository = get(),
            jwtTokensRepository = get(),
            getDeviceLanguageCode = get(),
            authSettings = get(named(AuthSettingsNames.AUTH)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }
}