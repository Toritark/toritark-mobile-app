package com.toritark.app.di

import com.toritark.app.di.module.*
import com.toritark.app.di.module.ads.adsModule
import com.toritark.app.di.module.auth.authModule
import com.toritark.app.di.module.billing.billingModule
import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun KoinApplication.configureModules(vararg additionalModules: Module) {
    modules(
        platformPreferencesModule,
        coreModule,
        coreApiModule,
        authModule,
        languageModule,
        onboardingModule,
        splashModule,
        storyModule,
        learningWordsModule,
        profileModule,
        adsModule,
        billingModule,
        mainModule,
        *additionalModules,
    )
}