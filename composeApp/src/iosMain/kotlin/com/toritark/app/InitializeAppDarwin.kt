package com.toritark.app

import co.touchlab.kermit.Logger
import com.toritark.app.di.configureModules
import com.toritark.app.domain.analytics.InitializeAnalytics
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

private val logger = Logger.withTag("InitializeAppDarwin")

fun initializeApp() {
    val koinApplication = startDi()
    koinApplication.initializeAnalytics()
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