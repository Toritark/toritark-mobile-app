package com.toritark.app.di.module.billing

import com.toritark.app.domain.billing.provider.BillingProvider
import com.toritark.app.domain.billing.provider.BillingProviderImpl
import org.koin.dsl.module

internal actual val platformBillingModule = module {

    single<BillingProvider> {
        BillingProviderImpl(
            isDebug = get(),
            getEnvironment = get(),
        )
    }

}