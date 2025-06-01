package com.toritark.app.data.analytics.system

import cocoapods.FBSDKCoreKit.FBSDKAppEvents
import com.toritark.app.data.analytics.model.AnalyticsEvent

internal class FacebookAnalyticsSystem : AnalyticsSystem(name = "Facebook") {

    override fun initialize() {
    }

    override fun setUserId(userId: String) {
        FBSDKAppEvents.shared.setUserID(userId)
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        FBSDKAppEvents.shared.logEvent(
            eventName = event.name,
            parameters = event.parameters.mapKeys { it.key as Any? },
        )
    }

    override fun logScreenViewInternal(screenName: String) {
        logScreenViewAsEvent(screenName)
    }

    override fun logAdRevenue(
        format: String,
        source: String,
        adUnitName: String,
        amount: Double,
        currency: String,
    ) {
        // Not supported on iOS
    }
}