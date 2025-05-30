package com.toritark.app.di.module.analytics

import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.analytics.InitializeAnalyticsImpl
import com.toritark.app.domain.core.debug.IsDebug
import org.koin.dsl.module

actual val analyticsPlatformModule = module {
    single<InitializeAnalytics> {
        val isDebug: IsDebug = get()

        InitializeAnalyticsImpl(
            analyticsSystems = listOf(
                // TODO
            )
        )
    }
}