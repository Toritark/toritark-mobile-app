package com.toritark.app.data.analytics.system

import android.content.Context
import com.facebook.appevents.AppEventsLogger
import com.toritark.app.data.analytics.extension.bundleParameters
import com.toritark.app.data.analytics.model.AnalyticsEvent

internal class FacebookAnalyticsSystem(
    private val context: Context,
) : AnalyticsSystem(name = "Facebook") {

    private var appEventsLogger: AppEventsLogger? = null

    override fun initialize() {
        appEventsLogger = AppEventsLogger.newLogger(context)
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        appEventsLogger?.logEvent(event.name, event.bundleParameters)
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
        // TODO
    }
}