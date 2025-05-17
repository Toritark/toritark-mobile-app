package com.toritark.app.di.module

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.presentation.splash.SplashViewModel
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