package com.toritark.app.di.module

import com.toritark.app.data.core_preferences.Preferences
import com.toritark.app.data.profile.api.repository.ProfileApiRepository
import com.toritark.app.data.profile.api.repository.ProfileApiRepositoryImpl
import com.toritark.app.data.profile.repository.ProfileRepository
import com.toritark.app.data.profile.repository.ProfileRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.di.name.ProfileSettingsNames
import com.toritark.app.domain.profile.interactor.ProfileInteractor
import com.toritark.app.domain.profile.interactor.ProfileInteractorImpl
import com.toritark.app.presentation.profile.main.ProfileMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {

    single<ProfileApiRepository> {
        ProfileApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT))
        )
    }

    single(named(ProfileSettingsNames.PROFILE)) {
        val preferences: Preferences = get()
        preferences.createSettings("profile")
    }

    single<ProfileRepository> {
        ProfileRepositoryImpl(
            settings = get(named(ProfileSettingsNames.PROFILE)),
            json = get(),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    single<ProfileInteractor> {
        ProfileInteractorImpl(
            profileApiRepository = get(),
            profileRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    viewModel {
        ProfileMainViewModel(
            profileInteractor = get(),
            billingInteractor = get(),
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}