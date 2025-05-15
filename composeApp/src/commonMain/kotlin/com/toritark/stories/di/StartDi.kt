package com.toritark.stories.di

import com.toritark.stories.di.module.*
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
        mainModule,
        *additionalModules,
    )
}