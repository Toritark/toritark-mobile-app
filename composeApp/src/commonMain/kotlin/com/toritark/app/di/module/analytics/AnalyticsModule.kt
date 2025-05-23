package com.toritark.app.di.module.analytics

import com.toritark.app.domain.analytics.InitializeAnalytics
import com.toritark.app.domain.analytics.InitializeAnalyticsImpl
import org.koin.dsl.module

val analyticsModule = module {

    includes(analyticsPlatformModule)
}