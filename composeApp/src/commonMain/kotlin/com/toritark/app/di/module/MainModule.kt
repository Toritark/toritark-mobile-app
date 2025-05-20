package com.toritark.app.di.module

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.presentation.main.screen.MainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainModule = module {

    viewModel {
        MainViewModel(
            profileInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}