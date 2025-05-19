package com.toritark.app.di.module.ads

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.ads.interactor.AdsInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val adsModule = module {
    includes(adsPlatformModule)

    single<AdsInteractor> {
        AdsInteractorImpl(
            adsProvider = get(),
            profileInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }
}