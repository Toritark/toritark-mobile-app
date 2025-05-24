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
}