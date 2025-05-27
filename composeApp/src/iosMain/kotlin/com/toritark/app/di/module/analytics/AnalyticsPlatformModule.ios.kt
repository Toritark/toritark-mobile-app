package com.toritark.app.di.module.analytics

import com.toritark.app.data.analytics.system.FirebaseAnalyticsSystem
import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.analytics.InitializeAnalyticsImpl
import org.koin.dsl.module

actual val analyticsPlatformModule = module {

    single<InitializeAnalytics> {
        InitializeAnalyticsImpl(
            analyticsSystems = listOf(
                FirebaseAnalyticsSystem(),
                // TODO: Add other systems
            ),
        )
    }
}