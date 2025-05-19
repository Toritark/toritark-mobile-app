package com.toritark.app.di

import com.toritark.app.di.module.*
import com.toritark.app.di.module.ads.adsModule
import com.toritark.app.di.module.auth.authModule
import org.koin.core.KoinApplication
import org.koin.core.module.Module

fun KoinApplication.configureModules(vararg additionalModules: Module) {
    modules(
        platformPreferencesModule,
        coreModule,
        platformCoreModule,
        coreApiModule,
        authModule,
        languageModule,
        onboardingModule,
        splashModule,
        storyModule,
        learningWordsModule,
        profileModule,
        adsModule,
        mainModule,
        *additionalModules,
    )
}