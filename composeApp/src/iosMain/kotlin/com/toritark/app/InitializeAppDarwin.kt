package com.toritark.app

import co.touchlab.kermit.Logger
import com.toritark.app.di.configureModules
import com.toritark.app.domain.ads.interactor.AdsInteractor
import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.billing.interactor.BillingInteractor
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

private val logger = Logger.withTag("InitializeAppDarwin")

@Suppress("unused")
fun initializeApp() {
    startDi().apply {
        initializeAnalytics()
        initializeAds()
        initializeBilling()
    }
}

private fun startDi(): KoinApplication {
    return startKoin {
        configureModules()
    }
}

private fun KoinApplication.initializeAnalytics() {
    logger.d { "initializeAnalytics" }

    val initializeAnalytics: InitializeAnalytics = koin.get()
    initializeAnalytics()
}

private fun KoinApplication.initializeAds() {
    logger.d { "initializeAds" }

    val adsInteractor = koin.get<AdsInteractor>()
    adsInteractor.initialize()
}

private fun KoinApplication.initializeBilling() {
    logger.d { "initializeBilling" }

    val billingInteractor = koin.get<BillingInteractor>()
    billingInteractor.initialize()
}