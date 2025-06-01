package com.toritark.app.di.module.analytics

import com.toritark.app.data.analytics.system.FacebookAnalyticsSystem
import com.toritark.app.data.analytics.system.FirebaseAnalyticsSystem
import com.toritark.app.data.analytics.system.MixpanelAnalyticsSystem
import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.analytics.InitializeAnalyticsImpl
import com.toritark.app.domain.core.debug.IsDebug
import org.koin.dsl.module

actual val analyticsPlatformModule = module {

    single<InitializeAnalytics> {
        val isDebug: IsDebug = get()

        InitializeAnalyticsImpl(
            analyticsSystems = listOf(
                FirebaseAnalyticsSystem(),
                MixpanelAnalyticsSystem(
                    isDebug = isDebug,
                ),
                FacebookAnalyticsSystem(),
            ),
        )
    }
}