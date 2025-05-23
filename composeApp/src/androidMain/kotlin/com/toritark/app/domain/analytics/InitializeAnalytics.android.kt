@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.analytics

import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.system.AnalyticsSystem

actual class InitializeAnalyticsImpl(
    private val analyticsSystems: List<AnalyticsSystem>,
) : InitializeAnalytics {

    actual constructor() : this(emptyList())

    actual override operator fun invoke() {
        Analytics.initialize(analyticsSystems)
    }
}