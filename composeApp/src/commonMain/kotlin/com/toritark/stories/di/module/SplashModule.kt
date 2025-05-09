package com.toritark.stories.di.module

import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.presentation.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val splashModule = module {

    viewModel {
        SplashViewModel(
            authInteractor = get(),
            onboardingRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}