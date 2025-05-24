package com.toritark.app.di.module.analytics

import android.content.Context
import com.toritark.app.data.analytics.system.AmplitudeAnalyticsSystem
import com.toritark.app.data.analytics.system.FacebookAnalyticsSystem
import com.toritark.app.data.analytics.system.FirebaseAnalyticsSystem
import com.toritark.app.data.analytics.system.KochavaAnalyticsSystem
import com.toritark.app.data.analytics.system.MixpanelAnalyticsSystem
import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.analytics.InitializeAnalyticsImpl
import com.toritark.app.domain.core.debug.IsDebug
import org.koin.dsl.module

actual val analyticsPlatformModule = module {

    single<InitializeAnalytics> {
        val context: Context = get()
        val isDebug: IsDebug = get()

        InitializeAnalyticsImpl(
            analyticsSystems = listOf(
                FirebaseAnalyticsSystem(),
                AmplitudeAnalyticsSystem(
                    context = context,
                    isDebug = isDebug,
                ),
                MixpanelAnalyticsSystem(
                    context = context,
                ),
                FacebookAnalyticsSystem(
                    context = context,
                ),
                KochavaAnalyticsSystem(
                    context = context,
                ),
            )
        )
    }
}