package com.toritark.app.data.analytics.system

import android.content.Context
import com.kochava.tracker.Tracker
import com.kochava.tracker.TrackerApi
import com.kochava.tracker.events.Event
import com.toritark.app.BuildKonfig
import com.toritark.app.data.analytics.model.AnalyticsEvent

internal class KochavaAnalyticsSystem(
    private val context: Context,
) : AnalyticsSystem(name = "Kochava") {

    private var trackerApi: TrackerApi? = null

    override fun initialize() {
        trackerApi = Tracker.getInstance().apply {
            startWithAppGuid(context, BuildKonfig.KOCHAVA_APP_GUID)
        }
    }

    override fun setUserId(userId: String) {
        trackerApi?.registerIdentityLink("user_id", userId)
    }

    override fun logEventInternal(event: AnalyticsEvent) {
        Event.buildWithEventName(event.name)
            .apply {
                event.parameters.forEach { (key, value) ->
                    when (value) {
                        is String -> setCustomStringValue(key, value)
                        is Boolean -> setCustomBoolValue(key, value)
                        is Number -> setCustomNumberValue(key, value.toDouble())
                        else -> setCustomStringValue(key, value.toString())
                    }
                }
            }
            .send()
    }
}