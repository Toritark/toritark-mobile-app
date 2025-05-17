package com.toritark.app.di.module

import com.toritark.app.data.core_preferences.Preferences
import com.toritark.app.data.onboarding.repository.OnboardingRepository
import com.toritark.app.data.onboarding.repository.OnboardingRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.OnboardingSettingsNames
import com.toritark.app.presentation.onboarding.main.OnboardingMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val onboardingModule = module {

    single(named(OnboardingSettingsNames.ONBOARDING)) {
        val preferences: Preferences = get()
        preferences.createSettings("onboarding")
    }

    single<OnboardingRepository> {
        OnboardingRepositoryImpl(
            settings = get(named(OnboardingSettingsNames.ONBOARDING)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    viewModel {
        OnboardingMainViewModel(
            languagesRepository = get(),
            onboardingRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}