package com.toritark.app.di.module.ads

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.ads.provider.AdsProvider
import com.toritark.app.domain.ads.provider.AdsProviderImpl
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val adsPlatformModule = module {

    single<AdsProvider> {
        AdsProviderImpl(
            isDebug = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }
}