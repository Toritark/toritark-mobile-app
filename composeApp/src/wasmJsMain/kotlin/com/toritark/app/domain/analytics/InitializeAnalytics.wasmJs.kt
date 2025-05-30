package com.toritark.app.domain.analytics

import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.system.AnalyticsSystem

actual class InitializeAnalyticsImpl(
    private val analyticsSystems: List<AnalyticsSystem>,
) : InitializeAnalytics {
    
    actual override operator fun invoke() {
        Analytics.initialize(analyticsSystems)
    }
}