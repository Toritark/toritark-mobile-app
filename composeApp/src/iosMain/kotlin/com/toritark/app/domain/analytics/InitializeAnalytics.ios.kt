@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.analytics

import co.touchlab.kermit.Logger
import com.toritark.app.data.analytics.Analytics
import com.toritark.app.data.analytics.system.AnalyticsSystem

actual class InitializeAnalyticsImpl(
    private val analyticsSystems: List<AnalyticsSystem>,
) : InitializeAnalytics {

    private val logger = Logger.withTag(LOG_TAG)

    actual override fun invoke() {
        Analytics.initialize(analyticsSystems)
    }

    private companion object {
        private const val LOG_TAG = "InitializeAnalytics"
    }
}