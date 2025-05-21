package com.toritark.app.di.module.billing

import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.domain.billing.interactor.BillingInteractor
import com.toritark.app.domain.billing.interactor.BillingInteractorImpl
import com.toritark.app.domain.billing.provider.BillingProvider
import com.toritark.app.domain.billing.provider.BillingProviderImpl
import com.toritark.app.presentation.billing.paywall.PaywallViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val billingModule = module {

    single<BillingProvider> {
        BillingProviderImpl(
            isDebug = get(),
            getEnvironment = get(),
        )
    }

    single<BillingInteractor> {
        BillingInteractorImpl(
            billingProvider = get(),
            profileInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
        )
    }

    viewModel {
        PaywallViewModel(
            billingInteractor = get(),
            profileInteractor = get(),
            defaultDispatcher = get(named(DispatchersNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
            mainDispatcher = get(named(DispatchersNames.MAIN)),
        )
    }
}