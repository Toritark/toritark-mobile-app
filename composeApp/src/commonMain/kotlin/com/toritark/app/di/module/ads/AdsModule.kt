package com.toritark.app.di.module.ads

import com.toritark.app.data.ads.api.repository.AdsApiRepository
import com.toritark.app.data.ads.api.repository.AdsApiRepositoryImpl
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.ads.interactor.AdsInteractorImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

val adsModule = module {
    includes(adsPlatformModule)

    single<AdsApiRepository> {
        AdsApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    single<AdsInteractor> {
        AdsInteractorImpl(
            adsProvider = get(),
            adsApiRepository = get(),
            profileInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }
}