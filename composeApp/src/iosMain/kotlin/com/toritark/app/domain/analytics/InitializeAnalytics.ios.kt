@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.analytics

import co.touchlab.kermit.Logger

actual class InitializeAnalyticsImpl : InitializeAnalytics {

    private val logger = Logger.withTag(LOG_TAG)

    actual override fun invoke() {
        logger.e { "InitializeAnalyticImpl is not implemented" }
        // TODO
    }

    private companion object {
        private const val LOG_TAG = "InitializeAnalytics"
    }
}