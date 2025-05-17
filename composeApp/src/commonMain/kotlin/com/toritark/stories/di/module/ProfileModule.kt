package com.toritark.stories.di.module

import com.toritark.stories.di.name.DispatchersNames
import com.toritark.stories.presentation.profile.main.ProfileMainViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {

    viewModel {
        ProfileMainViewModel(
            languagesRepository = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}