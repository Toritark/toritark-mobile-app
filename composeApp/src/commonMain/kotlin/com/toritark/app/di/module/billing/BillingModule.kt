package com.toritark.app.di.module.billing

import com.toritark.app.data.billing.api.repository.BillingApiRepository
import com.toritark.app.data.billing.api.repository.BillingApiRepositoryImpl
import com.toritark.app.data.billing.repository.BillingSettingsRepository
import com.toritark.app.data.billing.repository.BillingSettingsRepositoryImpl
import com.toritark.app.data.core_preferences.Preferences
import com.toritark.app.di.name.BillingSettingsNames
import com.toritark.app.di.name.DispatchersNames
import com.toritark.app.di.name.HttpClientNames
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

    single<BillingApiRepository> {
        BillingApiRepositoryImpl(
            httpClient = get(named(HttpClientNames.DEFAULT)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    single(named(BillingSettingsNames.BILLING)) {
        val preferences: Preferences = get()
        preferences.createSettings("billing")
    }

    single<BillingSettingsRepository> {
        BillingSettingsRepositoryImpl(
            settings = get(named(BillingSettingsNames.BILLING)),
            ioDispatcher = get(named(DispatchersNames.IO)),
        )
    }

    single<BillingInteractor> {
        BillingInteractorImpl(
            billingApiRepository = get(),
            billingSettingsRepository = get(),
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