@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.toritark.app.domain.analytics

interface InitializeAnalytics {
    operator fun invoke()
}

expect class InitializeAnalyticsImpl() : InitializeAnalytics {
    override operator fun invoke()
}